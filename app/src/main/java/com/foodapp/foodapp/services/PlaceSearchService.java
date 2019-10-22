package com.foodapp.foodapp.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.foodapp.foodapp.api.response.PlaceResponse;
import com.foodapp.foodapp.receivers.PlaceReceiver;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import static com.foodapp.foodapp.helpers.Constants.API_KEY;
import static com.foodapp.foodapp.helpers.Constants.PLACE_EXTRA;

public class PlaceSearchService extends IntentService {

    private PlacesClient placesClient;

    public PlaceSearchService() {
        super("PlaceSearchService");
    }

    public PlaceSearchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), API_KEY);
        // Create a new Places client instance
        placesClient = Places.createClient(this);
        com.foodapp.foodapp.api.Place.findCurrentPlace(this, placesClient, new PlaceResponse() {
            @Override
            public void post(String name) {
                Intent placeIntent = new Intent(PlaceSearchService.this, PlaceReceiver.class);
                placeIntent.putExtra(PLACE_EXTRA, name);
                sendBroadcast(placeIntent);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void fail(Exception exception) {

            }
        });
    }
}
