package com.example.foundyapp.model.Dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.foundyapp.model.Post;

@Dao
public interface PostDao {

    @Insert
    void insert(Post post);
}
