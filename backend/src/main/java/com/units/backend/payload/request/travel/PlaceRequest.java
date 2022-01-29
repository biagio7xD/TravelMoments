package com.units.backend.payload.request.travel;

import javax.validation.constraints.NotBlank;

public class PlaceRequest {
    @NotBlank
    private String address;
    @NotBlank
    private float lng;
    @NotBlank
    private float lat;
    private String transport;
    private String description;
    private String city;
    private String country;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "TravelRequest{" +
                "address='" + address + '\'' +
                ", lang=" + lng +
                ", lat=" + lat +
                ", transport='" + transport + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
