package com.example.sunnyweather.logic.model;

import java.util.List;

public class PlaceResponse {
    private String status;
    private List<Place> places;

    public PlaceResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
