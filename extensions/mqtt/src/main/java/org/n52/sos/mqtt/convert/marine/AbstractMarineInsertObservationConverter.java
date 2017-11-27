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

import java.util.List;

import org.joda.time.DateTime;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.om.AbstractPhenomenon;
import org.n52.sos.ogc.om.NamedValue;
import org.n52.sos.ogc.om.ObservationValue;
import org.n52.sos.ogc.om.OmConstants;
import org.n52.sos.ogc.om.OmObservableProperty;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.om.OmObservationConstellation;
import org.n52.sos.ogc.om.SingleObservationValue;
import org.n52.sos.ogc.om.values.CountValue;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.v20.PhysicalSystem;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.request.RequestContext;
import org.n52.sos.util.net.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastian
 */
public abstract class AbstractMarineInsertObservationConverter<T> implements MqttInsertObservationConverter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMarineInsertObservationConverter.class);

    @Override
    public InsertObservationRequest convert(T message) throws OwsExceptionReport {
        InsertObservationRequest request = new InsertObservationRequest();
        request.setService(SosConstants.SOS);
        request.setVersion(Sos2Constants.SERVICEVERSION);
        RequestContext requestContext = new RequestContext();
        requestContext.setIPAddress(new IPAddress("127.0.0.1"));
        request.setRequestContext(requestContext);
        request.setOfferings(createOfferings(message));
        request.setObservation(createObservations(message));
        return request;
    }

    protected OmObservation createQuantityObservation(T message, String procedure, String phenomenon, double value, String unit, DateTime phenomenonTime, DateTime resultTime) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, procedure, phenomenon, OmConstants.OBS_TYPE_MEASUREMENT));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createQuantityObservationValue(phenomenonTime, value, unit));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    protected OmObservation createCountObservation(T message, String procedure, String phenomenon, int value, String unit, DateTime phenomenonTime, DateTime resultTime) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, procedure, phenomenon, OmConstants.OBS_TYPE_COUNT_OBSERVATION));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createCountObservationValue(phenomenonTime, value, unit));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    protected OmObservation createCountObservation(T message, String procedure, String phenomenon, int value, DateTime phenomenonTime, DateTime resultTime) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, procedure, phenomenon, OmConstants.OBS_TYPE_COUNT_OBSERVATION));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createCountObservationValue(phenomenonTime, value));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    protected OmObservationConstellation createObservationConstellation(T message, String procedure, String phenomenon, String observationType) throws OwsExceptionReport {
        OmObservationConstellation constellation = new OmObservationConstellation();
        constellation.setObservableProperty(createPhenomenon(phenomenon));
        constellation.setFeatureOfInterest(createFeatureOfInterest(message));
        constellation.setOfferings(createOfferings(message));
        constellation.setObservationType(observationType);
        constellation.setProcedure(createProcedure(procedure));
        return constellation;
    }

    protected ObservationValue<?> createQuantityObservationValue(DateTime time, double value, String unit) {
        SingleObservationValue<Double> obsValue = new SingleObservationValue<>();
        QuantityValue quantityValue = new QuantityValue(value);
        quantityValue.setUnit(unit);
        obsValue.setValue(quantityValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    private ObservationValue<?> createCountObservationValue(DateTime time, int value, String unit) {
        SingleObservationValue<Integer> obsValue = new SingleObservationValue<>();
        CountValue countValue = new CountValue(value);
        countValue.setUnit(unit);
        obsValue.setValue(countValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    private ObservationValue<?> createCountObservationValue(DateTime time, int value) {
        SingleObservationValue<Integer> obsValue = new SingleObservationValue<>();
        CountValue countValue = new CountValue(value);
        obsValue.setValue(countValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    protected SosProcedureDescription createProcedure(String procedure) {
        return new PhysicalSystem().setIdentifier(procedure);
    }

    protected AbstractPhenomenon createPhenomenon(String identifier) {
        return new OmObservableProperty(identifier);
    }

    protected abstract List<OmObservation> createObservations(T message) throws OwsExceptionReport;

    protected abstract AbstractFeature createFeatureOfInterest(T message) throws OwsExceptionReport;

    protected abstract List<String> createOfferings(T message);

    protected abstract NamedValue<?> createSpatialFilteringProfileParameter(T message) throws OwsExceptionReport;

}
