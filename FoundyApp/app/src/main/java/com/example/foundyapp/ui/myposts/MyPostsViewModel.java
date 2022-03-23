package com.example.foundyapp.ui.myposts;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.ModelFirebase;
import com.example.foundyapp.model.Post;
import com.example.foundyapp.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MyPostsViewModel extends ViewModel {

    LiveData<List<Post>> myPostsList;
    LiveData<User> currentUser;

    public MyPostsViewModel() {
        myPostsList = Model.instance.getCurrentUserPostList();

    }

    public LiveData<List<Post>> getMyPosts() {
        return myPostsList;
    }

    public LiveData<User> getCurrentUser(){
        return currentUser;
    }



}