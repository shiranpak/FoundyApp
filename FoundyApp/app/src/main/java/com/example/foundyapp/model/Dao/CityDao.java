package com.example.foundyapp.model.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foundyapp.model.City;

import java.util.List;

@Dao
public interface CityDao {

    @Query("select * from City")
    List<City> GetAllCities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(City city);
}
