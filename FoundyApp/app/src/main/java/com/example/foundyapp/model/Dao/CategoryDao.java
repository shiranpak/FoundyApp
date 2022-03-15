package com.example.foundyapp.model.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foundyapp.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("select * from Category")
    List<Category> GetAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category category);
}
