package org.n52.sos.mqtt;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.n52.sos.util.JSONUtils;

import com.fasterxml.jackson.databind.JsonNode;

public class MqttClient {
    
    static String json =
            "[ {\"hex\":\"48455d\", \"flight\":\"KLM56T  \", \"lat\":52.020584, \"lon\":7.423248, \"altitude\":33325, \"track\":294, \"speed\":451},"
            + "{\"hex\":\"3c5eed\", \"flight\":\"GWI67Y  \", \"lat\":51.770185, \"lon\":7.237473, \"altitude\":23975, \"track\":70, \"speed\":401},"
            + "{\"hex\":\"4851ae\", \"flight\":\"KLM427  \", \"lat\":51.919618, \"lon\":7.347028, \"altitude\":31725, \"track\":112, \"speed\":503},"
            + "{\"hex\":\"484161\", \"flight\":\"KLM1374 \", \"lat\":52.114765, \"lon\":7.506880, \"altitude\":33075, \"track\":293, \"speed\":451},"
            + "{\"hex\":\"4844e7\", \"flight\":\"\", \"lat\":51.516449, \"lon\":7.035630, \"altitude\":32950, \"track\":122, \"speed\":487},"
            + "{\"hex\":\"4840e6\", \"flight\":\"KLM1768 \", \"lat\":52.456421, \"lon\":6.599045, \"altitude\":23050, \"track\":318, \"speed\":358},"
            + "{\"hex\":\"484fde\", \"flight\":\"KLM1602 \", \"lat\":51.507614, \"lon\":8.032639, \"altitude\":38000, \"track\":318, \"speed\":451}]";
    static String topic = "adsb52n";
    
    /*
     * Procedure: hex
     * FeatureOfInterest: flight / hex
     * Phenomenon: track, speed, altitude
     * Offering: hex
     * SamplingGeometry: lat, lon
     * timestamp: 
     * 
     * 
     * Local caching + expire time (last update)
     */
   

    public static void main(String[] args) {
        MQTT mqtt = new MQTT();
        BlockingConnection connection = null;
        try {
            //192.168.52.138
            mqtt.setHost("192.168.52.138", 1883);
            connection = mqtt.blockingConnection();
            connection.connect();

            

            Topic[] topics = { new Topic(topic, QoS.AT_LEAST_ONCE) };
            byte[] qoses = connection.subscribe(topics);

            connection.publish(topic, json.getBytes(), QoS.AT_LEAST_ONCE, false);
            Message message = connection.receive();
            byte[] payload = message.getPayload();
            
            
            JsonNode loadString = JSONUtils.loadString(new String(message.getPayload()));
            // process the message then:
            System.out.println("Payload: " + new String(payload));
            
            message.ack();
            connection.disconnect();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null && connection.isConnected()) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
