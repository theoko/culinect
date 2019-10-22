package com.foodapp.foodapp.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.foodapp.foodapp.api.response.PlaceResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

import static com.foodapp.foodapp.helpers.Constants.FOOD_PLACE_CONFIDENCE_LEVEL;

public class Place {

    public static void findCurrentPlace(final Context context, PlacesClient placesClient, final PlaceResponse placeResponse) {
        FindCurrentPlaceRequest currentPlaceRequest =
                FindCurrentPlaceRequest.newInstance(getPlaceFields());
        Task<FindCurrentPlaceResponse> currentPlaceTask =
                placesClient.findCurrentPlace(currentPlaceRequest);

        currentPlaceTask.addOnSuccessListener(
                new OnSuccessListener<FindCurrentPlaceResponse>() {
                    @Override
                    public void onSuccess(FindCurrentPlaceResponse response) {
                        List<PlaceLikelihood> placeLikelihoods = response.getPlaceLikelihoods();
                        List<PlaceLikelihood> foodPlaceLikelihoods = new ArrayList<>();
                        for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
                            List<com.google.android.libraries.places.api.model.Place.Type> types = placeLikelihood.getPlace().getTypes();
                            for (com.google.android.libraries.places.api.model.Place.Type placeType : types) {
                                if (
                                        placeType == com.google.android.libraries.places.api.model.Place.Type.BAKERY ||
                                                placeType == com.google.android.libraries.places.api.model.Place.Type.CAFE ||
                                                placeType == com.google.android.libraries.places.api.model.Place.Type.RESTAURANT ||
                                                placeType == com.google.android.libraries.places.api.model.Place.Type.FOOD
                                ) {
                                    foodPlaceLikelihoods.add(placeLikelihood);
                                }
                            }
                        }

                        for (PlaceLikelihood placeLikelihood : foodPlaceLikelihoods) {
                            if (placeLikelihood.getLikelihood() > FOOD_PLACE_CONFIDENCE_LEVEL) {
                                placeResponse.post(placeLikelihood.getPlace().getName());
                            }
                        }
                    }
                }
        );

        currentPlaceTask.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                        placeResponse.fail(exception);
                        Log.e(context.getClass().getName(), "RESPONSE EXCEPTION: " + exception.getMessage());
                    }
                });

        currentPlaceTask.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                placeResponse.onComplete();
            }
        });
    }

    private static List<com.google.android.libraries.places.api.model.Place.Field> getPlaceFields() {
        List<com.google.android.libraries.places.api.model.Place.Field> fieldList = new ArrayList<>();
        fieldList.add(com.google.android.libraries.places.api.model.Place.Field.NAME);
        fieldList.add(com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
        fieldList.add(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
        fieldList.add(com.google.android.libraries.places.api.model.Place.Field.TYPES);
        return fieldList;
    }
}
