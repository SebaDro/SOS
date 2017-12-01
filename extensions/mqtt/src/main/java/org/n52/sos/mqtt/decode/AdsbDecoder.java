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


import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.AdsbToInsertObservation;
import org.n52.sos.mqtt.convert.AdsbToInsertSensor;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;

import com.fasterxml.jackson.databind.JsonNode;

public class AdsbDecoder extends AbstractMqttJsonDecoder {

    protected MqttMessage parseMessage(JsonNode json) {
        return new AdsbMessage()
                .setHex(getString(json, AdsbMessage.HEX))
                .setFlight(getString(json, AdsbMessage.HEX))
                .setAltitude(getInteger(json, AdsbMessage.ALTITUDE))
                .setLat(getDouble(json, AdsbMessage.LAT))
                .setLon(getDouble(json, AdsbMessage.LON))
                .setTrack(getInteger(json, AdsbMessage.TRACK))
                .setSpeed(getInteger(json, AdsbMessage.SPEED))
                .setTime(getDateTime(json, AdsbMessage.TIME))
                .setSquawk(getString(json, AdsbMessage.SQUAWK));
    }


    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        return new AdsbToInsertSensor();
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return new AdsbToInsertObservation();
    }

}
