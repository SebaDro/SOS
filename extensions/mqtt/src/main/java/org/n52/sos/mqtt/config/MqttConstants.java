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

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public interface MqttConstants {

    String MQTT_CONFIG = "mqtt.config";

    String MQTT_KEY = "mqtt.key";
    
    String MQTT_NAME= "mqtt.name";

    String MQTT_ACTIVE = "mqtt.active";

    String MQTT_HOST = "mqtt.host";

    String MQTT_PORT = "mqtt.port";

    String MQTT_TOPIC = "mqtt.topic";

    String MQTT_USERNAME = "mqtt.username";

    String MQTT_PASSWORD = "mqtt.password";

    String MQTT_DECODER = "mqtt.decoder";

    String MQTT_PROTOCOL = "mqtt.protocol";

    String MQTT_OM_OBSERVABLE_PROPERTIES = "mqtt.om.observableProperties";

    String MQTT_OM_OBSERVATION_FIELD = "mqtt.om.observationField";

    String MQTT_CSV_FIELD_SEPERATOR = "mqtt.csv.fieldSeperator";

    String MQTT_CSV_LINE_SEPERATOR = "mqtt.csv.lineSeperator";

}
