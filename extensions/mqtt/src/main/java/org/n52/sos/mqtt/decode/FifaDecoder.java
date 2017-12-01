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

import org.n52.sos.mqtt.api.FifaMessage;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.FifaInsertObservationConverter;
import org.n52.sos.mqtt.convert.FifaInsertSensorConverter;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;

import com.fasterxml.jackson.databind.JsonNode;

public class FifaDecoder extends AbstractMqttJsonDecoder {

    @Override
    protected MqttMessage parseMessage(JsonNode json) {
        return new FifaMessage()
                .setOffering(getString(json, FifaMessage.OFFERING))
                .setProcedure(getString(json, FifaMessage.PROCEDURE))
                .setFeatureOfInterest(getString(json, FifaMessage.FEATURE_OF_INTEREST))
                .setObservedProperty(getString(json, FifaMessage.OBSERVED_PROPERTY))
                .setPhenomenonTime(getDateTime(json, FifaMessage.PHENOMENON_TIME))
                .setResult(getString(json, FifaMessage.RESULT))
                .setParameter(getMap(json, FifaMessage.PARAMETER));
    }

    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        return new FifaInsertSensorConverter();
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return new FifaInsertObservationConverter();
    }

}
