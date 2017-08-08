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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.sos.config.annotation.Setting;
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
public class TtnDecoder extends AbstractMqttDecoder {

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
