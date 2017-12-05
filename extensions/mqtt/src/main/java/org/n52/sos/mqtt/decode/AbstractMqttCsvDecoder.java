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

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.text.StrTokenizer;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import org.n52.sos.mqtt.MqttSettings;
import org.n52.sos.mqtt.api.MqttMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author Sebastian Drost
 */
@Configurable
public abstract class AbstractMqttCsvDecoder implements MqttDecoder {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AbstractMqttCsvDecoder.class);

    private String lineSeperator;
    private String fieldSeperator;

    protected abstract MqttMessage parseMessage(String n) throws OwsExceptionReport;

    @Override
    public Set<MqttMessage> decode(String payload) {
        Set<MqttMessage> messages = Sets.newHashSet();
        if (payload == null || payload.isEmpty()) {
            return messages;
        }
        StrTokenizer tokenizer = new StrTokenizer(payload, lineSeperator);
        for (String m : ((List<String>) tokenizer.getTokenList())) {
            try {
                MqttMessage message = parseMessage(m);
                if (message != null) {
                    messages.add(parseMessage(m));
                }
            } catch (OwsExceptionReport ex) {
                LOG.error("Error while parsing message", ex);
            }
        };
        return messages;
    }

    public String getLineSeperator() {
        return lineSeperator;
    }

    @Setting(MqttSettings.MQTT_CSV_LINE_SEPERATOR)
    public void setLineSeperator(String lineSeperator) {
        this.lineSeperator = lineSeperator;
    }

    public String getFieldSeperator() {
        return fieldSeperator;
    }

    @Setting(MqttSettings.MQTT_CSV_FIELD_SEPERATOR)
    public void setFieldSeperator(String fieldSeperator) {
        this.fieldSeperator = fieldSeperator;
    }

}
