package com.marcosevaristo.tcc001.model;

public class Carro {
    private String id;
    private Linha linha;
    private String longitude;
    private String latitude;
    private String location;

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

    public Linha getLinha() {
        return linha;
    }

    public void setLinha(Linha linha) {
        this.linha = linha;
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
}
