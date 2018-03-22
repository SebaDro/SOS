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
package org.n52.sos.mqtt;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.locationtech.jts.io.ParseException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;

import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.n52.sos.mqtt.decode.MqttDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosMqttCallback implements MqttCallback {

    private static final Logger LOG = LoggerFactory.getLogger(SosMqttCallback.class);

    private MqttDecoder decoder;
    private MqttInsertSensorConverter insertSensorConverter;
    private MqttInsertObservationConverter insertObservationConverter;
    private ExecutorService executorService;
    private MqttMessageCollector messageCollector;
    private MqttInsertObservationRequestHandler requestHandler;

    public SosMqttCallback(MqttDecoder decoder, MqttMessageCollector collector, MqttInsertObservationRequestHandler requestHandler, ExecutorService executorService) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        this.decoder = decoder;
        this.messageCollector = collector;
        this.insertSensorConverter = decoder.getInsertSensorConverter();
        this.insertObservationConverter = decoder.getInsertObservationConverter();
        this.executorService = executorService;
        this.requestHandler = requestHandler;
    }

    @Override
    public void connectionLost(Throwable cause) {
        LOG.warn("Connection lost", cause.fillInStackTrace());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Set<org.n52.sos.mqtt.api.MqttMessage> messages = decoder.decode(new String(message.getPayload()));
        executorService.execute(new MessageHandler(messages));

    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOG.info("Delivery completed for message id '{}'", token.getMessageId());
    }

    private class MessageHandler implements Runnable {

        private Set<org.n52.sos.mqtt.api.MqttMessage> messages;

        public MessageHandler (Set<org.n52.sos.mqtt.api.MqttMessage> messages){
            this.messages = messages;
        }


               @Override
            public void run() {
                try {
                    for (org.n52.sos.mqtt.api.MqttMessage mqttMessage : messages) {
                        if (!requestHandler.isProcedureRegistered(mqttMessage.getProcedure())) {
                            InsertSensorRequest request;

                            request = insertSensorConverter.convert(mqttMessage);

                            requestHandler.getServiceOperator(request).receiveRequest(request);

                        }

                        messageCollector.addMessage(mqttMessage);
                        LOG.info("Add message to '{}' collector. Current collection size: {}",
                                mqttMessage.getClass().getSimpleName(),
                                messageCollector.getActualSize());
                        if (messageCollector.reachedLimit()) {
                            messageCollector.getMessages().forEach((k, v) -> {
                                try {
                                    InsertObservationRequest request = insertObservationConverter.convert(v);

                                    Long start = System.currentTimeMillis();
                                    requestHandler.getServiceOperator(request).receiveRequest(request);
                                    Long end = System.currentTimeMillis();
                                    LOG.info("InsertObservation request duration: {} ms", (end - start));
                                } catch (OwsExceptionReport ex) {
                                    LOG.error("Error while receiving InsertObservationRequest.", ex);
                                } catch (ParseException ex) {
                                    LOG.error("Error while creating InsertObservationRequest.", ex);
                                }
                            });
                            messageCollector.clearMessages();
                        }

                    }
                } catch (OwsExceptionReport ex) {
                    LOG.error("Error while processing messages!", ex);
                }
            }
    }

}
