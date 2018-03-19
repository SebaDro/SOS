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

import java.util.Set;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public interface MqttConfiguration {

    public String getKey();

    public void setKey(String key);

    public String getName();

    public void setName(String name);

    public boolean isActive();

    public void setIsActive(boolean isActive);

    public String getHost();

    public void setHost(String host);

    public String getPort();

    public void setPort(String port);

    public String getTopic();

    public void setTopic(String topic);

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);

    public String getProtocol();

    public void setProtocol(String protocol);

    public String getDecoder();

    public void setDecoder(String decoder);

    public Set<String> getObservableProperties();

    public void setObservableProperties(Set<String> observableProperties);

    public String getObservationField();

    public void setObservationField(String observationField);

    public String getCsvLineSeperator();

    public void setCsvLineSeperator(String csvLineSeperator);

    public String getCsvFieldSeperator();

    public void setCsvFieldSeperator(String csvFieldSeperator);

    public void setUseBatchRequest(boolean useBatchRequest);

    public boolean getUseBatchRequest();

    public void setBatchLimit(int limit);

    public int getBatchLimit();

}
