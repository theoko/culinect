package com.foodapp.foodapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodapp.foodapp.R;
import com.foodapp.foodapp.adapters.NearbyAdapter;
import com.foodapp.foodapp.models.FoodItemModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView recyclerView;
    private ArrayList<FoodItemModel> imageModelArrayList;
    private NearbyAdapter adapter;

    private int[] myImageList = new int[] {
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
            "tacos"
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        recyclerView = root.findViewById(R.id.recycler);
        imageModelArrayList = eatFruits();
        adapter = new NearbyAdapter(getActivity(), imageModelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

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

        for(int i = 0; i < 7; i++){
            FoodItemModel foodItemModel = new FoodItemModel();
            foodItemModel.setName(myImageNameList[i]);
            foodItemModel.setImage_drawable(myImageList[i]);
            list.add(foodItemModel);
        }

        return list;
    }
}