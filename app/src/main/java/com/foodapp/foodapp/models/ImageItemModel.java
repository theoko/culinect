package com.foodapp.foodapp.models;

import android.graphics.Bitmap;

public class ImageItemModel {
    Bitmap foodItemImage;

    public ImageItemModel(Bitmap foodItemImage) {
        this.foodItemImage = foodItemImage;
    }

    public Bitmap getFoodItemImage() {
        return foodItemImage;
    }

    public void setFoodItemImage(Bitmap foodItemImage) {
        this.foodItemImage = foodItemImage;
    }
}
