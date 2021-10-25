package com.udc.riws.ri.service.core.dto;

import java.util.Date;

public class TweetDto {
    private Long id;
    private String content;
    private UserDto user;
    private Date createdAt;
    private GeoLocationDto geolocation;
    private PlaceDto place;
    private Integer retweets;
    private Integer favorites;
    private String lang;

    public TweetDto() {
    }

    public TweetDto(long id, String text, UserDto user, Date createdAt, String lang, GeoLocationDto geoLocation, PlaceDto place, int favoriteCount, int retweetCount) {
        this.id = id;
        this.content = text;
        this.createdAt = createdAt;
        this.lang = lang;
        this.geolocation = geoLocation;
        this.favorites = favoriteCount;
        this.retweets = retweetCount;
        this.user = user;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public GeoLocationDto getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeoLocationDto geolocation) {
        this.geolocation = geolocation;
    }

    public PlaceDto getPlace() {
        return place;
    }

    public void setPlace(PlaceDto place) {
        this.place = place;
    }

    public Integer getRetweets() {
        return retweets;
    }

    public void setRetweets(Integer retweets) {
        this.retweets = retweets;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
