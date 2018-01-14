package com.groupproject.cs3.vca.gps_information;

/**
 * Created by Gaming_PC on 18/11/2017.
 */

public class gpsCoordinates {
    private double lat;
    private double lon;



    public gpsCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;

    }

    public double getlat() {
        return lat;
    }

    public void setlat(double lat) {
        this.lat = lat;
    }
    public double getLon() {        return lon;
    }

    public void setlon(double lon) {
        this.lon = lon;
    }
}
