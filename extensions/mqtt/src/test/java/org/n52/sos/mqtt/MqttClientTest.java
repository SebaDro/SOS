package org.n52.sos.mqtt;

public class MqttClientTest {

    String json =
            "[ {\"hex\":\"48455d\", \"flight\":\"KLM56T  \", \"lat\":52.020584, \"lon\":7.423248, \"altitude\":33325, \"track\":294, \"speed\":451},"
            + "{\"hex\":\"3c5eed\", \"flight\":\"GWI67Y  \", \"lat\":51.770185, \"lon\":7.237473, \"altitude\":23975, \"track\":70, \"speed\":401},"
            + "{\"hex\":\"4851ae\", \"flight\":\"KLM427  \", \"lat\":51.919618, \"lon\":7.347028, \"altitude\":31725, \"track\":112, \"speed\":503},"
            + "{\"hex\":\"484161\", \"flight\":\"KLM1374 \", \"lat\":52.114765, \"lon\":7.506880, \"altitude\":33075, \"track\":293, \"speed\":451},"
            + "{\"hex\":\"4844e7\", \"flight\":\"\", \"lat\":51.516449, \"lon\":7.035630, \"altitude\":32950, \"track\":122, \"speed\":487},"
            + "{\"hex\":\"4840e6\", \"flight\":\"KLM1768 \", \"lat\":52.456421, \"lon\":6.599045, \"altitude\":23050, \"track\":318, \"speed\":358},"
            + "{\"hex\":\"484fde\", \"flight\":\"KLM1602 \", \"lat\":51.507614, \"lon\":8.032639, \"altitude\":38000, \"track\":318, \"speed\":451}]";
    
    
    /*
     * Procedure: hex
     * FeatureOfInterest: flight / hex
     * Phenomenon: track, speed, altitude
     * Offering: hex
     * SamplingGeometry: lat, lon
     */
    
    /*
     * Insert Sensor and Observations:
     * 
     * private final InsertSensorDAO insertSensorDao;
     * insertSensorDao.insertSensor(insertSensorRequest);
     * 
     * private final InsertObservationDAO insertObservationDao; 
     * nsertObservationDao.insertObservation(insertObservationRequest);
     * 
     */
    
}
