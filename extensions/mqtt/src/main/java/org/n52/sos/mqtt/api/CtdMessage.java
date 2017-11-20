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
package org.n52.sos.mqtt.api;

import org.joda.time.DateTime;

/**
 *
 * @author Sebastianstian Drost
 */
public class CtdMessage implements MqttMessage {

    public static final String PRESSURE = "pressure";
    public static final String TEMPERATURE = "temperature";
    public static final String CONDITION = "condition";
    public static final String SALTINESS = "saltiness";
    public static final String SOUND_V = "sound_v";

    public static final String PRESSURE_UNIT = "bar";
    public static final String TEMPERATURE_UNIT = "°";
    public static final String CONDITION_UNIT = "unknown";
    public static final String SALTINESS_UNIT = "unknown";
    public static final String SOUND_V_UNIT = "unknown";

    private DateTime time;
    private String sensorId;
    private double pressure;
    private double temperature;
    private double condition;
    private double saltiness;
    private String soundV;

    public DateTime getTime() {
        return time;
    }

    public CtdMessage setTime(DateTime time) {
        this.time = time;
        return this;
    }

    public String getSensorId() {
        return sensorId;
    }

    public CtdMessage setSensorId(String sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public double getPressure() {
        return pressure;
    }

    public CtdMessage setPressure(double pressure) {
        this.pressure = pressure;
        return this;
    }

    public double getTemperature() {
        return temperature;
    }

    public CtdMessage setTemperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public double getCondition() {
        return condition;
    }

    public CtdMessage setCondition(double condition) {
        this.condition = condition;
        return this;
    }

    public double getSaltiness() {
        return saltiness;
    }

    public CtdMessage setSaltiness(double saltiness) {
        this.saltiness = saltiness;
        return this;
    }

    public String getSoundV() {
        return soundV;
    }

    public CtdMessage setSoundV(String soundV) {
        this.soundV = soundV;
        return this;
    }

    @Override
    public String getProcedure() {
        return sensorId;
    }

}
