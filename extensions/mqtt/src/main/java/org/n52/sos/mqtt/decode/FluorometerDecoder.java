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

import org.apache.commons.lang.text.StrTokenizer;
import org.joda.time.format.ISODateTimeFormat;
import org.n52.sos.mqtt.api.FluorometerMessage;
import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.convert.marine.FluorometerInsertObservationConverter;
import org.n52.sos.mqtt.convert.marine.FluorometerInsertSensorConverter;
import org.n52.sos.mqtt.convert.MqttInsertObservationConverter;
import org.n52.sos.mqtt.convert.MqttInsertSensorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastian
 */
public class FluorometerDecoder extends AbstractMqttCsvDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(FluorometerDecoder.class);

    @Override
    protected MqttMessage parseMessage(String message) {
        try {
            String[] sensorData = new StrTokenizer(message, "|").getTokenArray();
            String[] observationData = new StrTokenizer(sensorData[2], "\t").getTokenArray();

            return new FluorometerMessage()
                    .setShoreStationTime(ISODateTimeFormat.dateTime().parseDateTime(sensorData[0]))
                    .setSensorId(sensorData[1])
                    .setFluorescenceWavelength(Double.parseDouble(observationData[2]))
                    .setChlCount(Integer.parseInt(observationData[3]))
                    .setTurbidityWavelength(Double.parseDouble(observationData[4]))
                    .setNtuCount(Integer.parseInt(observationData[5]))
                    .setThermistor(Integer.parseInt(observationData[6]));
        } catch (Exception ex) {
            LOG.error("Error parsing MQTT message.", ex);
            return null;
        }
    }

    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        return new FluorometerInsertSensorConverter();
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return new FluorometerInsertObservationConverter();
    }

}
