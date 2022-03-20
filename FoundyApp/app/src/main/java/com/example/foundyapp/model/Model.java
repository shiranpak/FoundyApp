package com.example.foundyapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foundyapp.MyApplication;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    private Model(){}
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    ModelFirebase modelFirebase = new ModelFirebase();

    public interface GetAllDataListener{
        void onComplete(List<?> list);
    }
    public interface AddUserListener{
        void onComplete();
    }
    public interface  GetUserByMail{
        void onComplete(User user);
    }

    public void getAllData(GetAllDataListener listener){
        modelFirebase.getAllCategories(listener);
        modelFirebase.getAllCities(listener);
    }
    public void getCategories(GetAllDataListener listener){
        modelFirebase.getAllCategories(listener);
    }

    public void addUser(User user, String pass, Context context,AddUserListener listener) {
        modelFirebase.userRegistration(user,pass,context,listener);

    }
    public interface AddPostListener {
        void onComplete();
    }
    public interface SaveImageListener {
        void onComplete(String url);
    }
    public enum ListLoadingState {
        loading,
        loaded
    }
    public LiveData<ListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }
    MutableLiveData<ListLoadingState> postListLoadingState = new MutableLiveData<ListLoadingState>();
    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();
    public LiveData<List<Post>> getAllPosts() {
        if (postsList.getValue() == null) {
            refreshPostsList();
        }
        ;
        return postsList;
    }
    public void refreshPostsList(){
        postListLoadingState.setValue(ListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("StudentsLastUpdateDate", 0);
        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllPosts(lastUpdateDate, new ModelFirebase.GetAllPostsListener() {
        @Override
        public void onComplete(List<Post> list) {
            // add all records to the local db
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Long lud = new Long(0);
                    for (Post post : list) {
                        AppLocalDb.db.postDao().insert(post);
                        if (lud < post.getUpdateDate()) {
                            lud = post.getUpdateDate();
                        }
                    }
                    // update last local update date
                    MyApplication.getContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("PostsLastUpdateDate", lud)
                            .commit();

                    //return all data to caller
                    List<Post> ptList = AppLocalDb.db.postDao().GetAllPosts();
                    postsList.postValue(ptList);
                    postListLoadingState.postValue(ListLoadingState.loaded);
                }
            });
        }
    });
}

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }
    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post, () -> {
            listener.onComplete();
//            refreshPostsList();
        });

    }
    public void getUser (GetUserByMail listener) {
        modelFirebase.getUser(listener);
    }
}
