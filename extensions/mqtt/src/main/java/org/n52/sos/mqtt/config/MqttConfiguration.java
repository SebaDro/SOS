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

import java.util.Set;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MqttConfiguration {

    private String key;
    private boolean isActive;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDecoder() {
        return decoder;
    }

    public void setDecoder(String decoder) {
        this.decoder = decoder;
    }

    public Set<String> getObservableProperties() {
        return observableProperties;
    }

    public void setObservableProperties(Set<String> observableProperties) {
        this.observableProperties = observableProperties;
    }

    public String getObservationField() {
        return observationField;
    }

    public void setObservationField(String observationField) {
        this.observationField = observationField;
    }

    public String getCsvLineSeperator() {
        return csvLineSeperator;
    }

    public void setCsvLineSeperator(String csvLineSeperator) {
        this.csvLineSeperator = csvLineSeperator;
    }

    public String getCsvFieldSeperator() {
        return csvFieldSeperator;
    }

    public void setCsvFieldSeperator(String csvFieldSeperator) {
        this.csvFieldSeperator = csvFieldSeperator;
    }

}
