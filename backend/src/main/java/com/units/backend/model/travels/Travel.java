package com.units.backend.model.travels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Document(collection = "travels")
public class Travel {
    @Id
    private String id;
    private String idUser;
    private String date;
    private String transport;
    private List<List<Float>> latAndLng;
    private String routeInfo;
    public Travel() {

    }

    public Travel(String idUser, String date, List<List<Float>> latAndLng, String transport, String routeInfo) {
        this.idUser = idUser;
        this.date = parseDate(date);
        this.latAndLng = latAndLng;
        this.transport = transport;
        this.routeInfo = routeInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = parseDate(date);
    }

    public List<List<Float>> getLatAndLng() {
        return latAndLng;
    }

    public void setLatAndLng(List<List<Float>> latAndLng) {
        this.latAndLng = latAndLng;
    }

    private String parseDate(String date) {
        return date.length() <= 10 ? date : ZonedDateTime.parse(date).plusDays(1).toLocalDate().toString();
    }
}
