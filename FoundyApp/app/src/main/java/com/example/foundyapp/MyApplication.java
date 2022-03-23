package com.example.foundyapp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//המחלקה הבסיסית, נוצרת כשאנדרואיד מפעיל את האפליקציה
public class MyApplication extends Application {
    static Context context;
    public static ExecutorService executorService = Executors.newFixedThreadPool(1);
    final public static Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
