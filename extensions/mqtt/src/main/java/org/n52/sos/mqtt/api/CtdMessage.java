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
    public static final String CONDUCTIVITY = "conductivity";
    public static final String SALINITY = "salinity";
    public static final String SOUND_VELOCITY = "sound velocity";

    public static final String PRESSURE_UNIT = "dbar";
    public static final String TEMPERATURE_UNIT = "°C";
    public static final String CONDUCTIVITY_UNIT = "mS/cm";
    public static final String SALINITY_UNIT = "PSU";
    public static final String SOUND_VELOCITY_UNIT = "m/s";

    private DateTime shoreStationTime;
    private DateTime instrumentTime;
    private String sensorId;
    private double pressure;
    private double temperature;
    private double conductivity;
    private double salinity;
    private double soundVelocity;

    public DateTime getShoreStationTime() {
        return shoreStationTime;
    }

    public CtdMessage setShoreStationTime(DateTime time) {
        this.shoreStationTime = time;
        return this;
    }
    
        public DateTime getInstrumentTime() {
        return instrumentTime;
    }

    public CtdMessage setInstrumentTime(DateTime instrumentTime) {
        this.instrumentTime = instrumentTime;
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

    public double getConductivity() {
        return conductivity;
    }

    public CtdMessage setConductivity(double condition) {
        this.conductivity = condition;
        return this;
    }

    public double getSalinity() {
        return salinity;
    }

    public CtdMessage setSalinity(double saltiness) {
        this.salinity = saltiness;
        return this;
    }

    public double getSoundVelocity() {
        return soundVelocity;
    }

    public CtdMessage setSoundVelocity(double soundV) {
        this.soundVelocity = soundV;
        return this;
    }

    @Override
    public String getProcedure() {
        return sensorId;
    }

}
