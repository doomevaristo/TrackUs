package com.marcosevaristo.tcc001.model;

public class StepsObject {

    private DistanceObject distance;
    private DurationObject duration;
    private String location;

    public DistanceObject getDistance() {
        return distance;
    }

    public void setDistance(DistanceObject distance) {
        this.distance = distance;
    }

    public DurationObject getDuration() {
        return duration;
    }

    public void setDuration(DurationObject duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
