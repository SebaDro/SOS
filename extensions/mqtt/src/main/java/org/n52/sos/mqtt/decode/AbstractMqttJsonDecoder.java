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
package org.n52.sos.mqtt.decode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.n52.sos.mqtt.api.MqttMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import org.n52.janmayen.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public abstract class AbstractMqttJsonDecoder implements MqttDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMqttCsvDecoder.class);

    protected abstract MqttMessage parseMessage(JsonNode n);

    @Override
    public Set<MqttMessage> decode(String payload) {
        Set<MqttMessage> messages = Sets.newHashSet();
        try {
            messages.addAll(decodeJson(Json.loadString(payload)));
        } catch (Throwable cause) {
            LOG.error("Error while parsing message.", cause);
        }
        return messages;
    }

    protected Set<MqttMessage> decodeJson(JsonNode json) {
        Set<MqttMessage> messages = Sets.newHashSet();
        if (json == null || json.isNull() || json.isMissingNode()) {
            return messages;
        }
        if (json.isArray()) {
            for (JsonNode n : json) {
                if (n.isObject()) {
                    messages.add(parseMessage(n));
                }
            }
        } else if (json.isObject()) {
            messages.add(parseMessage(json));
        }

        return messages;
    }

    protected String getString(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return "";
        }
        return jsonNode.asText();
    }

    protected double getDouble(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return Double.NaN;
        }
        return jsonNode.asDouble();
    }

    protected int getInteger(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return Integer.MIN_VALUE;
        }
        return jsonNode.asInt();
    }

    protected DateTime getDateTime(JsonNode json, String name) {
        return new DateTime(getLong(json, name) * 1000);
    }

    protected long getLong(JsonNode json, String name) {
        return json.get(name).asLong();
    }

    protected JsonNode getJsonNode(JsonNode json, String name) {
        return json.get(name);
    }

    protected Map<String, Object> getMap(JsonNode json, String name) {
        Map<String, Object> map = new HashMap<>();
        JsonNode jsonNode = json.get(name);
        if (jsonNode == null || jsonNode.isNull() || jsonNode.isMissingNode()) {
            return map;
        }
        if (jsonNode.isObject()) {
            while (jsonNode.fields().hasNext()) {
                Entry<String, JsonNode> type = jsonNode.fields().next();
                map.put(type.getKey(), parse(jsonNode));

            }
        }
        return map;
    }

    private Object parse(JsonNode jsonNode) {
        return jsonNode.getClass();
    }

}
