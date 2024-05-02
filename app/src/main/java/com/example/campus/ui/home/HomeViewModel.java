package com.example.campus.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Boolean> buyClicked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sellClicked = new MutableLiveData<>();

    public HomeViewModel() {
        // Initialize LiveData objects
        buyClicked.setValue(false);
        sellClicked.setValue(false);
    }

    // Method to expose buyClicked LiveData
    public MutableLiveData<Boolean> getBuyClicked() {
        return buyClicked;
    }

    // Method to expose sellClicked LiveData
    public MutableLiveData<Boolean> getSellClicked() {
        return sellClicked;
    }

    // Method to handle buy button click
    public void setBuyClicked() {
        // Set the value of buyClicked to true
        buyClicked.setValue(true);
    }

    // Method to handle sell button click
    public void setSellClicked() {
        // Set the value of sellClicked to true
        sellClicked.setValue(true);
    }

    // Method to reset buyClicked LiveData
    public void resetBuyClicked() {
        buyClicked.setValue(false);
    }

    // Method to reset sellClicked LiveData
    public void resetSellClicked() {
        sellClicked.setValue(false);
    }
}
