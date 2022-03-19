package com.example.foundyapp.model;

import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Converters {
    @TypeConverter
    public String latLngToString(LatLng latLng) {
        return "(${latLng.latitude},${latLng.longitude}";
    }

    @TypeConverter
    public LatLng stringToLatLng(String str){
        String s = str.replace("(", "").replace(")", "");
        String[] list = s.split(",");
        return new LatLng(Double.parseDouble(list[0]), Double.parseDouble(list[1]));
    }
}
