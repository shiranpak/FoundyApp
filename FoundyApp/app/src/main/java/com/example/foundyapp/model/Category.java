package com.example.foundyapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Category {
    final public static String COLLECTION_NAME = "categories";

    @PrimaryKey
    @NonNull
    String name = "";

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }

    public static Category create(Map<String, Object> json) {
        String name = (String) json.get("category");

        Category category = new Category(name);
        return category;
    }
}
