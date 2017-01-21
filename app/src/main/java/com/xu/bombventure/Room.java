package com.xu.bombventure;

import java.util.ArrayList;

/**
 * Basic Room Class,holding everything we'd need for to describe a room
 */

public class Room {

    public String description;
    public double lat;
    public double lng;
    ArrayList<String>filters;
    int maxNo;
    ArrayList<Double>coords;



    public Room(String Description, double Lat, double Lng, ArrayList<String>Filters, int MaxNo){
        this.description=Description;
        this.lat=Lat;
        this.lng= Lng;
        this.filters= Filters;
        int maxNo =MaxNo;
        coords.add(lat);
        coords.add(lng);



    }

    public ArrayList<String> getFilters(){
        return filters;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Double> getCoordinates(){
        return coords;
    }

}
