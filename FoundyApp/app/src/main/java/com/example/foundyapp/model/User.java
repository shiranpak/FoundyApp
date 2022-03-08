package com.example.foundyapp.model;

public class User {
    final public static String CollectionName="users";
    String name;
    String email;
    //String password;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
