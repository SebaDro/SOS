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
package org.n52.sos.mqtt.api;

import org.joda.time.DateTime;

/**
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
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
