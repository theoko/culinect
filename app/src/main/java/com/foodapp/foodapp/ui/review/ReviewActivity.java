package com.foodapp.foodapp.ui.review;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.adapters.NearbyAdapter;
import com.foodapp.foodapp.adapters.UploadedImageAdapter;
import com.foodapp.foodapp.models.FoodItemModel;
import com.foodapp.foodapp.models.ImageItemModel;

import java.util.ArrayList;

import static com.foodapp.foodapp.helpers.Constants.CAMERA_PIC_REQUEST;

public class ReviewActivity extends AppCompatActivity {

    private ArrayList<ImageItemModel> imageItemModelArrayList;
    private RecyclerView uploadedImagesRecyclerView;

    private Button uploadBtn;
    private UploadedImageAdapter uploadedImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

//        ImageView imageview = findViewById(R.id.imgDish);

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
            ImageItemModel imageItem = new ImageItemModel(image);

            // Add image to horizontal RecyclerView
            imageItemModelArrayList.add(imageItem);
            updatePhotoItems();
        }
    }
}
