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

    public enum ListLoadingState {
        loading,
        loaded
    }
    MutableLiveData<ListLoadingState> categoryListLoadingState = new MutableLiveData<ListLoadingState>();

    MutableLiveData<List<Category>> categoriesList = new MutableLiveData<List<Category>>();

    public LiveData<List<Category>> getAllCategories() {

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("CategoriesUpdateDate", 0);

        if (categoriesList.getValue() == null) {
            modelFirebase.getAllCategories(lastUpdateDate, new ModelFirebase.GetAllCategoriesListener() {
                @Override
                public void onComplete(List<Category> list) {
                    // add all records to the local db
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Long lud = new Long(0);
                            Log.d("TAG", "fb returned " + list.size());
                            for (Category category : list) {
                                AppLocalDb.db.categoryDao().insertAll(category);
                                if (lud < category.getUpdateDate()) {
                                    lud = category.getUpdateDate();
                                }
                            }
                            // update last local update date
                            MyApplication.getContext()
                                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                    .edit()
                                    .putLong("StudentsLastUpdateDate", lud)
                                    .commit();

                            //return all data to caller
                            List<Category> stList = AppLocalDb.db.categoryDao().GetAllCategories();
                            categoriesList.postValue(stList);
                            categoryListLoadingState.postValue(ListLoadingState.loaded);
                        }
                    });
                }
            });
        }
        return categoriesList;
    }
}
