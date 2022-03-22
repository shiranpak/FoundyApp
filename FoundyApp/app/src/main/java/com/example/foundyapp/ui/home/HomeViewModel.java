package com.example.foundyapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;

import java.util.List;

public class HomeViewModel extends ViewModel {

    LiveData<List<Post>> data;

    public HomeViewModel(){
        data = Model.instance.getAllPosts();
    }

    public LiveData<List<Post>> getData() {
        return data;
    }
}