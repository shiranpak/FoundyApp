package com.example.foundyapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

@Entity
public class City {
    final public static String COLLECTION_NAME = "cities";

    @PrimaryKey
    @NonNull
    String name = "";
    @Ignore
    private LatLng location = null;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
    public City(){}
    public City(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    public static City create(Map<String, Object> data) {
        String name = (String) data.get("city");
        double latitude = Double.parseDouble((String) data.get("lat"));
        double longitude = Double.parseDouble((String) data.get("lng"));
        LatLng location = new LatLng(latitude,longitude);

        City city = new City(name,location);
        return city;
    }
}
