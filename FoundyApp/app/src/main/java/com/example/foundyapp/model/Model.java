package com.example.foundyapp.model;

import android.content.Context;
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

    public void getAllData(GetAllDataListener listener){
        modelFirebase.getAllCategories(listener);
        modelFirebase.getAllCities(listener);
    }
    public void getCategories(GetAllDataListener listener){
        modelFirebase.getAllCategories(listener);
    }
}
