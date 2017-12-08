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
import javax.inject.Inject;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.sos.mqtt.MqttSettings;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.api.OmMessage;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.n52.sos.mqtt.convert.OmInsertObservationConverter;
import org.n52.sos.mqtt.convert.OmInsertSensorConverter;

/**
 * 
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Configurable
public class OmDecoder extends AbstractMqttJsonDecoder {

    private String[] observableProperty;
    private String observationField;

    @Inject
    OmInsertSensorConverter insertSensorConverter;

    @Inject
    OmInsertObservationConverter insertObservationConverter;

    @Override
    protected MqttMessage parseMessage(JsonNode n) {
        JsonNode payload = n.findParent(observationField).get(observationField);
        OmMessage message = new OmMessage();
        if (payload.isArray()) {
            message.setProcedure(payload.get(0).get("procedure").asText());
        } else {
            message.setProcedure(payload.get("procedure").asText());
        }
        message.setOmPayload(payload);
        return message;
    }

    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        insertSensorConverter.setObservableProperties(observableProperty);
        return insertSensorConverter;
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return insertObservationConverter;
    }

    public String[] getObservableProperty() {
        return observableProperty;
    }

    @Setting(MqttSettings.MQTT_OM_OBSERVABLE_PROPERTY)
    public void setObservableProperty(String observableProperty) {
        this.observableProperty = observableProperty.split(";");
    }

    public String getObservationField() {
        return observationField;
    }

    @Setting(MqttSettings.MQTT_OM_OBSERVATION_FIELD)
    public void setObservationField(String observationField) {
        this.observationField = observationField;
    }

}
