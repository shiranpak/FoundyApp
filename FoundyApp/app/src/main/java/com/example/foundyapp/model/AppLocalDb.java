package com.example.foundyapp.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foundyapp.MyApplication;
import com.example.foundyapp.model.Dao.CategoryDao;
import com.example.foundyapp.model.Dao.CityDao;
import com.example.foundyapp.model.Dao.PostDao;

@Database(entities = {Category.class,City.class, Post.class}, version = 6)
@TypeConverters(value = {Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract CityDao cityDao();
    public abstract PostDao postDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "FoundyLocalDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
