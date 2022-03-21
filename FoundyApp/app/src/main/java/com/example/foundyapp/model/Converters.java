package com.example.foundyapp.model;

import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Converters {
    @TypeConverter
    public String latLngToString(LatLng latLng) {
        if(latLng != null)
            return String.format("(%s,%s)", latLng.latitude,latLng.longitude);
        return null;
    }

    @TypeConverter
    public LatLng stringToLatLng(String str) {
        if (str != null) {
            String s = str.replace("(", "").replace(")", "");
            String[] list = s.split(",");
            return new LatLng(Double.parseDouble(list[0]), Double.parseDouble(list[1]));
        }
        return null;
    }
}
