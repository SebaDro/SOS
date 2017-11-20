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

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.n52.sos.config.annotation.Configurable;
import org.n52.sos.config.annotation.Setting;
import org.n52.sos.mqtt.MqttSettings;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author Sebastian Drost
 */
@Configurable
public abstract class AbstractMqttCsvDecoder implements MqttDecoder {

    private String lineSeperator;
    private String fieldSeperator;

    protected abstract MqttMessage parseMessage(String n);

    @Override
    public Set<MqttMessage> decode(String payload) {
        Set<MqttMessage> messages = Sets.newHashSet();
        if (payload == null || payload.isEmpty()) {
            return messages;
        }
        StrTokenizer tokenizer = new StrTokenizer(payload, lineSeperator);
        for (String m : ((List<String>) tokenizer.getTokenList())) {
            MqttMessage message = parseMessage(m);
            if (message != null) {
                messages.add(parseMessage(m));
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
