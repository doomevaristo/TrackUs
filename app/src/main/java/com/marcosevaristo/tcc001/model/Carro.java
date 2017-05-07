package com.marcosevaristo.tcc001.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Carro implements Serializable{
    private String id;
    private String longitude;
    private String latitude;
    private String location;

    private static final long serialVersionUID = 1L;

    public Carro() {
    }

    public Carro(String id, String longitude, String latitude, String location) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Carro> converteMapParaListCarros(Map mapCarros) {
        return null;
    }
}
