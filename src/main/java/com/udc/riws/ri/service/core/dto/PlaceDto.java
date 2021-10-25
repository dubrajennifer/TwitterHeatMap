package com.udc.riws.ri.service.core.dto;

public class PlaceDto {
    private String id;
    private String name;
    private String streetAddress;
    private String countryCode;
    private String country;
    private String placeType;
    private String fullName;
    private String GeometryType;
    private GeoLocationDto geoLocation;
    PlaceDto[] ContainedWithIn;

    public PlaceDto() {
    }

    public PlaceDto(String id, String name, GeoLocationDto geoLocation, String streetAddress, String countryCode, String country, String placeType, String fullName) {
        this.id = id;
        this.name = name;
        this.geoLocation = geoLocation;
        this.streetAddress = streetAddress;
        this.countryCode = countryCode;
        this.country = country;
        this.placeType = placeType;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGeometryType() {
        return GeometryType;
    }

    public void setGeometryType(String geometryType) {
        GeometryType = geometryType;
    }

    public PlaceDto[] getContainedWithIn() {
        return ContainedWithIn;
    }

    public void setContainedWithIn(PlaceDto[] containedWithIn) {
        ContainedWithIn = containedWithIn;
    }

    public GeoLocationDto getGeoLocation(){
        return geoLocation;
    }

    public void setGeoLocation(GeoLocationDto geoLocation){
        this.geoLocation = geoLocation;
    }

}
