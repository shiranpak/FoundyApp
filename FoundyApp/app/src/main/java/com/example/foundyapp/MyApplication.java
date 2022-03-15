package com.example.foundyapp;

import android.app.Application;
import android.content.Context;

//המחלקה הבסיסית, נוצרת כשאנדרואיד מפעיל את האפליקציה
public class MyApplication extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
