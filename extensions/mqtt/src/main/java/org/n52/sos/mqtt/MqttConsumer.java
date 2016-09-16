/**
 * Copyright (C) 2012-2016 52°North Initiative for Geospatial Open Source
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
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.n52.sos.config.annotation.Configurable;
import org.n52.sos.service.SosContextListener;
import org.n52.sos.util.Cleanupable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Matthes Rieke <m.rieke@52north.org>
 */
@Configurable
public class MqttConsumer implements Cleanupable {

    private static final Logger LOG = LoggerFactory.getLogger(MqttConsumer.class);

    /*
     * Procedure: hex FeatureOfInterest: flight / hex Phenomenon: track, speed,
     * altitude Offering: hex SamplingGeometry: lat, lon timestamp:
     * 
     * 
     * Local caching + expire time (last update)
     */

    private MqttClient client;

    /**
     * the MQTT QoS as enum. use #ordinal() to get the int
     */
    public enum QualityOfService {
        AT_MOST_ONCE, AT_LEAST_ONCE, EXACTLY_ONCE
    }

    public MqttConsumer() {
        SosContextListener.registerShutdownHook(this);
        getMqttSetting();
    }
    
    /**
     * connects the client
     * 
     * @throws MqttException
     */
    public void connect() throws MqttException {
        this.client = new MqttClient(String.format("tcp://%s:%s", getMqttSetting().getHost(), getMqttSetting().getPort()), getId(), new MemoryPersistence());
        client.connect();
        client.setCallback(new SosMqttCallback());
        subscribe(getMqttSetting().getTopic(), QualityOfService.EXACTLY_ONCE);
    }

    /**
     * subscribe for a topic
     * 
     * @param topic
     *            the topic to subscribe to
     * @param qos
     *            the QoS level
     * @throws MqttException
     *             if something goes wrong
     */
    private void subscribe(String topic, QualityOfService qos) throws MqttException {
        client.subscribe(topic, qos.ordinal());
    }

    @Override
    public void cleanup() {
        try {
            if (client != null) {
                client.unsubscribe(getMqttSetting().getTopic());
                client.disconnect();
            }
        } catch (MqttException e) {
            LOG.error("Error while closing connection!", e);
        }
    }
    
   
    
    private String getId() {
       return UUID.randomUUID().toString();
    }
    
    private MqttSettings getMqttSetting() {
        return MqttSettings.getInstance();
    }
    
//    public static void main(String[] args) throws MqttException {
//        String host = "ows.dev.52north.org";
//        String topic = "n52.adsb";
//
//        MqttConsumer c = new MqttConsumer();
//        c.connect();
//        c.subscribe(topic, QualityOfService.EXACTLY_ONCE);
//
//        while (true) {
//
//        }
//    }

}
