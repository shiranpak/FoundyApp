package com.example.foundyapp.model.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foundyapp.model.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from posts")
    List<Post> GetAllPosts();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post... posts);
    @Query("DELETE FROM posts")
    void removeAll();
}
