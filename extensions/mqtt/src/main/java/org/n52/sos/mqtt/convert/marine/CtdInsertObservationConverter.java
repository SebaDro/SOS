/*
 * Copyright (C) 2017 52north.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.n52.sos.mqtt.convert.marine;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.List;
import org.joda.time.DateTime;
import org.n52.sos.exception.ows.concrete.InvalidSridException;
import org.n52.sos.mqtt.api.CtdMessage;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.gml.ReferenceType;
import org.n52.sos.ogc.om.NamedValue;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.sos.ogc.om.values.GeometryValue;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.util.GeometryHandler;
import org.n52.sos.util.JTSHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastian Drost
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
