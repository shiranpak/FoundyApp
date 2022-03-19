package com.example.foundyapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

import android.media.Image;

@Entity(tableName = "posts")
public class Post {
    final public static String COLLECTION_NAME = "posts";
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int postId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = "";
    private String category = "";
    private LatLng location;
    private Long date = new Long(0);;
    private String description = "";
    private boolean type; //true = found, false = lost
    private String userId = "";
    private boolean flag = false;
    private String imageUrl;
    private Long updateDate = new Long(0);

    public Post(){}
    public Post(@NonNull int postId, String title,String category, LatLng location, Long date, String description, boolean type, String userId, boolean flag) {
        this.postId = postId;
        this.category = category;
        this.title = title;
        this.location = location;
        this.date = date;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.flag = flag;
    }

    @NonNull
    public int getPostId() {
        return postId;
    }

    public void setPostId(@NonNull int postId) {
        this.postId = postId;
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


    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",postId);
        json.put("category",category);
        json.put("title",title);
        json.put("description",description);
        json.put("location",location);
        json.put("date",date);
        json.put("type",type);
        json.put("user",userId);
        json.put("flag",flag);
        json.put("updateDate", FieldValue.serverTimestamp());
        json.put("imageUrl",imageUrl);
        return json;
    }

    public static Post create(Map<String, Object> json) {
        int id = (int) json.get("id");
        String category = (String) json.get("category");
        String title = (String) json.get("title");
        String description = (String) json.get("description");
        String user = (String) json.get("user");
        LatLng location = (LatLng) json.get("location");
        Boolean flag = (Boolean) json.get("flag");
        Boolean type = (Boolean) json.get("type");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Timestamp dateTs = (Timestamp)json.get("date");
        Long updateDate = ts.getSeconds();
        Long date = dateTs.getSeconds();
        String avatarUrl = (String)json.get("imageUrl");

        Post student = new Post(id,title,category,location,date,description,type,user,flag);
        student.setUpdateDate(updateDate);
        student.setImageUrl(avatarUrl);
        return student;
    }
}

