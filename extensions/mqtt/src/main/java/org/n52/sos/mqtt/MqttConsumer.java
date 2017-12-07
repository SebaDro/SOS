/*
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

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;

import org.n52.faroe.SettingsService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.Debouncer;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.sos.mqtt.decode.MqttDecoder;
import org.n52.sos.mqtt.decode.MqttDecoderRepository;
import org.n52.svalbard.encode.EncoderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matthes Rieke <m.rieke@52north.org>
 */
@Configurable
public class MqttConsumer implements Constructable, Destroyable {

    private static final Logger LOG = LoggerFactory.getLogger(MqttConsumer.class);
    private static MqttConsumer instance;

    private String topic;
    private String host;
    private String port;
    private MqttClient client;
    private String decoder;
    private String protocol;
    private String username;
    private String password;
    private boolean isActive;

    private Debouncer debouncer;

    @Inject
    MqttDecoderRepository mqttDecoderRepository;

    @Inject
    SettingsService settingsManager;

    @Inject
    EncoderRepository encoderRepository;

    @Override
    public void init() {
        LOG.debug("MQTT client initialized");
        createDebouncer();
        try {
            connect();
            LOG.info("MQTT client connected");
        } catch (Exception ex) {
            LOG.error("Error during connecting MQTT client", ex);
        }
    }

    @Override
    public void destroy() {
        cleanup();
    }

    private void createDebouncer() {
        debouncer = new Debouncer(1000, new Runnable() {
            @Override
            public void run() {
                try {
                    cleanup();
                    connect();
                } catch (Exception ex) {
                    LOG.error("Error during connecting MQTT client", ex);
                }
            }
        });
    }

    /**
     * the MQTT QoS as enum. use #ordinal() to get the int
     */
    public enum QualityOfService {
        AT_MOST_ONCE, AT_LEAST_ONCE, EXACTLY_ONCE
    }

    public static MqttConsumer getInstance() {
        if (instance == null) {
            instance = new MqttConsumer();
//            SettingsManager.getInstance().configure(instance);
        }
        return instance;
    }

    /**
     * connects the client
     *
     * @throws MqttException
     */
    public void connect() throws MqttException {
        if (isActive) {
            if (client == null) {
                this.client = new MqttClient(String.format("tcp://%s:%s", getHost(), getPort()), getId(), new MemoryPersistence());
                LOG.debug("MQTT client created!");
            }
            if (!client.isConnected()) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setConnectionTimeout(0);
                if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                    options.setUserName(username);
                    options.setPassword(password.toCharArray());
                }
                client.connect(options);
                LOG.debug("Connected to: {}", String.format("tcp://%s:%s", getHost(), getPort()));
                try {
                    Optional<MqttDecoder> decoder = mqttDecoderRepository.getDecoder(getDecoder());
                    if (!decoder.isPresent()) {
                        throw new ClassNotFoundException("Decoder is not available");
                    }
                    settingsManager.configure(decoder.get());
                    client.setCallback(new SosMqttCallback(decoder.get()));
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    throw new ConfigurationError("Error while starting MQTT consumer", e);
                }
            }
            subscribe(getTopic(), QualityOfService.EXACTLY_ONCE);
            LOG.debug("Subscibed to topic: {}", getTopic());
        }
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

    private void cleanup() {
        try {
            if (client != null && client.isConnected() && getTopic() != null) {
                client.unsubscribe(getTopic());
                client.disconnect();
            }
        } catch (MqttException e) {
            LOG.error("Error while closing connection!", e);
        }
    }

    private void requestConnecting() {
        if (debouncer != null) {
            debouncer.call();
        }
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

    @Setting(MqttSettings.MQTT_TOPIC)
    public void setTopic(String topic) {
        this.topic = topic;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_HOST)
    public void setHost(String host) {
        this.host = host;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_PORT)
    public void setPort(String port) {
        this.port = port;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_DECODER)
    public void setDecoder(String decoder) {
        this.decoder = decoder;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_PROTOCOL)
    public void setProtocol(String protocol) {
        this.protocol = protocol;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_USERNAME)
    public void setUsername(String username) {
        this.username = username;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_PASSWORD)
    public void setPassword(String password) {
        this.password = password;
        requestConnecting();
    }

    @Setting(MqttSettings.MQTT_ACTIVE)
    public void setActive(boolean active) {
        this.isActive = active;
        if (isActive) {
            requestConnecting();
        } else {
            cleanup();
        }

    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    public String getDecoder() {
        return decoder;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

}
