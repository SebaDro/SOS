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
package org.n52.sos.mqtt.config;

import org.n52.sos.mqtt.config.json.JsonMqttConfiguration;
import org.n52.sos.mqtt.config.json.JsonMqttConfigurationDecoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.UUID;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.janmayen.Json;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class JsonMqttConfigurationDecoderTest {

    private static final String KEY = UUID.randomUUID().toString();
    private static final boolean IS_ACTIVE = true;
    private static final String HOST = "ows.dev.52north.org";
    private static final String PORT = "1883";
    private static final String PROTOCOL = "tcp";
    private static final String TOPIC = "n52.adsb";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final String DECODER = "org.n52.sos.mqtt.decode.AdsbDecoder";
    private static final String OBSERVATION_FIELD = "bird count";
    private static final String CSV_LINE_SEPERATOR = "\\n";
    private static final String CSV_FIELD_SEPERATOR = ";";
    private static final String BIRD_COUNT_PROPERTY = "bird count";
    private static final String TEMPERATURE_PROPERTY = "temperature";

    private static JsonMqttConfigurationDecoder decoder;
    private static Set<String> observableProperties;
    private static ObjectNode configNode;

    @BeforeClass
    public static void init() {
        decoder = new JsonMqttConfigurationDecoder();

        configNode = Json.nodeFactory().objectNode()
                .put(MqttConstants.MQTT_KEY, KEY)
                .put(MqttConstants.MQTT_ACTIVE, IS_ACTIVE)
                .put(MqttConstants.MQTT_HOST, HOST)
                .put(MqttConstants.MQTT_PORT, PORT)
                .put(MqttConstants.MQTT_TOPIC, TOPIC)
                .put(MqttConstants.MQTT_PROTOCOL, PROTOCOL)
                .put(MqttConstants.MQTT_DECODER, DECODER)
                .put(MqttConstants.MQTT_USERNAME, USERNAME)
                .put(MqttConstants.MQTT_PASSWORD, PASSWORD)
                .put(MqttConstants.MQTT_OM_OBSERVATION_FIELD, OBSERVATION_FIELD)
                .put(MqttConstants.MQTT_CSV_LINE_SEPERATOR, CSV_LINE_SEPERATOR)
                .put(MqttConstants.MQTT_CSV_FIELD_SEPERATOR, CSV_FIELD_SEPERATOR);
        configNode.putArray(MqttConstants.MQTT_OM_OBSERVABLE_PROPERTIES).add(BIRD_COUNT_PROPERTY).add(TEMPERATURE_PROPERTY);

        observableProperties = Sets.newHashSet();
        observableProperties.add(BIRD_COUNT_PROPERTY);
        observableProperties.add(TEMPERATURE_PROPERTY);
    }

    @Test
    public void basic() {
        MqttConfiguration decodedConfig = decoder.decode((JsonNode) configNode);

        Assert.assertThat(decodedConfig.getKey(), CoreMatchers.is(equalTo(KEY)));
        Assert.assertThat(decodedConfig.isActive(), CoreMatchers.is(equalTo(IS_ACTIVE)));
        Assert.assertThat(decodedConfig.getHost(), CoreMatchers.is(equalTo(HOST)));
        Assert.assertThat(decodedConfig.getPort(), CoreMatchers.is(equalTo(PORT)));
        Assert.assertThat(decodedConfig.getTopic(), CoreMatchers.is(equalTo(TOPIC)));
        Assert.assertThat(decodedConfig.getProtocol(), CoreMatchers.is(equalTo(PROTOCOL)));
        Assert.assertThat(decodedConfig.getDecoder(), CoreMatchers.is(equalTo(DECODER)));
        Assert.assertThat(decodedConfig.getUsername(), CoreMatchers.is(equalTo(USERNAME)));
        Assert.assertThat(decodedConfig.getPassword(), CoreMatchers.is(equalTo(PASSWORD)));
        Assert.assertThat(decodedConfig.getObservationField(), CoreMatchers.is(equalTo(OBSERVATION_FIELD)));
        Assert.assertThat(decodedConfig.getObservableProperties(), CoreMatchers.is(equalTo(observableProperties)));
        Assert.assertThat(decodedConfig.getCsvLineSeperator(), CoreMatchers.is(equalTo(CSV_LINE_SEPERATOR)));
        Assert.assertThat(decodedConfig.getCsvFieldSeperator(), CoreMatchers.is(equalTo(CSV_FIELD_SEPERATOR)));

    }
}
