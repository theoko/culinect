package com.foodapp.foodapp.ui.review;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.adapters.UploadedImageAdapter;
import com.foodapp.foodapp.api.response.PlaceResponse;
import com.foodapp.foodapp.models.ImageItemModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.foodapp.foodapp.helpers.Constants.API_KEY;
import static com.foodapp.foodapp.helpers.Constants.CAMERA_PIC_REQUEST;
import static com.foodapp.foodapp.helpers.Constants.FOOD_ITEM_CONFIDENCE_LEVEL;

public class ReviewActivity extends AppCompatActivity {

    // Places API
    private PlacesClient placesClient;

    private EditText restaurantEditText;
    private EditText dishEditText;

    private ArrayList<ImageItemModel> imageItemModelArrayList;
    private RecyclerView uploadedImagesRecyclerView;

    private Button uploadBtn;
    private UploadedImageAdapter uploadedImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        restaurantEditText = findViewById(R.id.restaurant);
        dishEditText = findViewById(R.id.dish);

        uploadedImagesRecyclerView = findViewById(R.id.uploadedImagesRecyclerView);

        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        initializePhotosList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlacesAPI();
        findCurrentPlace();
    }

    private void initializePlacesAPI() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), API_KEY);

        // Create a new Places client instance
        placesClient = Places.createClient(this);
    }

    private void findCurrentPlace() {
        ReviewActivity.this.setLoading(true);

        com.foodapp.foodapp.api.Place.findCurrentPlace(ReviewActivity.this, placesClient, new PlaceResponse() {
            @Override
            public void post(String name) {
                updateRestaurantName(name);
            }

            @Override
            public void onComplete() {
                ReviewActivity.this.setLoading(false);
            }

            @Override
            public void fail(Exception exception) {
                Log.e(getClass().getName(), Objects.requireNonNull(exception.getMessage()));
            }
        });
    }

    private void detectDish(Bitmap bitmap) {
        // Detect food labels
        final FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getCloudImageLabeler();

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);

        labeler.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        // Task completed successfully
                        FirebaseVisionImageLabel firebaseVisionImageLabelWithHighestConfidence = null;
                        for (FirebaseVisionImageLabel firebaseVisionImageLabel : labels) {
                            // Check against confidence level
                            if (firebaseVisionImageLabel.getConfidence() > FOOD_ITEM_CONFIDENCE_LEVEL) {
                                firebaseVisionImageLabelWithHighestConfidence = firebaseVisionImageLabel;
                            }
                            if (firebaseVisionImageLabelWithHighestConfidence != null) {
                                // Check if we have an item with higher confidence
                                if (firebaseVisionImageLabelWithHighestConfidence.getConfidence() < firebaseVisionImageLabel.getConfidence()) {
                                    firebaseVisionImageLabelWithHighestConfidence = firebaseVisionImageLabel;
                                }
                            }
                            Log.e(getClass().getName(), "Firebase vision image label: " + firebaseVisionImageLabel.getText());
                        }

                        if (firebaseVisionImageLabelWithHighestConfidence != null) {
                            updateDishName(firebaseVisionImageLabelWithHighestConfidence.getText());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        Log.e(getClass().getName(), "Firebase vision image label exception: " + e.getMessage());
                    }
                });
    }

    private void setLoading(boolean loading) {

    }

    private List<Place.Field> getPlaceFields() {
        List<Place.Field> fieldList = new ArrayList<>();
        fieldList.add(Place.Field.NAME);
        fieldList.add(Place.Field.ADDRESS);
        fieldList.add(Place.Field.LAT_LNG);
        fieldList.add(Place.Field.TYPES);
        return fieldList;
    }

    private void updateRestaurantName(String name) {
        restaurantEditText.setText(name);
    }

    private void updateDishName(String name) {
        dishEditText.setText(name);
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    private void initializePhotosList() {
        imageItemModelArrayList = new ArrayList<>();
    }

    private void updatePhotoItems() {
        uploadedImageAdapter = new UploadedImageAdapter(ReviewActivity.this, imageItemModelArrayList);
        uploadedImagesRecyclerView.setAdapter(uploadedImageAdapter);
        uploadedImagesRecyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            // Get image from camera
            Bitmap image = (Bitmap) data.getExtras().get("data");
            detectDish(image);
            ImageItemModel imageItem = new ImageItemModel(image);

            // Add image to horizontal RecyclerView
            imageItemModelArrayList.add(imageItem);
            updatePhotoItems();
        }
    }
}
