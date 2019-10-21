package com.foodapp.foodapp.ui.review;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.foodapp.foodapp.R;

import static com.foodapp.foodapp.helpers.Constants.CAMERA_PIC_REQUEST;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView uploadedImagesRecyclerView;

    private Button uploadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        uploadedImagesRecyclerView = findViewById(R.id.uploadedImagesRecyclerView);

        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
//            ImageView imageview = findViewById(R.id.imgDish);
//            imageview.setImageBitmap(image);

            // Add image to horizontal RecyclerView
        }
    }
}
