package com.example.foundyapp.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    User user;


    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public String getUid(){
        return firebaseUser.getUid();
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

    public void editUser(User user, Model.EditUserListener listener)
    {
        if (checkIfLoggedIn() ){
            DocumentReference u=db.collection(User.CollectionName).document(firebaseUser.getUid());
            if(user.getImage()!=null) {
                u.update("name", user.getFullName(), "image", user.getImage());
            }
            else{
                u.update("name", user.getFullName());
            }
        }

    }

    public interface GetAllPostsListener{
        void onComplete(List<Post> list);
    }

    public void deletePost (String postId , Model.deletePostListener listener){
        DocumentReference post = db.collection(Post.COLLECTION_NAME).document(postId);
        post.update("isDeleted", true);
    }

    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Post post = Post.create(doc.getId(), doc.getData());
                            if (post != null) {
                                list.add(post);
                            }
                        }
                    }

                    listener.onComplete(list);
                }).addOnFailureListener(e -> listener.onComplete(null));
    }

    public void getCurrentUserPosts(String userId, Long lastUpdateDate, GetAllPostsListener listener) {

        db.collection(Post.COLLECTION_NAME)
                //     .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, new Timestamp(lastUpdateDate, 0))
                .whereEqualTo("user",userId)
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Post post = Post.create(doc.getId(), doc.getData());
                            if (post != null) {
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                }).addOnFailureListener(e -> listener.onComplete(null));
    }

    public void addPost(Post post, Model.AddPostListener listener) {
        Map<String, Object> json = post.toJson();
        db.collection(Post.COLLECTION_NAME)
                .document()
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }
    /**
     * Firebase Storage
     */
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public void saveImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("posts_Images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        });
                    }
                });
    }


    public void saveUserImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("user_avatars/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        });
                    }
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

    public void  userRegistration (User user, String password, Context context, Model.AddUserListener listener){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setUserId(mAuth.getUid());
                        Map<String, Object> json = user.toJson();
                        db.collection(user.CollectionName)
                                .document(user.userId)
                                .set(json).addOnSuccessListener(unused -> listener.onComplete())
                                .addOnFailureListener(e -> listener.onComplete());
                        Toast.makeText(context,
                                "Registration successful!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    else{
                        // Registration failed
                        Toast.makeText(
                                context,
                                "Registration failed!!"
                                        + " Please try again later "+task.getException().getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    public boolean checkIfLoggedIn(){
        if(mAuth.getCurrentUser()!=null) {
            firebaseUser = mAuth.getCurrentUser();
            return true;
        }
        else return false;
    }

public void SignOut(Model.signOutUserListener listener){
        mAuth.signOut();
    }

    public void getUser(String id,Model.GetUserById listener){
            db.collection(user.CollectionName).document(id).get().addOnCompleteListener(task -> {
                User user = null;
                if (task.isSuccessful() && task.getResult() != null) {
                    Object userData = task.getResult().getData();
                    if(userData!=null){
                        user = User.create(task.getResult().getData());
                    }
                }
                listener.onComplete(user);
            });
    }

    public void getCurrentUser(Model.GetUserById listener){
        if (checkIfLoggedIn()) {
            db.collection(user.CollectionName).document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
                User user = null;
                if (task.isSuccessful() & task.getResult() != null) {
                    user = User.create(task.getResult().getData());
                }
                listener.onComplete(user);

            });
        }

    }
}