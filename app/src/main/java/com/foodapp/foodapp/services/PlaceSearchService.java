package com.foodapp.foodapp.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.api.response.PlaceResponse;
import com.foodapp.foodapp.receivers.PlaceReceiver;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import static com.foodapp.foodapp.helpers.Constants.API_KEY;
import static com.foodapp.foodapp.helpers.Constants.CHECK_PLACE_ID;
import static com.foodapp.foodapp.receivers.PlaceReceiver.CHANNEL_ID;

public class PlaceSearchService extends IntentService {

    private PlacesClient placesClient;
    private final BroadcastReceiver receiver = new PlaceReceiver();

    public PlaceSearchService() {
        super("PlaceSearchService");
    }

    public PlaceSearchService(String name) {
        super(name);
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        IntentFilter filter = new IntentFilter();
//        registerReceiver(receiver, filter);
//    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), API_KEY);
        // Create a new Places client instance
        placesClient = Places.createClient(this);
        com.foodapp.foodapp.api.Place.findCurrentPlace(this, placesClient, new PlaceResponse() {
            @Override
            public void post(String name) {
//                Intent placeIntent = new Intent(PlaceSearchService.this, PlaceReceiver.class);
//                placeIntent.putExtra(PLACE_EXTRA, name);
//                sendBroadcast(placeIntent);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PlaceSearchService.this);
                String notificationTitle = "At " + name + "?";
                Notification notification = new NotificationCompat.Builder(PlaceSearchService.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(notificationTitle)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .build();
                notificationManager.notify(CHECK_PLACE_ID, notification);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void fail(Exception exception) {

            }
        });
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        unregisterReceiver(receiver);
//    }
}
