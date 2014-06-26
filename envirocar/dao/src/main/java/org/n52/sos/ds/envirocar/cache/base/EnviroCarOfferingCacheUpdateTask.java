/**
 * Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.ds.envirocar.cache.base;

import java.util.Collection;
import java.util.List;

import org.envirocar.server.core.entities.Sensor;
import org.envirocar.server.core.entities.Sensors;
import org.envirocar.server.core.entities.Track;
import org.envirocar.server.core.entities.Tracks;
import org.envirocar.server.core.filter.PhenomenonFilter;
import org.envirocar.server.core.filter.TrackFilter;
import org.envirocar.server.mongo.dao.MongoPhenomenonDao;
import org.envirocar.server.mongo.dao.MongoTrackDao;
import org.envirocar.server.mongo.dao.MongoTrackDao.TimeExtrema;
import org.n52.sos.ds.envirocar.EnviroCarDaoFactory;
import org.n52.sos.ds.envirocar.cache.AbstractEnviroCarThreadableDatasourceCacheUpdate;
import org.n52.sos.exception.ows.concrete.GenericThrowableWrapperException;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.features.SfConstants;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosEnvelope;
import org.n52.sos.service.Configurator;
import org.n52.sos.util.CacheHelper;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.StringHelper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EnviroCarOfferingCacheUpdateTask extends AbstractEnviroCarThreadableDatasourceCacheUpdate {

    private Sensor sensor;

    public EnviroCarOfferingCacheUpdateTask(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public void execute() {
        try {
            // perform single threaded updates here
            String offeringId = sensor.getIdentifier();
            String prefixedOfferingId = CacheHelper.addPrefixOrGetOfferingIdentifier(offeringId);

            getCache().addOffering(prefixedOfferingId);
            getCache().setNameForOffering(prefixedOfferingId, getOfferingName(prefixedOfferingId, offeringId));

            // time ranges
            MongoTrackDao dao = (MongoTrackDao) getEnviroCarDaoFactory().getTrackDAO();
            TimeExtrema ote = dao.getTimeExtrema(offeringId);
            if (ote != null) {
                getCache().setMinPhenomenonTimeForOffering(offeringId, ote.getMinPhenomenonTime());
                getCache().setMaxPhenomenonTimeForOffering(offeringId, ote.getMaxPhenomenonTime());
                getCache().setMinResultTimeForOffering(offeringId, ote.getMinResultTime());
                getCache().setMaxResultTimeForOffering(offeringId, ote.getMaxResultTime());
            }

            // procedures
            getCache().setProceduresForOffering(offeringId, Sets.newHashSet(offeringId));

            // Observable properties
            Collection<Object> fromMeasurments =
                    ((MongoPhenomenonDao) getEnviroCarDaoFactory().getPhenomenonDAO())
                            .getFromMeasurments(new PhenomenonFilter().setSensors(Sensors
                                    .from(Sets.newHashSet(sensor)).build()));
            // Sensors sensors = getSensors(offProc, getEnviroCarDaoFactory());
            // Collection<String> observablePropertyIdentifier =
            // getObservablePropertyIdentifier(sensors,
            // getEnviroCarDaoFactory());
            // getCache().setObservablePropertiesForOffering(offeringId,
            // observablePropertyIdentifier);

            // Observation types
            getCache().setObservationTypesForOffering(offeringId, Sets.newHashSet(OmConstants.OBS_TYPE_MEASUREMENT));

            // Features of Interest
            List<String> featureOfInterestIdentifiers = Lists.newArrayList();
            Tracks tracks = getEnviroCarDaoFactory().getTrackDAO().get(new TrackFilter().setSensorType(offeringId));
            for (Track track : tracks) {
                featureOfInterestIdentifiers.add(track.getIdentifier());
            }
            getCache().setFeaturesOfInterestForOffering(offeringId, featureOfInterestIdentifiers);
            getCache().setFeatureOfInterestTypesForOffering(offeringId,
                    Sets.newHashSet(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE));

            // Spatial Envelope
            SosEnvelope envelopeForOffering =
                    getEnvelopeForOffering(featureOfInterestIdentifiers, getEnviroCarDaoFactory());
            getCache().setEnvelopeForOffering(offeringId, envelopeForOffering);

            // Spatial Filtering Profile Spatial Envelope
            getCache().setSpatialFilteringProfileEnvelopeForOffering(offeringId, envelopeForOffering);

        } catch (OwsExceptionReport owse) {
            getErrors().add(owse);
        } catch (Exception e) {
            getErrors().add(
                    new GenericThrowableWrapperException(e)
                            .withMessage("Error while processing offering cache update task!"));
        }
    }

    private String getOfferingName(String offeringIdentifier, String offeringName) {
        if (!StringHelper.isNotEmpty(offeringName)) {
            offeringName = offeringIdentifier;
            if (offeringName.startsWith("http")) {
                offeringName = offeringName.substring(offeringName.lastIndexOf('/') + 1, offeringName.length());
            } else if (offeringName.startsWith("urn")) {
                offeringName = offeringName.substring(offeringName.lastIndexOf(':') + 1, offeringName.length());
            }
            if (offeringName.contains("#")) {
                offeringName = offeringName.substring(offeringName.lastIndexOf('#') + 1, offeringName.length());
            }
        }
        return offeringName;
    }

    private SosEnvelope getEnvelopeForOffering(Collection<String> featureOfInterestIdentifiers,
            EnviroCarDaoFactory enviroCarDaoFactory) throws OwsExceptionReport {
        if (CollectionHelper.isNotEmpty(featureOfInterestIdentifiers)) {
            return Configurator.getInstance().getFeatureQueryHandler()
                    .getEnvelopeForFeatureIDs(featureOfInterestIdentifiers, enviroCarDaoFactory);
        }
        return null;
    }

}
