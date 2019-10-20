package com.foodapp.foodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.models.GroupItemModel;

import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<GroupItemModel> imageModelArrayList;
    public static int limit = 6;

    public GroupsAdapter(Context ctx, ArrayList<GroupItemModel> imageModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public GroupsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.group_card, parent, false);
        GroupsAdapter.MyViewHolder holder = new GroupsAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.MyViewHolder holder, int position) {
//        holder.iv.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        holder.time.setText(imageModelArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(imageModelArrayList.size() < limit) return imageModelArrayList.size(); else return limit;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView time;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.groupTitle);
            iv = (ImageView) itemView.findViewById(R.id.heartIcon);
        }

    }
}
