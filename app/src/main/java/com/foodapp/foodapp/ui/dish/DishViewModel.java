package com.foodapp.foodapp.ui.dish;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DishViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public DishViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dish fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
