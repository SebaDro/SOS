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
import org.n52.sos.ogc.om.values.TextValue;
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
        List<OmObservation> observations = Lists.newArrayList();
        observations.add(createPressureObservation(message));
        observations.add(createTemperatureObservation(message));
        observations.add(createConditionObservation(message));
        observations.add(createSaltinessObservation(message));
//        observations.add(createSoundVObservation(message));

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

    private OmObservation createPressureObservation(CtdMessage message) {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, CtdMessage.PRESSURE));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getPressure(), CtdMessage.PRESSURE_UNIT));
        observation.setResultTime(new TimeInstant(message.getTime()));
        return observation;
    }

    private OmObservation createTemperatureObservation(CtdMessage message) {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, CtdMessage.TEMPERATURE));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getTemperature(), CtdMessage.TEMPERATURE_UNIT));
        observation.setResultTime(new TimeInstant(message.getTime()));
        return observation;
    }

    private OmObservation createConditionObservation(CtdMessage message) {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, CtdMessage.CONDITION));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getCondition(), CtdMessage.CONDITION_UNIT));
        observation.setResultTime(new TimeInstant(message.getTime()));
        return observation;
    }

    private OmObservation createSaltinessObservation(CtdMessage message) {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, CtdMessage.SALTINESS));
        observation.setValue(createQuantityObservationValue(message.getTime(), message.getSaltiness(), CtdMessage.SALTINESS_UNIT));
        observation.setResultTime(new TimeInstant(message.getTime()));
        return observation;
    }

    private OmObservation createSoundVObservation(CtdMessage message) {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, CtdMessage.SOUND_V));
        observation.setValue(createTextObservationValue(message.getTime(), message.getSoundV(), CtdMessage.SOUND_V_UNIT));
        observation.setResultTime(new TimeInstant(message.getTime()));
        return observation;
    }

    private OmObservationConstellation createObservationConstellation(CtdMessage message, String phenomenon) {
        OmObservationConstellation constellation = new OmObservationConstellation();
        constellation.setObservableProperty(createPhenomenon(phenomenon));
        constellation.setFeatureOfInterest(createFeatureOfInterest(message));
        constellation.setOfferings(createOffering(message));
        constellation.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        constellation.setProcedure(createProcedure(message));
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

    private ObservationValue<?> createTextObservationValue(DateTime time, String value, String unit) {
        SingleObservationValue<String> obsValue = new SingleObservationValue<>();
        TextValue quantityValue = new TextValue(value);
        quantityValue.setUnit(unit);
        obsValue.setValue(quantityValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    private AbstractFeature createFeatureOfInterest(CtdMessage message) {
        String identifier = message.getSensorId();
        SamplingFeature samplingFeature = new SamplingFeature(new CodeWithAuthority(identifier));
        samplingFeature.addName(identifier);
        return samplingFeature;
    }

    private SosProcedureDescription createProcedure(CtdMessage message) {
        return new PhysicalSystem().setIdentifier(message.getSensorId());
    }

    private Set<String> createOffering(CtdMessage message) {
        return Sets.newHashSet(message.getProcedure());
    }

    private AbstractPhenomenon createPhenomenon(String identifier) {
        return new OmObservableProperty(identifier);
    }

}
