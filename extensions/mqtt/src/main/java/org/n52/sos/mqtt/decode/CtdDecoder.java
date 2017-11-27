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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.joda.time.format.ISODateTimeFormat;
import org.n52.sos.mqtt.api.CtdMessage;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.marine.CtdInsertObservationConverter;
import org.n52.sos.mqtt.convert.marine.CtdInsertSensorConverter;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastian Drost
 */
public class CtdDecoder extends AbstractMqttCsvDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(CtdDecoder.class);

    @Override
    protected MqttMessage parseMessage(String message) {
        try {
            String[] sensorData = new StrTokenizer(message, "|").getTokenArray();
            String[] observationData = new StrTokenizer(sensorData[2], " ").getTokenArray();

            return new CtdMessage()
                    .setShoreStationTime(ISODateTimeFormat.dateTime().parseDateTime(sensorData[0]))
                    .setSensorId(sensorData[1])
                    .setPressure(Double.parseDouble(observationData[0]))
                    .setTemperature(Double.parseDouble(observationData[1]))
                    .setConductivity(Double.parseDouble(observationData[2]))
                    .setSalinity(Double.parseDouble(observationData[3]))
                    .setSoundVelocity(Double.parseDouble(observationData[4]));
        } catch (Exception ex) {
            LOG.error("Error parsing MQTT message.", ex);
            return null;
        }
    }

    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        return new CtdInsertSensorConverter();
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return new CtdInsertObservationConverter();
    }

}
