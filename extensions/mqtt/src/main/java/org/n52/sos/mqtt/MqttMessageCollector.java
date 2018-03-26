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

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.n52.sos.mqtt.api.MqttMessage;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class MqttMessageCollector {

    private int limit;
    private boolean batchActivated;
    private int actualSize;
    private Map<String, List<MqttMessage>> messages;

    public MqttMessageCollector(int limit, boolean isBatchActivated) {
        this.limit = limit;
        this.batchActivated = isBatchActivated;
        this.messages = new HashMap();
        this.actualSize = 0;
    }

    public void addMessage(MqttMessage message) {
        if (!messages.containsKey(message.getProcedure())) {
            messages.put(message.getProcedure(), Lists.newArrayList(message));
        } else {
            messages.get(message.getProcedure()).add(message);
        }
        actualSize++;
    }

    public void clearMessages() {
        messages.clear();
        actualSize = 0;
    }

    public boolean reachedLimit() {
        return actualSize >= limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isBatchActivated() {
        return batchActivated;
    }

    public void setBatchActivated(boolean batchActivated) {
        this.batchActivated = batchActivated;
    }

    public int getActualSize() {
        return actualSize;
    }

    public Map<String, List<MqttMessage>> getMessages() {
        return messages;
    }

    public List<MqttMessage> getMessagesForKey(String key) {
        return messages.get(key);
    }

}
