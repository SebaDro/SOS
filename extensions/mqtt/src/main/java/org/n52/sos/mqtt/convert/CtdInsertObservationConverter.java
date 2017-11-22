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
package org.n52.sos.mqtt.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTime;
import org.n52.sos.mqtt.api.CtdMessage;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.om.AbstractPhenomenon;
import org.n52.sos.ogc.om.ObservationValue;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.OmObservableProperty;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.om.OmObservationConstellation;
import org.n52.sos.ogc.om.SingleObservationValue;
import org.n52.sos.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.v20.PhysicalSystem;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.request.RequestContext;
import org.n52.sos.util.net.IPAddress;

/**
 *
 * @author Sebastian Drost
 */
public class CtdInsertObservationConverter implements MqttInsertObservationConverter<CtdMessage> {

    @Override
    public InsertObservationRequest convert(CtdMessage message) throws OwsExceptionReport {
        String procedure = message.getProcedure();
        String featureId = message.getProcedure();
        String offering = message.getProcedure();
        DateTime phenomenonTime = message.getShoreStationTime();
        DateTime resultTime = message.getShoreStationTime();

        List<OmObservation> observations = Lists.newArrayList();
        observations.add(createQuantityObservation(procedure, CtdMessage.PRESSURE,
                featureId, offering, message.getPressure(), CtdMessage.PRESSURE_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(procedure, CtdMessage.CONDUCTIVITY,
                featureId, offering, message.getConductivity(), CtdMessage.CONDUCTIVITY_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(procedure, CtdMessage.SALINITY,
                featureId, offering, message.getSalinity(), CtdMessage.SALINITY_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(procedure, CtdMessage.SOUND_VELOCITY,
                featureId, offering, message.getSoundVelocity(), CtdMessage.SOUND_VELOCITY_UNIT,
                phenomenonTime, resultTime));
        observations.add(createQuantityObservation(procedure, CtdMessage.TEMPERATURE,
                featureId, offering, message.getTemperature(), CtdMessage.TEMPERATURE_UNIT,
                phenomenonTime, resultTime));

        InsertObservationRequest request = new InsertObservationRequest();
        request.setService(SosConstants.SOS);
        request.setVersion(Sos2Constants.SERVICEVERSION);
        RequestContext requestContext = new RequestContext();
        requestContext.setIPAddress(new IPAddress("127.0.0.1"));
        request.setRequestContext(requestContext);
        request.setOfferings(Lists.newArrayList(message.getSensorId()));
        request.setObservation(observations);
        return request;
    }

    private OmObservation createQuantityObservation(String procedure, String phenomenon, String featureId, String offering, double value, String unit, DateTime phenomenonTime, DateTime resultTime) {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(procedure, phenomenon, featureId, offering));
        observation.setValue(createQuantityObservationValue(phenomenonTime, value, unit));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    private OmObservationConstellation createObservationConstellation(String procedure, String phenomenon, String featureId, String offering) {
        OmObservationConstellation constellation = new OmObservationConstellation();
        constellation.setObservableProperty(createPhenomenon(phenomenon));
        constellation.setFeatureOfInterest(createFeatureOfInterest(featureId));
        constellation.setOfferings(createOffering(offering));
        constellation.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        constellation.setProcedure(createProcedure(procedure));
        return constellation;
    }

    private ObservationValue<?> createQuantityObservationValue(DateTime time, double value, String unit) {
        SingleObservationValue<Double> obsValue = new SingleObservationValue<>();
        QuantityValue quantityValue = new QuantityValue(value);
        quantityValue.setUnit(unit);
        obsValue.setValue(quantityValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    private AbstractFeature createFeatureOfInterest(String identifier) {;
        SamplingFeature samplingFeature = new SamplingFeature(new CodeWithAuthority(identifier));
        samplingFeature.addName(identifier);
        return samplingFeature;
    }

    private SosProcedureDescription createProcedure(String procedure) {
        return new PhysicalSystem().setIdentifier(procedure);
    }

    private Set<String> createOffering(String offering) {
        return Sets.newHashSet(offering);
    }

    private AbstractPhenomenon createPhenomenon(String identifier) {
        return new OmObservableProperty(identifier);
    }

}
