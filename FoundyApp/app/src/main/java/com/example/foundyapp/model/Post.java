package com.example.foundyapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foundyapp.MyApplication;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "posts")
public class Post {
    final public static String COLLECTION_NAME = "posts";
    public final static String LAST_UPDATED = "lastUpdated";
    final static String POSTS_LAST_UPDATE = "POSTS_LAST_UPDATE";

    @PrimaryKey
    @NonNull
    private String postId;
    private String title;
    private String category;
    private LatLng location = null;
    private Long date = new Long(0);
    private String description;
    private boolean type; //true = found, false = lost
    private String userId;
    private boolean isDeleted = false;
    private String imageUrl;
    private Long lastUpdated = new Long(0);

    public Post(){}


    public Post(@NonNull String postid, String title, String category, LatLng location, Long date, String description, boolean type, String userId, boolean isDeleted , Long lastUpdated) {
        this.postId = postid;
        this.category = category;
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
    }
    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("category",category);
        json.put("title",title);
        json.put("description",description);
        json.put("location",location);
        json.put("date",date);
        json.put("type",type);
        json.put("user",userId);
        json.put("isDeleted",isDeleted);
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        json.put("imageUrl",imageUrl);
        return json;
    }

    public static Post create(String postId, Map<String, Object> json) {
        String id = postId;
        String category = (String) json.get("category");
        String title = (String) json.get("title");
        String description = (String) json.get("description");
        String user = (String) json.get("user");
        HashMap<String, Double> data = (HashMap<String, Double>) json.get("location");
        LatLng location = null;
        if(data != null) {
            double latitude = data.get("latitude");
            double longitude = data.get("longitude");
            location = new LatLng(latitude, longitude);
        }
        Boolean isDeleted = (Boolean) json.get("isDeleted");
        Boolean type = (Boolean) json.get("type");
        Long date = (long)json.get("date");
        String imageUrl = (String)json.get("imageUrl");
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        Long lastUpdated = new Long(ts.getSeconds());

        Post post = new Post(id, title,category,location,date,description,type,user,isDeleted,lastUpdated);
        post.setImageUrl(imageUrl);
        return post;
    }

    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong(POSTS_LAST_UPDATE,0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong(POSTS_LAST_UPDATE,date);
        editor.commit();
    }

}

