package org.n52.sos.mqtt.convert;

import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.InsertSensorRequest;

public interface MqttInsertSensorConverter<T> {

    InsertSensorRequest convert(T message) throws OwsExceptionReport;

}
