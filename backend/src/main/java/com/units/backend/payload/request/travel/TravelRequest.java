package com.units.backend.payload.request.travel;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class TravelRequest {

    @NotBlank
    private String date;
    @NotBlank
    private List<List<Float>> latAndLng;
    private String transport;
    private String routeInfo;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<List<Float>> getLatAndLng() {
        return latAndLng;
    }

    public void setLatAndLng(List<List<Float>> latAndLng) {
        this.latAndLng = latAndLng;
    }
}
