package com.example.foundyapp.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.ModelFirebase;
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