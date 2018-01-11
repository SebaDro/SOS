/*
 * Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.mqtt.convert.marine;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.List;
import org.joda.time.DateTime;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.util.JTSHelper;
import org.n52.sos.mqtt.api.CtdMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class CtdInsertObservationConverter extends AbstractMarineInsertObservationConverter<CtdMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(CtdInsertObservationConverter.class);

    @Override
    protected List<OmObservation> createObservations(CtdMessage message) throws OwsExceptionReport {
        String procedure = message.getProcedure();
        DateTime phenomenonTime = message.getShoreStationTime();
        DateTime resultTime = message.getShoreStationTime();

        List<OmObservation> observations = Lists.newArrayList();
        observations.add(createQuantityObservation(message, procedure, CtdMessage.PRESSURE,
                message.getPressure(), CtdMessage.PRESSURE_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(message, procedure, CtdMessage.CONDUCTIVITY,
                message.getConductivity(), CtdMessage.CONDUCTIVITY_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(message, procedure, CtdMessage.SALINITY,
                message.getSalinity(), CtdMessage.SALINITY_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(message, procedure, CtdMessage.SOUND_VELOCITY,
                message.getSoundVelocity(), CtdMessage.SOUND_VELOCITY_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(message, procedure, CtdMessage.TEMPERATURE,
                message.getTemperature(), CtdMessage.TEMPERATURE_UNIT,
                phenomenonTime, resultTime));

        return observations;
    }

    @Override
    protected List<String> createOfferings(CtdMessage message) {
        return Lists.newArrayList(message.getSensorId());
    }

    @Override
    protected AbstractFeature createFeatureOfInterest(CtdMessage message) {
        SamplingFeature samplingFeature = new SamplingFeature(new CodeWithAuthority(message.getSensorId()));
        samplingFeature.addName(message.getSensorId());
        try {
            samplingFeature.setGeometry(createGeometry());
        } catch (InvalidSridException ex) {
            LOG.warn("Can not create geometry for SamplingFeature.", ex);
        } finally {
            return samplingFeature;
        }
    }

    private Geometry createGeometry() throws OwsExceptionReport {
        int epsg = 4326;
        double lat = 53.227;
        double lon = -9.271;
        GeometryFactory factory = JTSHelper.getGeometryFactoryForSRID(epsg);
        Geometry geometry = factory.createPoint(new Coordinate(lat, lon));
        return geometry;
    }

    @Override
    protected NamedValue<?> createSpatialFilteringProfileParameter(CtdMessage message) throws OwsExceptionReport {
        final NamedValue<Geometry> namedValue = new NamedValue<Geometry>();
        namedValue.setName(new ReferenceType(Sos2Constants.HREF_PARAMETER_SPATIAL_FILTERING_PROFILE));
        Geometry geometry = createGeometry();
        namedValue.setValue(new GeometryValue(geometry));
        return namedValue;
    }

}
