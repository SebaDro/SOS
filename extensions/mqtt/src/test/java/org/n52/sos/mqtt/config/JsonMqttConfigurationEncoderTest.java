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
package org.n52.sos.mqtt.config;

import org.n52.sos.mqtt.config.json.JsonMqttConfiguration;
import org.n52.sos.mqtt.config.json.JsonMqttConfigurationEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.UUID;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class JsonMqttConfigurationEncoderTest {

    private static final String KEY = UUID.randomUUID().toString();
    private static final boolean IS_ACTIVE = true;
    private static final String HOST = "ows.dev.52north.org";
    private static final String PORT = "1883";
    private static final String PROTOCOL = "tcp";
    private static final String TOPIC = "n52.adsb";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final String DECODER = "org.n52.sos.mqtt.decode.AdsbDecoder";
    private static final boolean USE_BATCH_REQUEST = true;
    private static final int BATCH_LIMIT = 10;
    private static final String OBSERVATION_FIELD = "bird count";
    private static final String CSV_LINE_SEPERATOR = "\\n";
    private static final String CSV_FIELD_SEPERATOR = ";";
    private static final String BIRD_COUNT_PROPERTY = "bird count";
    private static final String TEMPERATURE_PROPERTY = "temperature";

    private static JsonMqttConfigurationEncoder encoder;
    private static JsonMqttConfiguration configuration;
    private static JsonMqttConfiguration configurationWithNullValues;
    private static Set<String> observableProperties;

    @BeforeClass
    public static void init() {
        encoder = new JsonMqttConfigurationEncoder();

        configuration = new JsonMqttConfiguration();
        configuration.setKey(KEY);
        configuration.setIsActive(IS_ACTIVE);
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(PROTOCOL);
        configuration.setTopic(TOPIC);
        configuration.setUsername(USERNAME);
        configuration.setPassword(PASSWORD);
        configuration.setDecoder(DECODER);
        configuration.setUseBatchRequest(USE_BATCH_REQUEST);
        configuration.setBatchLimit(BATCH_LIMIT);
        configuration.setObservationField(OBSERVATION_FIELD);
        configuration.setCsvLineSeperator(CSV_LINE_SEPERATOR);
        configuration.setCsvFieldSeperator(CSV_FIELD_SEPERATOR);

        observableProperties = Sets.newHashSet();
        observableProperties.add(BIRD_COUNT_PROPERTY);
        observableProperties.add(TEMPERATURE_PROPERTY);
        configuration.setObservableProperties(observableProperties);

        configurationWithNullValues = new JsonMqttConfiguration();
        configurationWithNullValues.setKey(UUID.randomUUID().toString());
        configurationWithNullValues.setIsActive(IS_ACTIVE);
        configurationWithNullValues.setHost(HOST);
        configurationWithNullValues.setPort(PORT);
        configurationWithNullValues.setProtocol(PROTOCOL);
        configurationWithNullValues.setDecoder(DECODER);
    }

    @Test
    public void basic() {
        JsonNode jsonConfig = encoder.encode(configuration);

        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_KEY).asText(),
                CoreMatchers.equalTo(configuration.getKey()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_ACTIVE).asBoolean(),
                CoreMatchers.equalTo(configuration.isActive()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_HOST).asText(),
                CoreMatchers.equalTo(configuration.getHost()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_PORT).asText(),
                CoreMatchers.equalTo(configuration.getPort()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_PROTOCOL).asText(),
                CoreMatchers.equalTo(configuration.getProtocol()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_TOPIC).asText(),
                CoreMatchers.equalTo(configuration.getTopic()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_DECODER).asText(),
                CoreMatchers.equalTo(configuration.getDecoder()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_BATCH_REQUEST).asBoolean(),
                CoreMatchers.equalTo(configuration.getUseBatchRequest()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_BATCH_LIMIT).asInt(),
                CoreMatchers.equalTo(configuration.getBatchLimit()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_USERNAME).asText(),
                CoreMatchers.equalTo(configuration.getUsername()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_PASSWORD).asText(),
                CoreMatchers.equalTo(configuration.getPassword()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_OM_OBSERVATION_FIELD).asText(),
                CoreMatchers.equalTo(configuration.getObservationField()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_CSV_LINE_SEPERATOR).asText(),
                CoreMatchers.equalTo(configuration.getCsvLineSeperator()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_CSV_FIELD_SEPERATOR).asText(),
                CoreMatchers.equalTo(configuration.getCsvFieldSeperator()));

        Set<String> osbervableProperties = Sets.newHashSet();
        ((ArrayNode) jsonConfig.get(MqttConstants.MQTT_OM_OBSERVABLE_PROPERTIES))
                .forEach(n -> osbervableProperties.add(n.asText()));

        Assert.assertThat(osbervableProperties,
                Matchers.containsInAnyOrder(configuration.getObservableProperties()
                        .toArray(new String[configuration.getObservableProperties().size()])));
    }

    @Test
    public void shouldEncodeNullValuesAsEmptyString() {
        JsonNode jsonConfig = encoder.encode(configurationWithNullValues);

        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_USERNAME).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_PASSWORD).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_OM_OBSERVATION_FIELD).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_CSV_LINE_SEPERATOR).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_CSV_FIELD_SEPERATOR).asText(),
                is(IsEmptyString.isEmptyString()));
    }

    @Test
    public void shouldEncodeNullValuesAsEmptyStringOrSet() {
        JsonNode jsonConfig = encoder.encode(configurationWithNullValues);

        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_USERNAME).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_PASSWORD).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_OM_OBSERVATION_FIELD).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_CSV_LINE_SEPERATOR).asText(),
                is(IsEmptyString.isEmptyString()));
        Assert.assertThat(jsonConfig.get(MqttConstants.MQTT_CSV_FIELD_SEPERATOR).asText(),
                is(IsEmptyString.isEmptyString()));

        Set<String> observableProperties = Sets.newHashSet();
        ((ArrayNode) jsonConfig.get(MqttConstants.MQTT_OM_OBSERVABLE_PROPERTIES))
                .forEach(n -> observableProperties.add(n.asText()));

        Assert.assertThat(observableProperties, CoreMatchers.is(Matchers.empty()));
    }

}
