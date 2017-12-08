/*
 * Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.mqtt.decode;

import javax.inject.Inject;
import org.apache.commons.lang.text.StrTokenizer;
import org.joda.time.format.ISODateTimeFormat;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
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
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class FluorometerDecoder extends AbstractMqttCsvDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(FluorometerDecoder.class);

    @Inject
    FluorometerInsertObservationConverter insertObservationConverter;

    @Inject
    FluorometerInsertSensorConverter insertSensorConverter;

    @Override
    protected MqttMessage parseMessage(String message) throws OwsExceptionReport {
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
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            throw new NoApplicableCodeException().causedBy(ex).withMessage("Error while parsing message with non expected content.", message);
        }
    }

    @Override
    public MqttInsertSensorConverter getInsertSensorConverter() {
        return insertSensorConverter;
    }

    @Override
    public MqttInsertObservationConverter getInsertOnbservationConverter() {
        return insertObservationConverter;
    }

}
