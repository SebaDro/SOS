/*
 * Copyright (C) 2017 52°North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.mqtt.config;

import java.util.Set;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public interface MqttConfiguration {

    public String getKey();

    public void setKey(String key);

    public boolean isActive();

    public void setIsActive(boolean isActive);

    public String getHost();

    public void setHost(String host);

    public String getPort();

    public void setPort(String port);

    public String getTopic();

    public void setTopic(String topic);

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);

    public String getProtocol();

    public void setProtocol(String protocol);

    public String getDecoder();

    public void setDecoder(String decoder);

    public Set<String> getObservableProperties();

    public void setObservableProperties(Set<String> observableProperties);

    public String getObservationField();

    public void setObservationField(String observationField);

    public String getCsvLineSeperator();

    public void setCsvLineSeperator(String csvLineSeperator);

    public String getCsvFieldSeperator();

    public void setCsvFieldSeperator(String csvFieldSeperator);

}
