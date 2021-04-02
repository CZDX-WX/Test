package com.czdxwx.test.model;

public class Place {
    private int id;
    private String place;

    public Place() {
    }

    public Place(int id, String place) {
        this.id = id;
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
