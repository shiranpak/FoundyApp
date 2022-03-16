package com.example.foundyapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity
public class City {
    final public static String COLLECTION_NAME = "cities";

    @PrimaryKey
    @NonNull
    String name = "";

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public City(String name) {
        this.name = name;
    }

    public static City create(Map<String, Object> data) {
        String name = (String) data.get("city");

        City city = new City(name);
        return city;
    }
}
