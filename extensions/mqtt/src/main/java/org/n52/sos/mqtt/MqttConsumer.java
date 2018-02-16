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

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.janmayen.Debouncer;
import org.n52.sos.mqtt.config.MqttConfiguration;
import org.n52.sos.mqtt.decode.MqttDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matthes Rieke <m.rieke@52north.org>
 */
public class MqttConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(MqttConsumer.class);

    private MqttClient client;
    private MqttConfiguration config;
    private MqttDecoder decoder;
    private Debouncer debouncer;

    public MqttConsumer(MqttConfiguration config) {
        this.config = config;
    }

    /**
     * the MQTT QoS as enum. use #ordinal() to get the int
     */
    public enum QualityOfService {
        AT_MOST_ONCE, AT_LEAST_ONCE, EXACTLY_ONCE
    }

    /**
     * connects the client
     *
     * @throws MqttException
     */
    public void connect() throws MqttException {
        if (client == null) {
            this.client = new MqttClient(String.format("tcp://%s:%s", config.getHost(), config.getPort()), getId(), new MemoryPersistence());
            LOG.debug("MQTT client created!");
        }
        if (!client.isConnected()) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(0);
            if (config.getUsername() != null && !config.getUsername().isEmpty() && config.getPassword() != null && !config.getPassword().isEmpty()) {
                options.setUserName(config.getPassword());
                options.setPassword(config.getPassword().toCharArray());
            }
            client.connect(options);
            LOG.debug("Connected to: {}", String.format("tcp://%s:%s", config.getHost(), config.getPort()));
            try {
                client.setCallback(new SosMqttCallback(decoder));
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new ConfigurationError("Error while starting MQTT consumer", e);
            }
        }
        subscribe(config.getTopic(), QualityOfService.AT_MOST_ONCE);
        LOG.debug("Subscibed to topic: {}", config.getTopic());
    }

    public boolean isConnected() {
        if (client != null && client.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * subscribe for a topic
     *
     * @param topic the topic to subscribe to
     * @param qos the QoS level
     * @throws MqttException if something goes wrong
     */
    private void subscribe(String topic, QualityOfService qos) throws MqttException {
        client.subscribe(topic, qos.ordinal());
    }

    public void cleanup() throws MqttException {
        if (client != null && client.isConnected() && config.getTopic() != null) {
            client.unsubscribe(config.getTopic());
            client.disconnect();
        }
    }

    private void requestConnecting() {
        if (debouncer != null) {
            debouncer.call();
        }
    }

    public MqttConfiguration getConfig() {
        return this.config;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

    public MqttDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(MqttDecoder decoder) {
        this.decoder = decoder;
    }
}
