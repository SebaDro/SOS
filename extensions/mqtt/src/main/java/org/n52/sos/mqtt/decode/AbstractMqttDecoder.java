package org.n52.sos.mqtt.decode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.n52.sos.mqtt.api.AdsbMessage;
import org.n52.sos.mqtt.api.MqttMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;

public abstract class AbstractMqttDecoder implements MqttDecoder {
    
    protected abstract MqttMessage parseMessage(JsonNode n);

    @Override
    public Set<MqttMessage> decoder(JsonNode json) {
        Set<MqttMessage> messages = Sets.newHashSet();
        if (json == null || json.isNull() || json.isMissingNode()) {
            return messages;
        }
        if (json.isArray()) {
            for (JsonNode n : json) {
                if (n.isObject()) {
                    messages.add(parseMessage(n));
                }
            }
        } else if (json.isObject()) {
            messages.add(parseMessage(json));
        }
        
        return messages;
    }

    protected String getString(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return "";
        }
        return jsonNode.asText();
    }
    
    protected double getDouble(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return Double.NaN;
        }
        return jsonNode.asDouble();
    }
    
    protected int getInteger(JsonNode json, String name) {
        JsonNode jsonNode = json.get(name);
        if (jsonNode.isNull()) {
            return Integer.MIN_VALUE;
        }
        return jsonNode.asInt();
    }
    
    protected DateTime getDateTime(JsonNode json, String name){
        return new DateTime(getLong(json, name)*1000);
    }
    
    protected long getLong(JsonNode json, String name){
        return json.get(name).asLong();
    }
    
    protected Map<String, Object> getMap(JsonNode json, String name) {
        Map<String, Object> map = new HashMap<>();
        JsonNode jsonNode = json.get(name);
        if (jsonNode == null || jsonNode.isNull() || jsonNode.isMissingNode()) {
            return map;
        }
        if (jsonNode.isObject()) {
            while (jsonNode.fields().hasNext()) {
                Entry<String, JsonNode> type = (Entry<String, JsonNode>) jsonNode.fields().next();
                map.put(type.getKey(), parse(jsonNode));
                
            }
        }
        return map;
    }

    private Object parse(JsonNode jsonNode) {
        return jsonNode.getClass();
    }

}
