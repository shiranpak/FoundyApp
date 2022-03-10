package com.example.foundyapp.model;

public class Model {
    ModelFirebase modelFirebase=new ModelFirebase();
    private Model(){ }

    public interface AddUserListenner{
        void onComplete();
    }

}
