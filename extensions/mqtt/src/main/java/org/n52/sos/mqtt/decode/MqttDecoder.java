package org.n52.sos.mqtt.decode;

import java.util.Set;

import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;

import com.fasterxml.jackson.databind.JsonNode;

public interface MqttDecoder {

    Set<MqttMessage> decoder(JsonNode json);

    MqttInsertSensorConverter getInsertSensorConverter();
    
    MqttInsertObservationConverter getInsertOnbservationConverter();
    
}
