package com.units.backend.model.travels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Document(collection = "places")
public class Place {


    @Id
    private String id;
    private String idUser;
    private String idTravel;
    private float lng;
    private float lat;
    private String description;
    private String address;
    private String city;
    private String country;
    private String date;

    public Place(String idUser, String idTravel, float lng, float lat, String description, String address, String city, String country, String date) {
        this.idUser = idUser;
        this.lng = lng;
        this.lat = lat;
        this.description = description;
        this.address = address;
        this.city = city;
        this.country = country;
        this.date = parseDate(date);
        this.idTravel = idTravel;
    }

    public Place() {

    }

    @Override
    public String toString() {
        return "Travel{" +
                "_id=" + id +
                ", idUser='" + idUser + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", idTravel=" + idTravel +
                ", date='" + date + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = parseDate(date);
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String get_id() {
        return id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String parseDate(String date) {
        return date.length() <= 10 ? date : ZonedDateTime.parse(date).plusDays(1).toLocalDate().toString();
    }

}