package com.example.foundyapp.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foundyapp.MyApplication;
import com.example.foundyapp.model.Dao.CategoryDao;

@Database(entities = {Category.class,City.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract CategoryDao categoryDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "FoundyLocalDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
