/**
 * Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.n52.sos.cache.ContentCache;
import org.n52.sos.config.SettingsManager;
import org.n52.sos.exception.ows.concrete.InvalidAcceptVersionsParameterException;
import org.n52.sos.exception.ows.concrete.InvalidServiceOrVersionException;
import org.n52.sos.exception.ows.concrete.InvalidServiceParameterException;
import org.n52.sos.exception.ows.concrete.MissingServiceParameterException;
import org.n52.sos.exception.ows.concrete.MissingVersionParameterException;
import org.n52.sos.exception.ows.concrete.VersionNotSupportedException;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.n52.sos.mqtt.decode.MqttDecoder;
import org.n52.sos.ogc.ows.CompositeOwsException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.request.GetCapabilitiesRequest;
import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.request.InsertSensorRequest;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.operator.ServiceOperator;
import org.n52.sos.service.operator.ServiceOperatorKey;
import org.n52.sos.service.operator.ServiceOperatorRepository;
import org.n52.sos.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosMqttCallback implements MqttCallback {

    private static final Logger LOG = LoggerFactory.getLogger(SosMqttCallback.class);
    private MqttDecoder decoder;
    private MqttInsertSensorConverter insertSensorConverter;
    private MqttInsertObservationConverter insertObservationConverter;

    public SosMqttCallback(String decoderName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        decoder = (MqttDecoder) Class.forName(decoderName).newInstance();
        SettingsManager.getInstance().configure(decoder);
        insertSensorConverter = decoder.getInsertSensorConverter();
        insertObservationConverter = decoder.getInsertOnbservationConverter();
    }

    @Override
    public void connectionLost(Throwable cause) {
        LOG.warn("Connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            for (org.n52.sos.mqtt.api.MqttMessage mqttMessage : decoder.decoder(JSONUtils.loadString(new String(message.getPayload())))) {
                if (!isProcedureRegistered(mqttMessage.getProcedure())) {
                    InsertSensorRequest request;

                    request = insertSensorConverter.convert(mqttMessage);

                    getServiceOperator(request).receiveRequest(request);
                }
                InsertObservationRequest request = insertObservationConverter.convert(mqttMessage);
                getServiceOperator(request).receiveRequest(request);
            }
        } catch (OwsExceptionReport e) {
            LOG.error("Error while processing messages!", e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOG.info("Delivery completed for message id '{}'", token.getMessageId());
    }

    private ContentCache getCache() {
        return Configurator.getInstance().getCache();
    }

    private boolean isProcedureRegistered(String procedure) {
        return getCache().getProcedures().contains(procedure);
    }

    protected ServiceOperator getServiceOperator(ServiceOperatorKey sokt) throws OwsExceptionReport {
        return getServiceOperatorRepository().getServiceOperator(sokt);
    }

    protected ServiceOperator getServiceOperator(AbstractServiceRequest<?> request) throws OwsExceptionReport {
        checkServiceOperatorKeyTypes(request);
        for (ServiceOperatorKey sokt : request.getServiceOperatorKeyType()) {
            ServiceOperator so = getServiceOperator(sokt);
            if (so != null) {
                return so;
            }
        }
        throw new InvalidServiceOrVersionException(request.getService(), request.getVersion());
    }

    protected void checkServiceOperatorKeyTypes(AbstractServiceRequest<?> request) throws OwsExceptionReport {
        CompositeOwsException exceptions = new CompositeOwsException();
        for (ServiceOperatorKey sokt : request.getServiceOperatorKeyType()) {
            if (sokt.hasService()) {
                if (sokt.getService().isEmpty()) {
                    exceptions.add(new MissingServiceParameterException());
                } else if (!getServiceOperatorRepository().isServiceSupported(sokt.getService())) {
                    exceptions.add(new InvalidServiceParameterException(sokt.getService()));
                }
            }
            if (request instanceof GetCapabilitiesRequest) {
                GetCapabilitiesRequest gcr = (GetCapabilitiesRequest) request;
                if (gcr.isSetAcceptVersions()) {
                    boolean hasSupportedVersion = false;
                    for (String acceptVersion : gcr.getAcceptVersions()) {
                        if (isVersionSupported(request.getService(), acceptVersion)) {
                            hasSupportedVersion = true;
                        }
                    }
                    if (!hasSupportedVersion) {
                        exceptions.add(new InvalidAcceptVersionsParameterException(gcr.getAcceptVersions()));
                    }
                }
            } else if (sokt.hasVersion()) {
                if (sokt.getVersion().isEmpty()) {
                    exceptions.add(new MissingVersionParameterException());
                } else if (!isVersionSupported(sokt.getService(), sokt.getVersion())) {
                    exceptions.add(new VersionNotSupportedException());
                }
            }
        }
        exceptions.throwIfNotEmpty();
    }

    protected boolean isVersionSupported(String service, String acceptVersion) {
        return getServiceOperatorRepository().isVersionSupported(service, acceptVersion);
    }

    protected boolean isServiceSupported(String service) {
        return getServiceOperatorRepository().isServiceSupported(service);
    }

    protected ServiceOperatorRepository getServiceOperatorRepository() {
        return ServiceOperatorRepository.getInstance();
    }
}
