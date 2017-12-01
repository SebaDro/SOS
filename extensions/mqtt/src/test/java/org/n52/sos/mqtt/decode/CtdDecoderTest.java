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

import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.faroe.ConfigurationError;
import org.n52.sos.mqtt.api.CtdMessage;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author Sebastian Drost
 */
public class CtdDecoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String LINE_SEPERATOR = "\n";

    private static CtdDecoder decoder;

    @BeforeClass
    public static void init() {
        decoder = new CtdDecoder();
        decoder.setLineSeperator(LINE_SEPERATOR);
    }

    @Test
    public void shouldParseMessageWhenReceivingObservationValues() {
        String observationPayload = "2017-11-16T12:00:55.517Z|I-OCEAN7-304-0616641|  24.07  12.625  37.067  31.615 1495.3035 18:54:31.20M";
        CtdMessage observationMessage = (CtdMessage) decoder.parseMessage(observationPayload);

        Assert.assertThat(observationMessage.getPressure(), CoreMatchers.equalTo(24.07));
        Assert.assertThat(observationMessage.getTemperature(), CoreMatchers.equalTo(12.625));
        Assert.assertThat(observationMessage.getConductivity(), CoreMatchers.equalTo(37.067));
        Assert.assertThat(observationMessage.getSalinity(), CoreMatchers.equalTo(31.615));
        Assert.assertThat(observationMessage.getSoundVelocity(), CoreMatchers.equalTo(1495.3035));

    }

    @Test
    public void shouldNotParseMessageWhenReceivingNonObservationValues() {
        String paramPayload = "2017-11-16T12:00:50.468Z|I-OCEAN7-304-0616641|  Press   Temp    Cond    Sal    SoundV";
        CtdMessage paramMessage = (CtdMessage) decoder.parseMessage(paramPayload);

        Assert.assertThat(paramMessage, CoreMatchers.nullValue());
    }

}
