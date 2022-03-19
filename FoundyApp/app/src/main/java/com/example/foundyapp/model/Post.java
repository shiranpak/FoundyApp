package com.example.foundyapp.model;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {

    final public static String COLLECTION_NAME = "posts";

    @PrimaryKey
    @NonNull
    private int postId;
    private String category;
    private String location;
    private String date;
    private Image image;
    private String description;
    private enum type {lost,found}
    private int userId;
    private boolean isDeleted;
}
