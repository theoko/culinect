package com.foodapp.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.models.ImageItemModel;

import java.util.ArrayList;

public class UploadedImageAdapter extends RecyclerView.Adapter<UploadedImageAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<ImageItemModel> imageModelArrayList;

    public UploadedImageAdapter(Context ctx, ArrayList<ImageItemModel> imageModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public UploadedImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_card_no_title, parent, false);
        UploadedImageAdapter.MyViewHolder holder = new UploadedImageAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(UploadedImageAdapter.MyViewHolder holder, int position) {
        holder.iv.setImageBitmap(imageModelArrayList.get(position).getFoodItemImage());
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.dishImageView);
        }

    }
}
