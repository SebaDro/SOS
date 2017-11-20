/*
 * Copyright (C) 2017 52north.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.n52.sos.mqtt.decode;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.sos.config.annotation.Setting;
import org.n52.sos.mqtt.MqttSettings;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.api.OmMessage;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.n52.sos.mqtt.convert.OmInsertObservationConverter;
import org.n52.sos.mqtt.convert.OmInsertSensorConverter;

/**
 *
 * @author Sebastian Drost
 */
public class OmDecoder extends AbstractMqttJsonDecoder {

    private String[] observableProperty;
    private String observationField;

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
        OmInsertSensorConverter converter = new OmInsertSensorConverter();
        converter.setObservableProperties(observableProperty);
        return converter;
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return new OmInsertObservationConverter();
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
