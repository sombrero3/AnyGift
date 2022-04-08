package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.anygift.R;
import com.example.anygift.ShopGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    RecyclerView gridRV;
    ShopGridAdapter adapter;
    List<Integer> images;
    List<String> titles;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop, container, false);


        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("First Item");
        titles.add("Second Item");
        titles.add("Third Item");
        titles.add("Fourth Item");
        titles.add("fifth Item");
        titles.add("sixth Item");

        images.add(R.drawable.add_icon);
        images.add(R.drawable.green_face);
        images.add(R.drawable.cards_yellow);
        images.add(R.drawable.coin);
        images.add(R.drawable.common_google_signin_btn_icon_light);
        images.add(R.drawable.baseline_keyboard_arrow_down_black_24dp);

        gridRV = view.findViewById(R.id.shop_rv);
        adapter = new ShopGridAdapter(getContext(), titles,images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);


        gridRV.setLayoutManager(gridLayoutManager);
        gridRV.setAdapter(adapter);


        return view;
    }
}