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
package org.n52.sos.mqtt.config.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
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

    @Inject
    JsonMqttConfigurationDecoder jsonConfigDecoder;

    @Inject
    JsonMqttConfigurationEncoder jsonConfigEncoder;

    @Override
    public void init() {
        if (getConfiguration().path(MqttConstants.MQTT_CONFIG) == null);
        {
            configuration().writeLock().lock();
            try {
                getConfiguration().putArray(MqttConstants.MQTT_CONFIG);
                configuration().writeNow();
            } finally {
                configuration().writeLock().unlock();
            }
        }

    }

    @Override
    public MqttConfiguration createMqttConfiguration() {
        JsonMqttConfiguration config = new JsonMqttConfiguration();
        configuration().writeLock().lock();
        try {
            config.setKey(UUID.randomUUID().toString());
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
            getConfiguration().with(MqttConstants.MQTT_CONFIG).set(config.getKey(), jsonConfigEncoder.encode(config));
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
    public MqttConfiguration getMqttConfiguration(String id) {
        configuration().readLock().lock();
        try {
            return jsonConfigDecoder.decode(getConfiguration().path(MqttConstants.MQTT_CONFIG).path(id));
        } finally {
            configuration().readLock().unlock();
        }
    }

    @Override
    public Set<MqttConfiguration> getAllMqttConfigurations() {
        configuration().readLock().lock();
        try {
            return StreamSupport.stream(((ArrayNode) getConfiguration().path(MqttConstants.MQTT_CONFIG)).spliterator(), false)
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

}
