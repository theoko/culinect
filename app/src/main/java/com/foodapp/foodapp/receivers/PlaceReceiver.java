package com.foodapp.foodapp.receivers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.foodapp.foodapp.helpers.Constants;

import static com.foodapp.foodapp.helpers.Constants.CHECK_PLACE_ID;

public class PlaceReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "place_check_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String placeName = (String) extras.get(Constants.PLACE_EXTRA);
            Toast.makeText(context, "Location: " + placeName, Toast.LENGTH_LONG).show();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            String notificationTitle = "At " + placeName + "?";
            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(notificationTitle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .build();
            notificationManager.notify(CHECK_PLACE_ID, notification);
        }
    }
}
