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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Sets;
import java.util.Set;
import org.n52.sos.mqtt.config.MqttConstants;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class JsonMqttConfigurationDecoder {

    public JsonMqttConfiguration decode(JsonNode json) {
        JsonMqttConfiguration config = new JsonMqttConfiguration();
        config.setKey(getString(json, MqttConstants.MQTT_Key));
        config.setIsActive(getBoolean(json, MqttConstants.MQTT_ACTIVE));
        config.setHost(getString(json, MqttConstants.MQTT_HOST));
        config.setPort(getString(json, MqttConstants.MQTT_PORT));
        config.setTopic(getString(json, MqttConstants.MQTT_TOPIC));
        config.setProtocol(getString(json, MqttConstants.MQTT_PROTOCOL));
        config.setDecoder(getString(json, MqttConstants.MQTT_DECODER));

        config.setUsername(getString(json, MqttConstants.MQTT_USERNAME));
        config.setPassword(getString(json, MqttConstants.MQTT_PASSWORD));
        config.setObservationField(getString(json, MqttConstants.MQTT_OM_OBSERVATION_FIELD));
        config.setCsvLineSeperator(getString(json, MqttConstants.MQTT_CSV_LINE_SEPERATOR));
        config.setCsvFieldSeperator(getString(json, MqttConstants.MQTT_CSV_FIELD_SEPERATOR));

        config.setObservableProperties(getSet(json, MqttConstants.MQTT_OM_OBSERVABLE_PROPERTIES));

        return config;
    }

    private String getString(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return "";
        }
        return jsonNode.asText();
    }

    private boolean getBoolean(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return false;
        }
        return jsonNode.asBoolean();
    }

    private Set getSet(JsonNode json, String name) {
        Set set = Sets.newHashSet();
        ((ArrayNode) json.get(name))
                .forEach(n -> set.add(n.asText()));
        return set;
    }
}
