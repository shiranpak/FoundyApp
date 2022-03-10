package com.example.foundyapp.model;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

//    public  void  Register (User user,Model.AddUserListenner listenner )
//    {
//        Map<String, Object> json = user.toJson();
//        // Add a new document with a generated ID
//        db.collection(user.CollectionName)
//                .document(user.getUid())
//                .set(json).addOnSuccessListener(unused -> listenner.onComplete())
//                .addOnFailureListener(e -> listenner.onComplete());
//    }


}
