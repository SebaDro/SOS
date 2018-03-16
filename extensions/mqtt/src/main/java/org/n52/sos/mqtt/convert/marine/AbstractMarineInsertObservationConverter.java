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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.n52.janmayen.net.IPAddress;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceRequestContext;
import org.n52.shetland.ogc.sensorML.v20.PhysicalSystem;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 * @param <T>
 */
public abstract class AbstractMarineInsertObservationConverter<T> implements MqttInsertObservationConverter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMarineInsertObservationConverter.class);

    @Override
    public InsertObservationRequest convert(T message) throws OwsExceptionReport {
        InsertObservationRequest request = createBaseInsertObservationRequest(message);
        request.setObservation(createObservations(message));
        return request;
    }

    @Override
    public InsertObservationRequest convert(List<T> messages) {
        List<OmObservation> observations = new ArrayList();
        InsertObservationRequest request = createBaseInsertObservationRequest(messages.get(0));
        messages.forEach(m -> {
            try {
                observations.addAll(createObservations(m));
            } catch (OwsExceptionReport ex) {
                LOG.error("Error while decoding message", ex);
            }
        });
        request.setObservation(observations);
        return request;
    }

    protected InsertObservationRequest createBaseInsertObservationRequest(T message) {
        InsertObservationRequest request = new InsertObservationRequest();
        request.setService(SosConstants.SOS);
        request.setVersion(Sos2Constants.SERVICEVERSION);
        OwsServiceRequestContext requestContext = new OwsServiceRequestContext();
        requestContext.setIPAddress(new IPAddress("127.0.0.1"));
        request.setRequestContext(requestContext);
        request.setOfferings(createOfferings(message));
        return request;
    }

    protected OmObservation createQuantityObservation(T message, String procedure, String phenomenon, double value, String unit, DateTime phenomenonTime, DateTime resultTime) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, procedure, phenomenon, OmConstants.OBS_TYPE_MEASUREMENT, unit));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createQuantityObservationValue(phenomenonTime, value, unit));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    protected OmObservation createCountObservation(T message, String procedure, String phenomenon, int value, String unit, DateTime phenomenonTime, DateTime resultTime) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, procedure, phenomenon, OmConstants.OBS_TYPE_COUNT_OBSERVATION, unit));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createCountObservationValue(phenomenonTime, value, unit));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    protected OmObservation createCountObservation(T message, String procedure, String phenomenon, int value, DateTime phenomenonTime, DateTime resultTime) throws OwsExceptionReport {
        OmObservation observation = new OmObservation();
        observation.setObservationConstellation(createObservationConstellation(message, procedure, phenomenon, OmConstants.OBS_TYPE_COUNT_OBSERVATION, null));
        observation.addParameter(createSpatialFilteringProfileParameter(message));
        observation.setValue(createCountObservationValue(phenomenonTime, value));
        observation.setResultTime(new TimeInstant(resultTime));
        return observation;
    }

    protected OmObservationConstellation createObservationConstellation(T message, String procedure, String phenomenon, String observationType, String unit) throws OwsExceptionReport {
        OmObservationConstellation constellation = new OmObservationConstellation();
        constellation.setObservableProperty(createPhenomenon(phenomenon, unit));
        constellation.setFeatureOfInterest(createFeatureOfInterest(message));
        constellation.setOfferings(createOfferings(message));
        constellation.setObservationType(observationType);
        constellation.setProcedure(createProcedure(procedure));
        return constellation;
    }

    protected ObservationValue<?> createQuantityObservationValue(DateTime time, double value, String unit) {
        SingleObservationValue obsValue = new SingleObservationValue<>();
        QuantityValue quantityValue = new QuantityValue(value);
        quantityValue.setUnit(unit);
        obsValue.setValue(quantityValue);
        obsValue.setUnit(unit);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    protected ObservationValue<?> createCountObservationValue(DateTime time, int value, String unit) {
        SingleObservationValue<Integer> obsValue = new SingleObservationValue<>();
        CountValue countValue = new CountValue(value);
        countValue.setUnit(unit);
        obsValue.setValue(countValue);
        obsValue.setUnit(unit);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    protected ObservationValue<?> createCountObservationValue(DateTime time, int value) {
        SingleObservationValue<Integer> obsValue = new SingleObservationValue<>();
        CountValue countValue = new CountValue(value);
        obsValue.setValue(countValue);
        obsValue.setPhenomenonTime(new TimeInstant(time));
        return obsValue;
    }

    protected SosProcedureDescription createProcedure(String procedure) {
        return new SosProcedureDescription(new PhysicalSystem().setIdentifier(procedure));
    }

    protected AbstractPhenomenon createPhenomenon(String identifier, String unit) {
        OmObservableProperty property = new OmObservableProperty(identifier);
        property.setUnit(unit);
        return property;
    }

    protected abstract List<OmObservation> createObservations(T message) throws OwsExceptionReport;

    protected abstract AbstractFeature createFeatureOfInterest(T message) throws OwsExceptionReport;

    protected abstract List<String> createOfferings(T message);

    protected abstract NamedValue<?> createSpatialFilteringProfileParameter(T message) throws OwsExceptionReport;

}
