package com.example.foundyapp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.foundyapp.DrawerActivity;
import com.example.foundyapp.LoginActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

    /**Authentication**/

    public void loginUser(String email, String password, Context context){

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context,
                        "Hello!",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                // sign-in failed
                try {
                    throw task.getException();
                }
                // if user enters wrong email.
                catch (FirebaseAuthInvalidUserException invalidEmail) {
                    Toast.makeText(context,
                            "Email is not exist",
                            Toast.LENGTH_LONG)
                            .show();
                }
                // if user enters wrong password.
                catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                    Toast.makeText(context,
                            "Wrong password",
                            Toast.LENGTH_LONG)
                            .show();
                } catch (Exception e) {
                    Toast.makeText(context,
                            "Failed to login",
                            Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    public void  userRegistration (String email, String password, Context context){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context,
                                "Registration successful!",
                                Toast.LENGTH_LONG)
                                .show();
                        // if the user created intent to login activity
                        Intent intent=new Intent(context,LoginActivity.class);
                        context.startActivity(intent);

                    }
                    else{
                        // Registration failed
                        Toast.makeText(
                                context,
                                "Registration failed!!"
                                        + " Please try again later",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

    }

    public boolean checkIfLoggedIn(){
        if(mAuth.getCurrentUser()!=null)
            return  true;
        else return false;
    }



}