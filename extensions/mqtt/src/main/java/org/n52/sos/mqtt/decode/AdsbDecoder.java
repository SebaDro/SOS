package org.n52.sos.mqtt.decode;

import java.util.Set;

import org.fusesource.mqtt.client.Message;
import org.joda.time.DateTime;
import org.n52.sos.mqtt.api.AdsbMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

public class AdsbDecoder {
    
    public Set<AdsbMessage> decoder(JsonNode json) {
        Set<AdsbMessage> messages = Sets.newHashSet();
        if (json == null || json.isNull() || json.isMissingNode()) {
            return messages;
        }
        if (json.isArray()) {
            for (JsonNode n : json) {
                if (n.isObject()) {
                    messages.add(parseAdsbMessage(n));
                }
            }
        } else if (json.isObject()) {
            messages.add(parseAdsbMessage(json));
        }
        
        return messages;
    }

    private AdsbMessage parseAdsbMessage(JsonNode json) {
        return new AdsbMessage()
                .setHex(getString(json, AdsbMessage.HEX))
                .setFlight(getString(json, AdsbMessage.HEX))
                .setAltitude(getInteger(json, AdsbMessage.ALTITUDE))
                .setLat(getDouble(json, AdsbMessage.LAT))
                .setLon(getDouble(json, AdsbMessage.LON))
                .setTrack(getInteger(json, AdsbMessage.TRACK))
                .setSpeed(getInteger(json, AdsbMessage.SPEED))
                .setTime(getDateTime(json, AdsbMessage.TIME));
    }

    private String getString(JsonNode json, String name) {
        return json.get(name).asText();
    }
    
    private double getDouble(JsonNode json, String name) {
        return json.get(name).asDouble();
    }
    
    private int getInteger(JsonNode json, String name) {
        return json.get(name).asInt();
    }
    
    private DateTime getDateTime(JsonNode json, String name){
    	return new DateTime(getLong(json, name));
    }
    
    private long getLong(JsonNode json, String name){
    	return json.get(name).asLong();
    }

}
