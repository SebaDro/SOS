/**
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
package org.n52.sos.mqtt.api;

import org.joda.time.DateTime;

public class AdsbMessage implements MqttMessage {
    
    public static final String HEX = "hex";
    public static final String FLIGHT = "flight";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String ALTITUDE = "altitude";
    public static final String TRACK = "track";
    public static final String SPEED = "speed";
    public static final String TIME = "timestamp";
    public static final String SQUAWK = "squawk";
    
    public static final String ALTITUDE_UNIT = "ft";
    public static final String TRACK_UNIT = "°";
    public static final String SPEED_UNIT = "kn";
    
    private String hex;
    private String flight;
    private double lat;
    private double lon;
    private int altitude;
    private int track;
    private int speed;
    private DateTime time;
    private String squawk;
    
    @Override
    public String getProcedure() {
        return getHex();
    }
    /**
     * @return the hex
     */
    public String getHex() {
        return hex;
    }
    /**
     * @param hex the hex to set
     */
    public AdsbMessage setHex(String hex) {
        this.hex = hex;
        return this;
    }
    /**
     * @return the flight
     */
    public String getFlight() {
        return flight;
    }
    /**
     * @param flight the flight to set
     */
    public AdsbMessage setFlight(String flight) {
        this.flight = flight;
        return this;
    }
    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }
    /**
     * @param lat the lat to set
     */
    public AdsbMessage setLat(double lat) {
        this.lat = lat;
        return this;
    }
    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }
    /**
     * @param lon the lon to set
     */
    public AdsbMessage setLon(double lon) {
        this.lon = lon;
        return this;
    }
    /**
     * @return the altitude
     */
    public int getAltitude() {
        return altitude;
    }
    /**
     * @param altitude the altitude to set
     */
    public AdsbMessage setAltitude(int altitude) {
        this.altitude = altitude;
        return this;
    }
    /**
     * @return the track
     */
    public int getTrack() {
        return track;
    }
    /**
     * @param track the track to set
     */
    public AdsbMessage setTrack(int track) {
        this.track = track;
        return this;
    }
    /**
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }
    /**
     * @param speed the speed to set
     */
    public AdsbMessage setSpeed(int speed) {
        this.speed = speed;
        return this;
    }
    /**
     * @return the time
     */
    public DateTime getTime() {
        return time;
    }
    /**
     * @param time the time to set
     */
    public AdsbMessage setTime(DateTime time) {
        this.time = time;
        return this;
    }
    /**
     * @return the squawk
     */
    public String getSquawk() {
        return squawk;
    }
    /**
     * @param squawk the squawk to set
     */
    public AdsbMessage setSquawk(String squawk) {
        this.squawk = squawk;
        return this;
    }

}
