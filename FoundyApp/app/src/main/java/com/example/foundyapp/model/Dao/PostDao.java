package com.example.foundyapp.model.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foundyapp.model.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from Post")
    List<Post> GetAllPosts();
    @Insert
    void insert(Post post);

}
