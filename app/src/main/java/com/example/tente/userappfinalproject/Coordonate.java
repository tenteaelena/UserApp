package com.example.tente.userappfinalproject;

/**
 * Created by student on 11/7/2017.
 */

public class Coordonate {
    public double longitudine = -1;
    public double latitudine = -1;
    public String locationName = "";

    public Coordonate(double longitudine, double latitudine, String locationName){
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.locationName = locationName;
    }
}
