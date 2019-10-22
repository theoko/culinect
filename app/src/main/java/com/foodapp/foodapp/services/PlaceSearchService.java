package com.foodapp.foodapp.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class PlaceSearchService extends IntentService {
    public PlaceSearchService() {
        super("PlaceSearchService");
    }

    public PlaceSearchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        
    }
}
