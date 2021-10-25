package com.udc.riws.ri.service.core.dto;

public class UserDto {
    private Long id;
    private String name;
    private String location;
    private String lang;
    private String email;

    public UserDto() {
    }

    public UserDto(long id, String name, String email, String lang, String location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.location = location;
        this.lang = lang;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
