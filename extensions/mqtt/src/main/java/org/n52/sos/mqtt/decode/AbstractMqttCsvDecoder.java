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

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.text.StrTokenizer;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import org.n52.sos.mqtt.api.MqttMessage;
import org.n52.sos.mqtt.config.MqttConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Configurable
public abstract class AbstractMqttCsvDecoder extends MqttDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMqttCsvDecoder.class);

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

    @Override
    public void configure(MqttConfiguration config) {
        setLineSeperator(config.getCsvLineSeperator());
        setFieldSeperator(config.getCsvFieldSeperator());
    }

    public String getLineSeperator() {
        return lineSeperator;
    }

    public void setLineSeperator(String lineSeperator) {
        this.lineSeperator = lineSeperator;
    }

    public String getFieldSeperator() {
        return fieldSeperator;
    }

    public void setFieldSeperator(String fieldSeperator) {
        this.fieldSeperator = fieldSeperator;
    }

}
