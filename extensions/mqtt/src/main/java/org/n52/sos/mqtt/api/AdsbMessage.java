package org.n52.sos.mqtt.api;

import org.joda.time.DateTime;

public class AdsbMessage {
    
    public static final String HEX = "hex";
    public static final String FLIGHT = "flight";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String ALTITUDE = "altitude";
    public static final String TRACK = "track";
    public static final String SPEED = "speed";
    public static final String TIME = "time";
    
    private String hex;
    private String flight;
    private double lat;
    private double lon;
    private int altitude;
    private int track;
    private int speed;
    private DateTime time;
    
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
    public void setTime(DateTime time) {
        this.time = time;
    }

}
