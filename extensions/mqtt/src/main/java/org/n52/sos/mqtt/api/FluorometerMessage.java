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
 * @author Sebastian
 */
public class FluorometerMessage implements MqttMessage {

    public static final String FLUORESCENCE = "fluorescence wavelength";
    public static final String TURBIDITY = "turbidity wavelength";
    public static final String CHL = "CHL counts";
    public static final String NTU = "NTU counts";
    public static final String THERMISTOR = "thermistor";

    public static final String FLUORESCENCE_UNIT = "nm";
    public static final String TURBIDITY_UNIT = "nm";

    private DateTime shoreStationTime;
    private DateTime instrumentTime;
    private String sensorId;
    private double fluorescenceWavelength;
    private double turbidityWavelength;
    private int chlCount;
    private int ntuCount;
    private int thermistor;

    public DateTime getShoreStationTime() {
        return shoreStationTime;
    }

    public FluorometerMessage setShoreStationTime(DateTime shoreStationTime) {
        this.shoreStationTime = shoreStationTime;
        return this;
    }

    public DateTime getInstrumentTime() {
        return instrumentTime;
    }

    public FluorometerMessage setInstrumentTime(DateTime instrumentTime) {
        this.instrumentTime = instrumentTime;
        return this;
    }

    public String getSensorId() {
        return sensorId;
    }

    public FluorometerMessage setSensorId(String sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public double getFluorescenceWavelength() {
        return fluorescenceWavelength;
    }

    public FluorometerMessage setFluorescenceWavelength(double fluorescence) {
        this.fluorescenceWavelength = fluorescence;
        return this;
    }

    public double getTurbidityWavelength() {
        return turbidityWavelength;
    }

    public FluorometerMessage setTurbidityWavelength(double turbidity) {
        this.turbidityWavelength = turbidity;
        return this;
    }

    public int getChlCount() {
        return chlCount;
    }

    public FluorometerMessage setChlCount(int chl) {
        this.chlCount = chl;
        return this;
    }

    public int getNtuCount() {
        return ntuCount;
    }

    public FluorometerMessage setNtuCount(int ntu) {
        this.ntuCount = ntu;
        return this;
    }    

    public int getThermistor() {
        return thermistor;
    }

    public FluorometerMessage setThermistor(int thermistor) {
        this.thermistor = thermistor;
        return this;
    }
    
    @Override
    public String getProcedure() {
        return getSensorId();
    }

}
