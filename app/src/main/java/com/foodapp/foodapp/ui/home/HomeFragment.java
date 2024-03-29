package com.foodapp.foodapp.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.adapters.GroupsAdapter;
import com.foodapp.foodapp.adapters.NearbyAdapter;
import com.foodapp.foodapp.helpers.Constants;
import com.foodapp.foodapp.models.FoodItemModel;
import com.foodapp.foodapp.models.GroupItemModel;
import com.foodapp.foodapp.ui.review.ReviewActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView nearbyRecyclerView;
    private RecyclerView groupsRecyclerView;

    private ArrayList<FoodItemModel> nearbyItemModelArrayList;
    private ArrayList<GroupItemModel> groupItemModelArrayList;

    // Adapters
    private NearbyAdapter nearbyAdapter;
    private GroupsAdapter groupsAdapter;

    // Buttons
    private Button btnPublishReview;

    private int[] myImageList = new int[] {
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish,
            R.mipmap.ic_salmon_dish
    };
    private String[] myImageNameList = new String[] {
            "tacos",
            "salmon",
            "pancakes",
            "tacos",
            "salmon",
            "pancakes",
            "tacos",
            "salmon",
            "pancakes",
            "tacos",
            "salmon",
            "pancakes"
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        // TODO: Replace with items pulled from DB
        nearbyRecyclerView = root.findViewById(R.id.recycler);
        nearbyItemModelArrayList = eatFruits();
        nearbyAdapter = new NearbyAdapter(getActivity(), nearbyItemModelArrayList);
        nearbyRecyclerView.setAdapter(nearbyAdapter);
        nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        groupsRecyclerView = root.findViewById(R.id.group_recycler);
        groupsRecyclerView.setNestedScrollingEnabled(true);
        groupItemModelArrayList = groups();
        groupsAdapter = new GroupsAdapter(getActivity(), groupItemModelArrayList);
        groupsRecyclerView.setAdapter(groupsAdapter);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        btnPublishReview = root.findViewById(R.id.btnPublishReview);
        btnPublishReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewIntent = new Intent(getActivity(), ReviewActivity.class);
                startActivityForResult(reviewIntent, Constants.REVIEW_ACTIVITY_RETURN_CODE);
            }
        });

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    private ArrayList<FoodItemModel> eatFruits(){

        ArrayList<FoodItemModel> list = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            FoodItemModel foodItemModel = new FoodItemModel();
            foodItemModel.setName(myImageNameList[i]);
            foodItemModel.setImage_drawable(myImageList[i]);
            list.add(foodItemModel);
        }

        return list;
    }

    private ArrayList<GroupItemModel> groups(){

        ArrayList<GroupItemModel> list = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            GroupItemModel groupItemModel = new GroupItemModel();
            groupItemModel.setName(myImageNameList[i]);
            groupItemModel.setImage_drawable(myImageList[i]);
            list.add(groupItemModel);
        }

        return list;
    }
}