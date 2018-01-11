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
package org.n52.sos.mqtt.config.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import org.n52.faroe.json.AbstractJsonDao;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.sos.mqtt.config.MqttConfiguration;
import org.n52.sos.mqtt.config.MqttConfigurationDao;
import org.n52.sos.mqtt.config.MqttConstants;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class JsonMqttConfigurationDao extends AbstractJsonDao implements MqttConfigurationDao, Constructable {

    private Set<MqttConfiguration> configurations;

    @Inject
    JsonMqttConfigurationDecoder jsonConfigDecoder;

    @Inject
    JsonMqttConfigurationEncoder jsonConfigEncoder;

    @Override
    public void init() {
        if (getConfiguration().get(MqttConstants.MQTT_CONFIG) == null) {
            configuration().writeLock().lock();
            try {
//                getConfiguration().putArray(MqttConstants.MQTT_CONFIG);
                getConfiguration().putObject(MqttConstants.MQTT_CONFIG);
                configuration().writeNow();
            } finally {
                configuration().writeLock().unlock();
            }
        }
        createTestConfigs();
    }

    @Override
    public MqttConfiguration createMqttConfiguration(String name) {
        JsonMqttConfiguration config = new JsonMqttConfiguration();
        configuration().writeLock().lock();
        try {
            config.setKey(UUID.randomUUID().toString());
            config.setName(name);
            saveMqttConfiguration(config);
        } finally {
            configuration().writeLock().unlock();
        }
        configuration().scheduleWrite();
        return config;
    }

    @Override
    public void saveMqttConfiguration(MqttConfiguration config) {
        configuration().writeLock().lock();
        try {
            ObjectNode jsonConfig = getConfiguration();
            ObjectNode jsonMqttConfig = jsonConfig.with(MqttConstants.MQTT_CONFIG);
            jsonMqttConfig.set(config.getKey(), jsonConfigEncoder.encode(config));
            configuration().writeNow();
        } finally {
            configuration().writeLock().unlock();
        }
    }

    @Override
    public void updateMqttConfiguration(MqttConfiguration config) {
        configuration().writeLock().lock();
        try {
            ObjectNode jsonConfig = getConfiguration();
            ObjectNode jsonMqttConfig = jsonConfig.with(MqttConstants.MQTT_CONFIG);
            jsonMqttConfig.putObject(config.getKey()).setAll((ObjectNode) jsonConfigEncoder.encode(config));
            configuration().writeNow();
        } finally {
            configuration().writeLock().unlock();
        }
    }

    @Override
    public void deleteMqttConfiguration(MqttConfiguration config) {
        configuration().writeLock().lock();
        try {
            getConfiguration().with(MqttConstants.MQTT_CONFIG).remove(config.getKey());
            configuration().scheduleWrite();
        } finally {
            configuration().writeLock().unlock();
        }
    }

    @Override
    public Optional<MqttConfiguration> getMqttConfigurationById(String id) {
        configuration().readLock().lock();
        try {
            return Optional.of(getConfiguration().path(MqttConstants.MQTT_CONFIG).path(id))
                    .map(n -> jsonConfigDecoder.decode(n));
        } finally {
            configuration().readLock().unlock();
        }
    }

    @Override
    public Optional<MqttConfiguration> getMqttConfigurationByName(String name) {
        configuration().readLock().lock();
        try {
            return StreamSupport.stream(getConfiguration().with(MqttConstants.MQTT_CONFIG).spliterator(), true)
                    .filter(n -> n.get(MqttConstants.MQTT_NAME).asText().equals(name))
                    .findFirst()
                    .map(n -> jsonConfigDecoder.decode(n));
        } finally {
            configuration().readLock().unlock();
        }
    }

    @Override
    public Set<MqttConfiguration> getAllMqttConfigurations() {
        configuration().readLock().lock();
        try {
            return StreamSupport.stream(getConfiguration().with(MqttConstants.MQTT_CONFIG).spliterator(), false)
                    .map(n -> jsonConfigDecoder.decode(n))
                    .collect(Collectors.toSet());
        } finally {
            configuration().readLock().unlock();
        }
    }

    @Override
    public void deleteAll() {
        this.configuration().delete();
    }

    @Override
    public boolean hasMqttConfigurationForName(String name) {
        return StreamSupport.stream(getConfiguration().path(MqttConstants.MQTT_CONFIG).spliterator(), false)
                .anyMatch(n -> n.get(MqttConstants.MQTT_NAME).asText().equals(name));
    }

    private void createTestConfigs() {
        MqttConfiguration config1 = new JsonMqttConfiguration();
        config1.setKey("test1");
        config1.setName("config1");
        MqttConfiguration config2 = new JsonMqttConfiguration();
        config2.setKey("test2");
        config2.setName("config2");
        configurations = Sets.newHashSet();
        configurations.add(config1);
        configurations.add(config2);
    }

}
