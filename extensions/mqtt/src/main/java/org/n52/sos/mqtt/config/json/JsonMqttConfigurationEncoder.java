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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import org.n52.janmayen.Json;
import org.n52.sos.mqtt.config.MqttConfiguration;
import org.n52.sos.mqtt.config.MqttConstants;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class JsonMqttConfigurationEncoder {

    public JsonNode encode(MqttConfiguration config) {
        ObjectNode configNode = Json.nodeFactory().objectNode();
        configNode.set(MqttConstants.MQTT_KEY, createTextNode(config.getKey()));
        configNode.set(MqttConstants.MQTT_NAME, createTextNode(config.getName()));
        configNode.set(MqttConstants.MQTT_ACTIVE, createBooleanNode(config.isActive()));
        configNode.set(MqttConstants.MQTT_HOST, createTextNode(config.getHost()));
        configNode.set(MqttConstants.MQTT_PORT, createTextNode(config.getPort()));
        configNode.set(MqttConstants.MQTT_TOPIC, createTextNode(config.getTopic()));
        configNode.set(MqttConstants.MQTT_PROTOCOL, createTextNode(config.getProtocol()));
        configNode.set(MqttConstants.MQTT_DECODER, createTextNode(config.getDecoder()));

        configNode.set(MqttConstants.MQTT_USERNAME, createTextNode(config.getUsername()));
        configNode.set(MqttConstants.MQTT_PASSWORD, createTextNode(config.getPassword()));
        configNode.set(MqttConstants.MQTT_OM_OBSERVATION_FIELD, createTextNode(config.getObservationField()));
        configNode.set(MqttConstants.MQTT_CSV_LINE_SEPERATOR, createTextNode(config.getCsvLineSeperator()));
        configNode.set(MqttConstants.MQTT_CSV_FIELD_SEPERATOR, createTextNode(config.getCsvFieldSeperator()));

        configNode.set(MqttConstants.MQTT_OM_OBSERVABLE_PROPERTIES, createArrayNode(config.getObservableProperties()));
        return configNode;
    }

    private JsonNode createTextNode(String value) {
        if (value == null) {
            return Json.nodeFactory().textNode("");
        } else {
            return Json.nodeFactory().textNode(value);
        }
    }

    private JsonNode createBooleanNode(boolean value) {
        return Json.nodeFactory().booleanNode(value);
    }

    private JsonNode createArrayNode(Set<String> value) {
        Set<String> observableProperties = Sets.newHashSet();
        if (value != null) {
            observableProperties.addAll(value);
        }
        return Json.nodeFactory()
                .arrayNode()
                .addAll(observableProperties
                        .stream()
                        .map(o -> Json.nodeFactory().textNode(o))
                        .collect(Collectors.toSet()));
    }
}
