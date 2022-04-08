package com.example.anygift.feed;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.adapters.ShopGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    RecyclerView gridRV;
    ShopGridAdapter adapter;
    List<Integer> images;
    List<String> titles;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog dialog;
    TextView popUpPrice;
    ImageView popUpCoinsIcon;
    Button popUpCancel, popUpSaveBtn;
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        gridRV.setLayoutManager(gridLayoutManager);
        adapter = new ShopGridAdapter(getContext(), titles,images);
        gridRV.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                createNewShopDialog();
            }
        });


        return view;
    }

    public void createNewShopDialog(){
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        final View shopPopUpView = getLayoutInflater().inflate(R.layout.shop_popup,null);
        popUpCoinsIcon = shopPopUpView.findViewById(R.id.shop_popup_icon_iv);
        popUpPrice = shopPopUpView.findViewById(R.id.shop_popup_price_tv);
        popUpSaveBtn = shopPopUpView.findViewById(R.id.shop_popup_buy_btn);
        popUpCancel = shopPopUpView.findViewById(R.id.shop_popup_cancel_btn);


        alertDialogBuilder.setView(shopPopUpView);
        dialog = alertDialogBuilder.create();
        dialog.show();

        popUpSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        popUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}