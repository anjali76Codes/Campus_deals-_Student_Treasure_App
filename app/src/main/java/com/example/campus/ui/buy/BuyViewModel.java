package com.example.campus.ui.buy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BuyViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BuyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Buy fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}