package org.n52.sos.mqtt.convert;

import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.InsertObservationRequest;

public interface MqttInsertObservationConverter<T> {

    InsertObservationRequest convert(T message) throws OwsExceptionReport;
}
