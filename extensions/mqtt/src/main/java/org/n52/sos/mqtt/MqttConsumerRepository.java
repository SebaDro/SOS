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

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.sos.mqtt.config.MqttConfiguration;
import org.n52.sos.mqtt.config.MqttConfigurationDao;
import org.n52.sos.mqtt.decode.MqttDecoder;
import org.n52.sos.mqtt.decode.MqttDecoderFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MqttConsumerRepository implements Constructable, Destroyable {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MqttConsumerRepository.class);

    @Inject
    private MqttConfigurationDao mqttConfigurationDao;

    @Inject
    private MqttDecoderFactory decoderFactory;

    @Inject
    private MqttInsertObservationRequestHandler requestHandler;

    private Map<String, MqttConsumer> mqttConsumers;

    @Override
    public void init() {
        mqttConsumers = new HashMap();
        mqttConfigurationDao.getAllMqttConfigurations()
                .forEach(c -> {
                    if (!mqttConsumers.containsKey(c.getKey())) {
                        MqttConsumer consumer = create(c);
                        if (c.isActive()) {
                            try {
                                consumer.connect();
                            } catch (MqttException ex) {
                                LOG.error("Error during connecting MQTT client for configuration " + c.getKey(), ex);
                            } finally {
                                c.setIsActive(consumer.isConnected());
                                mqttConfigurationDao.updateMqttConfiguration(c);
                            }
                        }
                    }
                });
    }

    public Map<String, MqttConsumer> getAll() {
        return this.mqttConsumers;
    }

    public MqttConsumer get(String key) {
        return mqttConsumers.get(key);
    }

    public MqttConsumer create(MqttConfiguration config) {
        MqttConsumer consumer = new MqttConsumer(config);
        MqttDecoder decoder = decoderFactory.createMqttDecoder(config);
        consumer.setDecoder(decoder);
        consumer.setCollector(new MqttMessageCollector(decoder.getInsertObservationConverter().getMessageLimit()));
        consumer.setRequestHandler(requestHandler);
        mqttConsumers.put(consumer.getConfig().getKey(), consumer);
        return consumer;
    }

    public void update(MqttConfiguration config) {
        MqttConsumer consumer = mqttConsumers.get(config.getKey());
        consumer.updateConfiguration(config);
        consumer.setDecoder(decoderFactory.createMqttDecoder(config));
    }

    public void deleteAll() {
        mqttConsumers.clear();
    }

    public void delete(String key) {
        mqttConsumers.remove(key);
    }

    private MqttConsumer createMqttConsumer(MqttConfiguration config) {
        MqttConsumer consumer = new MqttConsumer(config);
        return consumer;
    }

    @Override
    public void destroy() {
        mqttConsumers.forEach((k, m) -> {
            try {
                m.cleanup();
            } catch (MqttException ex) {
                LOG.error("Error while closing MQTT connection for configuration" + m.getConfig().getKey(), ex);
            } finally {
                MqttConfiguration config = mqttConfigurationDao.getMqttConfigurationById(k).get();
                config.setIsActive(m.isConnected());
                mqttConfigurationDao.updateMqttConfiguration(config);
            }
        });
    }

}
