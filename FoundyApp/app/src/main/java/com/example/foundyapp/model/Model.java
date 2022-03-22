package com.example.foundyapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foundyapp.MyApplication;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Model {

    private Model(){}
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    ModelFirebase modelFirebase = new ModelFirebase();

    public void removeAllPosts() {
        executor.execute(() -> AppLocalDb.db.postDao().removeAll());
    }

    public interface GetAllDataListener{
        void onComplete(List<?> list);
    }
    public interface AddUserListener{
        void onComplete();
    }
    public interface  GetUserById{
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
    public interface  EditUserListener{
        void onComplete();
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
        return postsList;
    }
    public interface searchForPostsListener{
        void onComplete(List<Post> posts);
    }
    public void searchForPosts(String sCity, String sCategory, boolean sType, long sStartDate, long sEndDate , searchForPostsListener listener)
    {
        if (postsList.getValue() == null || postsList.getValue().size() == 0) {
            MyApplication.mainHandler.post(()->{
                listener.onComplete(null);
            });
            return;
        }else {
            MyApplication.executorService.execute(() -> {
                List<Post> filteredPosts = postsList.getValue().stream().
                        filter(post -> post.isType() == sType
                                && ((post.getLocation() != null) && (post.getLocation().longitude != 0) && getCityOfPost(post.getLocation()).equals(sCity))
                                && post.getCategory().equals(sCategory)
                                && (post.getDate() >= sStartDate && post.getDate() <= sEndDate)).collect(Collectors.toList());

                MyApplication.mainHandler.post(() -> {
                    listener.onComplete(filteredPosts);
                });
            });

        }
    }
    Geocoder gcd = new Geocoder(MyApplication.getContext(), Locale.getDefault());
    public String getCityOfPost(LatLng location){
        if(location != null){
            double lat = location.latitude;
            double lng = location.longitude;
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(lat, lng, 1);

                if (addresses.size() > 0) {
                    return addresses.get(0).getLocality();
                }
                else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
    public void refreshPostsList() {
        postListLoadingState.setValue(ListLoadingState.loading);

        // get last local update date
        Long localLastUpdate = Post.getLocalLastUpdated();

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllPosts(localLastUpdate, list -> {
            // add all records to the local db
            executor.execute(() -> {
                Long lud = 0L;
                for (Post post : list) {
                    AppLocalDb.db.postDao().insert(post);
                    // if the post deleted in the firebase, delete him from the local db
                    if (post.getIsDeleted())
                        AppLocalDb.db.postDao().delete(post);
                    if (post.getLastUpdated() > lud) {
                        lud = post.getLastUpdated();
                    }
                }
                // update last local update date
                Post.setLocalLastUpdated(lud);

                //return all data to caller
                List<Post> ptList = AppLocalDb.db.postDao().GetAllPosts();
                postsList.postValue(ptList);
                postListLoadingState.postValue(ListLoadingState.loaded);
            });
        });
    }

    public void saveUserImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveUserImage(imageBitmap, imageName, listener);
    }
    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }
    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post, () -> {
            listener.onComplete();
            refreshPostsList();
        });

    }
    public void getUser (GetUserById listener) {
        modelFirebase.getUser(listener);
    }
    public void editUser( User user,EditUserListener listener){ modelFirebase.editUser( user,listener); }

}
