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
    String id = "";
    String name = "";
    boolean flag = false;
    Long updateDate = new Long(0);
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Category(String name, String id, boolean flag) {
        this.name = name;
        this.id = id;
        this.flag = flag;
    }
    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public static Category create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String name = (String) json.get("name");
        Boolean flag = (Boolean) json.get("flag");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Long updateDate = ts.getSeconds();

        Category category = new Category(name,id,flag);
        category.setUpdateDate(updateDate);
        return category;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("id",id);
        json.put("name",name);
        json.put("flag",flag);
        json.put("updateDate", FieldValue.serverTimestamp());
        return json;
    }

    public Long getUpdateDate() {
        return updateDate;
    }
}
