/*
 * Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
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

import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.sos.mqtt.api.FluorometerMessage;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author Sebastian
 */
public class FluorometerDecoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String LINE_SEPERATOR = "\n";

    private static final String MESSAGE_WITH_NUMERIC_OBSERVATION_VALUES
            = "2017-11-23T09:36:27.375Z|WL-ECO-FLNTU-4476|11/23/17	09:34:42	695	56	700	212	547";

     private static final String MESSAGE_WITH_NON_EXPECTED_CONTENT
            = "2017-11-23T09:36:27.375Z|WL-ECO-FLNTU-4476|  Press   Temp    Cond    Sal    SoundV";

    private static FluorometerDecoder decoder;

    @BeforeClass
    public static void init() {
        decoder = new FluorometerDecoder();
        decoder.setLineSeperator(LINE_SEPERATOR);
    }

    @Test
    public void parseMessageTest() throws OwsExceptionReport {
        MqttMessage observationMessage = decoder.parseMessage(MESSAGE_WITH_NUMERIC_OBSERVATION_VALUES);

        Assert.assertThat(((FluorometerMessage) observationMessage).getFluorescenceWavelength(), CoreMatchers.equalTo(695.));
        Assert.assertThat(((FluorometerMessage) observationMessage).getChlCount(), CoreMatchers.equalTo(56));
        Assert.assertThat(((FluorometerMessage) observationMessage).getTurbidityWavelength(), CoreMatchers.equalTo(700.));
        Assert.assertThat(((FluorometerMessage) observationMessage).getNtuCount(), CoreMatchers.equalTo(212));
        Assert.assertThat(((FluorometerMessage) observationMessage).getThermistor(), CoreMatchers.equalTo(547));
    }

    @Test
    public void shouldShowExcpetionWhenReceivingNonNumericObservationValues() throws OwsExceptionReport {
        thrown.expect(NoApplicableCodeException.class);
        thrown.expectMessage(is("Error while parsing message with non expected content."));

        decoder.parseMessage(MESSAGE_WITH_NON_EXPECTED_CONTENT);
    }

}
