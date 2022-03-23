package com.example.foundyapp.ui.MyDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foundyapp.model.Model;

public class MyDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> fullName;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> image;

    public MyDetailsViewModel() {
        fullName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        image = new MutableLiveData<>();


            fullName.setValue(Model.instance.getCurrentUser().getValue().getFullName());
            email.setValue(Model.instance.getCurrentUser().getValue().getEmail());
            String imageUrl = Model.instance.getCurrentUser().getValue().getImage();
            if (imageUrl!=null){
                image.setValue(imageUrl);
            }
    }

    public LiveData<String> getFullName() { return fullName;}

    public LiveData<String> getImage() {
        return image;
    }

    public LiveData<String> getEmail() { return email; }
}