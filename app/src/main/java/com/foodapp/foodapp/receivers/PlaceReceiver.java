package com.foodapp.foodapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.foodapp.foodapp.helpers.Constants;

public class PlaceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String placeName = (String) extras.get(Constants.PLACE_EXTRA);
            Toast.makeText(context, "Location: " + placeName, Toast.LENGTH_LONG).show();
        }
    }
}
