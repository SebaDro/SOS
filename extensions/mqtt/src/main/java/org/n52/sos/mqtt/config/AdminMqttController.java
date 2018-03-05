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

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.sos.exception.NoSuchIdentifierException;
import org.n52.sos.exception.ows.concrete.DuplicateIdentifierException;
import org.n52.sos.mqtt.MqttConsumer;
import org.n52.sos.mqtt.MqttConsumerRepository;
import org.n52.sos.mqtt.config.json.JsonMqttConfiguration;
import org.n52.sos.mqtt.decode.MqttDecoderFactory;
import org.n52.sos.web.common.AbstractController;
import org.n52.sos.web.common.ControllerConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @since 4.0.0
 *
 */
@Controller
@RequestMapping(value = ControllerConstants.Paths.ADMIN_MQTT)
public class AdminMqttController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminMqttController.class);

    private static final String MQTT_SETTINGS_DEFINITION_KEY = "mqttSettingDefinitionGroup";

    private static final String MQTT_CONFIGURATIONS = "mqttConfigurations";

    private static final String MQTT_CONFIGURATION_KEY = "mqttConfigurationKey";

    private static final String MQTT_CONFIGURATION_ACTIVATION = "mqttConfigurationActivation";

    @Inject
    private MqttConfigurationDao mqttConfigDao;

    @Inject
    private MqttConsumerRepository mqttRepository;

    @Inject
    MqttDecoderFactory decoderFactory;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view() {
        Map<String, Object> model = new HashMap<>(1);
        List<MqttConfiguration> configurations = mqttConfigDao.getAllMqttConfigurations().stream()
                .sorted(new Comparator<MqttConfiguration>() {
                    @Override
                    public int compare(MqttConfiguration o1, MqttConfiguration o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                })
                .collect(Collectors.toList());;

        model.put(MQTT_CONFIGURATIONS, configurations);

        return new ModelAndView(ControllerConstants.Views.ADMIN_MQTT, model);
    }

    @ResponseBody
    @RequestMapping(value = "/{configurationId}", method = RequestMethod.GET)
    public MqttConfiguration mqttConfigurationForId(@PathVariable String configurationId) throws NoSuchIdentifierException {

        Optional<MqttConfiguration> config = mqttConfigDao.getMqttConfigurationById(configurationId);
        if (!config.isPresent()) {
            throw new NoSuchIdentifierException(configurationId);
        }
        MqttConfiguration conf = config.get();
        return conf;
    }

    @ResponseBody
    @RequestMapping(value = "/decoders", method = RequestMethod.GET)
    public Map<String, String> decoderValues() throws NoSuchIdentifierException {
        Map<String, String> decoders = Lists.newArrayList(MqttDecoderFactory.Decoder.values()).stream()
                .sorted()
                .collect(Collectors.toMap(MqttDecoderFactory.Decoder::qualifiedName, MqttDecoderFactory.Decoder::getName));
        return decoders;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public MqttConfiguration create(@RequestBody JsonMqttConfiguration config) throws DuplicateIdentifierException {

        if (mqttConfigDao.hasMqttConfigurationForName(config.getName())) {
            throw new DuplicateIdentifierException("MQTT configuration", config.getName());
        }
        config.setKey(UUID.randomUUID().toString());
        mqttConfigDao.saveMqttConfiguration(config);
        mqttRepository.create(config);

        return config;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/operations",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void operation(@RequestBody Map<String, Object> request) throws NoSuchIdentifierException, MqttException {

        String configurationId = (String) request.get(MQTT_CONFIGURATION_KEY);
        boolean activation = (boolean) request.get(MQTT_CONFIGURATION_ACTIVATION);
        Optional<MqttConfiguration> config = mqttConfigDao.getMqttConfigurationById(configurationId);
        if (!config.isPresent()) {
            throw new NoSuchIdentifierException(configurationId);
        }

        if (activation) {
            mqttRepository.get(configurationId).connect();
        } else {
            mqttRepository.get(configurationId).cleanup();
        }

        MqttConfiguration configuration = config.get();
        configuration.setIsActive(activation);
        mqttConfigDao.updateMqttConfiguration(configuration);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody JsonMqttConfiguration config) {
        MqttConsumer mqttClient = mqttRepository.get(config.getKey());
        if (mqttClient.isConnected()) {
            try {
                mqttClient.cleanup();
                mqttRepository.update(config);
                mqttClient.connect();
            } catch (MqttException ex) {
                LOG.error("Error while opening or closing MQTT connection for configuration" + config.getKey(), ex);
            } finally {
                config.setIsActive(mqttClient.isConnected());
                mqttConfigDao.updateMqttConfiguration(config);
            }
        } else {
            mqttRepository.update(config);
            mqttConfigDao.updateMqttConfiguration(config);
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchIdentifierException.class)
    public String onError(NoSuchIdentifierException e) {
        return String.format("The identifier %s is unknown!", e.getIdentifier());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DuplicateIdentifierException.class)
    public String onError(OwsExceptionReport e) {
        return "There is already a configuration registered with the given name. " + e.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MqttException.class)
    public String onError(MqttException e) {
        return "MQTT consumer could not be connected/closed" + e.getMessage();
    }
}
