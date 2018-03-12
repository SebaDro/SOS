/*
 * Copyright (C) 2018 52°North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.mqtt;

import java.util.ArrayList;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MqttMessageCollector {

    private int limit;
    private ArrayList<MqttMessage> messages;

    public MqttMessageCollector(int limit) {
        this.limit = limit;
    }

    public void addMessage(MqttMessage message) {
        messages.add(message);
    }

    public void clearMessages() {
        messages.clear();
    }

    public boolean reachedLimit() {
        return messages.size() >= limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<MqttMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MqttMessage> messages) {
        this.messages = messages;
    }

}
