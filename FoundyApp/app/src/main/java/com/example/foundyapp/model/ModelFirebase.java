package com.example.foundyapp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void getAllCategories(Model.GetAllDataListener listener) {
        db.collection(Category.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<Category> list = new LinkedList<Category>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Category category = Category.create(doc.getData());
                            if (category != null){
                                list.add(category);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getAllCities(Model.GetAllDataListener listener) {
        db.collection(City.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<City> list = new LinkedList<City>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            City city = City.create(doc.getData());
                            if (city != null){
                                list.add(city);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }
}