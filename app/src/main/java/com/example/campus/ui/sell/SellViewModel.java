package com.example.campus.ui.sell;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SellViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public  SellViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sell fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}