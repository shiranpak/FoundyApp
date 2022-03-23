package com.example.foundyapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Dao.PostDao;

import java.util.List;

public class PostViewModel extends ViewModel {
    LiveData<List<Post>> data = Model.instance.getAllPosts();
    public LiveData<List<Post>> getData() {
        return data;
    }
}
