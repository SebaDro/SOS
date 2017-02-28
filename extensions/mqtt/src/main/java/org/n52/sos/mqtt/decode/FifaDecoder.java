package org.n52.sos.mqtt.decode;

import org.n52.sos.mqtt.api.FifaMessage;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.FifaInsertObservationConverter;
import org.n52.sos.mqtt.convert.FifaInsertSensorConverter;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;

import com.fasterxml.jackson.databind.JsonNode;

public class FifaDecoder extends AbstractMqttDecoder {

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
