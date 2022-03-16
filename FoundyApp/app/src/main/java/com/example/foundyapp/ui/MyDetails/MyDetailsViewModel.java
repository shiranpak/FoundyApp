package com.example.foundyapp.ui.MyDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyDetailsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyDetailsViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}