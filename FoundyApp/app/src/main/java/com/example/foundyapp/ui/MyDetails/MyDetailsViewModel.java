package com.example.foundyapp.ui.MyDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Model;
import com.example.foundyapp.model.User;

public class MyDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> fullName;
    private final MutableLiveData<String> email;
    public MyDetailsViewModel() {
        fullName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        Model.instance.getUser(user -> {
            fullName.setValue(user.getFullName());
            email.setValue(user.getEmail());
        });
    }

    public LiveData<String> getFullName() {
        return fullName;
    }

    public LiveData<String> getEmail() {
        return email;
    }
}