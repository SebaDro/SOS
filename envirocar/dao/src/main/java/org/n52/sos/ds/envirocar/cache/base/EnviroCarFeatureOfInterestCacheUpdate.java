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

import org.envirocar.server.core.entities.Track;
import org.n52.sos.ds.envirocar.cache.AbstractEnviroCarThreadableDatasourceCacheUpdate;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.service.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class EnviroCarFeatureOfInterestCacheUpdate extends AbstractEnviroCarThreadableDatasourceCacheUpdate {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviroCarFeatureOfInterestCacheUpdate.class);

    @Override
    public void execute() {
        LOGGER.debug("Executing EnviroCarFeatureOfInterestCacheUpdate");
        startStopwatch();
        // FIXME shouldn't the identifiers be translated using
        // CacheHelper.addPrefixAndGetFeatureIdentifier()?
        try {
            for (Track track : getEnviroCarDaoFactory().getTrackDAO().get()) {
                String featureOfInterestIdentifier = track.getIdentifier();
                getCache().addFeatureOfInterest(featureOfInterestIdentifier);

                // TODO chang to sensor identifier???
                getCache().setProceduresForFeatureOfInterest(featureOfInterestIdentifier,
                        Sets.newHashSet(track.getSensor().getType()));
            }
            getCache().setGlobalEnvelope(
                    getFeatureQueryHandler().getEnvelopeForFeatureIDs(getCache().getFeaturesOfInterest(),
                            getEnviroCarDaoFactory()));
            getCache().addEpsgCode(
                    Integer.valueOf(Configurator.getInstance().getFeatureQueryHandler().getDefaultEPSG()));
        } catch (final OwsExceptionReport ex) {
            getErrors().add(ex);
        }
        LOGGER.debug("Finished executing EnviroCarFeatureOfInterestCacheUpdate ({})", getStopwatchResult());
    }

}
