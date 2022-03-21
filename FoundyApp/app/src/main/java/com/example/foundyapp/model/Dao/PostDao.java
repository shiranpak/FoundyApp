package com.example.foundyapp.model.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foundyapp.model.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("select * from posts")
    List<Post> GetAllPosts();

    @Query("select * from posts where `isDeleted` = 0")
    List<Post> GetAllActivePosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post... posts);
    @Delete
    void delete(Post post);
    @Query("DELETE FROM posts")
    void removeAll();
}
