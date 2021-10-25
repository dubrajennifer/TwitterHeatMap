package com.udc.riws.ri.service.core.dto;

public class GeoLocationDto {
    private double latitude;
    private double longitude;

    public GeoLocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoLocationDto() {

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
