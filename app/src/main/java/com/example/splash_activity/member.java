package com.example.splash_activity;

public class member {
    String name;
    String url;
    String id;
    String email;
    String phone;
    String details;
    String category;
    String wage;
    Double latitude,longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public member() {

    }

    public member(String name, String url, String id, String email, String phone, String details, String category,String wage, Double latitude, Double longitude) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.details = details;
        this.category = category;
        this.wage=wage;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getUrl() {
        return url;
    }
    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
