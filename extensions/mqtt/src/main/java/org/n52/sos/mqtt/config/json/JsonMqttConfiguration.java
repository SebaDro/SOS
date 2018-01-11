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

import java.util.Set;
import org.n52.sos.mqtt.config.MqttConfiguration;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class JsonMqttConfiguration implements MqttConfiguration {

    private String key;
    private String name;
    private boolean active;
    private String host;
    private String port;
    private String topic;
    private String username;
    private String password;
    private String protocol;
    private String decoder;
    private Set<String> observableProperties;
    private String observationField;
    private String csvLineSeperator;
    private String csvFieldSeperator;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setIsActive(boolean isActive) {
        this.active = isActive;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getDecoder() {
        return decoder;
    }

    @Override
    public void setDecoder(String decoder) {
        this.decoder = decoder;
    }

    @Override
    public Set<String> getObservableProperties() {
        return observableProperties;
    }

    @Override
    public void setObservableProperties(Set<String> observableProperties) {
        this.observableProperties = observableProperties;
    }

    @Override
    public String getObservationField() {
        return observationField;
    }

    @Override
    public void setObservationField(String observationField) {
        this.observationField = observationField;
    }

    @Override
    public String getCsvLineSeperator() {
        return csvLineSeperator;
    }

    @Override
    public void setCsvLineSeperator(String csvLineSeperator) {
        this.csvLineSeperator = csvLineSeperator;
    }

    @Override
    public String getCsvFieldSeperator() {
        return csvFieldSeperator;
    }

    @Override
    public void setCsvFieldSeperator(String csvFieldSeperator) {
        this.csvFieldSeperator = csvFieldSeperator;
    }

}
