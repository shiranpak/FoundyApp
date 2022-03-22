package com.example.foundyapp.model;

import android.graphics.Bitmap;

import com.example.foundyapp.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class User {
    final public static String CollectionName="users";
    String userId;
    String fullName;
    String email;
    String image;

    public User (String fullName,String email,String userId){

        this.userId=userId;
        this.email=email.toLowerCase();
        this.fullName=fullName;
        this.image=null;
    }
    public User (String fullName,String email,String image,String userId) {
        this.userId=userId;
        this.email=email.toLowerCase();
        this.fullName=fullName;
        this.image=image;

    }
        public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImage() {
        return image;
    }

    public String getUserId() {
        return userId;
    }

    public static User create(Map<String, Object> json) {
        String id= (String) json.get("id");
        String name=(String) json.get("name");
        String email=(String) json.get("email");
        String image=(String) json.get("image");
        User user= new User(name,email,image,id);
        return user;
    }
    public Map<String, Object> toJson() {
        Map<String,Object> Json = new HashMap<String,Object>();
        Json.put("id",userId);
        Json.put("name",fullName);
        Json.put("email",email);
        Json.put("image",image);
        return  Json;
    }

}
