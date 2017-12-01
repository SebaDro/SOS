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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.faroe.annotation.Setting;
import org.n52.sos.mqtt.MqttSettings;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.api.TtnMessage;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.n52.sos.mqtt.convert.TtnInsertObservationConverter;
import org.n52.sos.mqtt.convert.TtnInsertSensorConverter;

/**
 *
 * @author Sebastian Drost
 */
public class TtnDecoder extends AbstractMqttJsonDecoder {

    private String[] observableProperty;


    @Override
    protected MqttMessage parseMessage(JsonNode n) {
        JsonNode node = getJsonNode(n, TtnMessage.PAYLOAD_FIELDS);
        if (node.isArray()) {
            for (JsonNode on : node) {
                ((ObjectNode) on).put(TtnMessage.PROCEDURE, getString(n, TtnMessage.DEV_ID));
            };
        } else {
            ((ObjectNode) node).put(TtnMessage.PROCEDURE, getString(n, TtnMessage.DEV_ID));
        }
        return new TtnMessage()
                .setDevId(getString(n, TtnMessage.DEV_ID))
                .setOmPayload(node);
    }

    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        return new TtnInsertSensorConverter().setObservableProperty(observableProperty);
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return new TtnInsertObservationConverter();
    }

    @Setting(MqttSettings.MQTT_OM_OBSERVABLE_PROPERTY)
    public void setObservableProperty(String property) {
        this.observableProperty = property.split(";");
    }

    public String[] getObservableProperty() {
        return observableProperty;
    }

}
