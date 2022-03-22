package com.example.foundyapp.ui.myposts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.Post;

import java.util.List;

public class GalleryViewModel extends ViewModel {

    String userId;
    LiveData<List<Post>> data;

    public GalleryViewModel(){
        data = Model.instance.getAllPosts();
    }

    public LiveData<List<Post>> getData() {
        return data;
    }
}