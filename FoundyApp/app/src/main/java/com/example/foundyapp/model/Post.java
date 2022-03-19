package com.example.foundyapp.model;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity(tableName = "posts")
public class Post {

    final public static String COLLECTION_NAME = "posts";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int postId;
    private String category;
    private String location;
    private String date;
    private Image image;

    @NonNull
    private String description;
    private enum type {lost,found}
    private int userId;
    private boolean isDeleted=false;

    public int getPostId() {
        return postId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}
