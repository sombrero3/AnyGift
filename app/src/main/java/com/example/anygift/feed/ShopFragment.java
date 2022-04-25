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
import android.widget.Toast;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.User;
import com.example.anygift.adapters.ShopGridAdapter;
import com.example.anygift.model.Model;

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

        titles.add("Cost : 10$");
        titles.add("Cost : 45$");
        titles.add("Cost : 90$");
        titles.add("Cost : 220$");
        titles.add("Cost : 435$");
        titles.add("Cost : 860$");

        images.add(R.drawable.fifty);
        images.add(R.drawable.handred);
        images.add(R.drawable.twofifty);
        images.add(R.drawable.fivehandred);
        images.add(R.drawable.taulsend);
        images.add(R.drawable.shop_icon_2);

        gridRV = view.findViewById(R.id.shop_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        gridRV.setLayoutManager(gridLayoutManager);
        adapter = new ShopGridAdapter(getContext(), titles,images);
        gridRV.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                createNewShopDialog(position);
            }
        });


        return view;
    }

    public void createNewShopDialog(int pos){
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        final View shopPopUpView = getLayoutInflater().inflate(R.layout.shop_popup,null);
        popUpCoinsIcon = shopPopUpView.findViewById(R.id.shop_popup_icon_iv);
        popUpPrice = shopPopUpView.findViewById(R.id.shop_popup_price_tv);
        popUpSaveBtn = shopPopUpView.findViewById(R.id.shop_popup_buy_btn);
        popUpCancel = shopPopUpView.findViewById(R.id.shop_popup_cancel_btn);

        popUpCoinsIcon.setImageResource(images.get(pos));
        popUpPrice.setText(titles.get(pos));

        alertDialogBuilder.setView(shopPopUpView);
        dialog = alertDialogBuilder.create();
        dialog.show();

        popUpSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double num;
                switch (pos) {
                    case  0:
                       num = 50;
                       break;
                    case  1:
                        num = 100;
                        break;
                    case  2:
                        num = 250;
                        break;
                    case  3:
                        num = 500;
                        break;
                    case  4:
                        num = 1000;
                        break;
                    case  5:
                        num = 0;
                        break;
                    default: num = 0;
                }


                Model.instance.addCoinsToUser(Model.instance.getSignedUser().getId(),
                        num, new Model.userReturnListener() {
                            @Override
                            public void onComplete(User user, String message) {
                                Model.instance.getSignedUser().setCoins(user.getCoins());
                                Toast.makeText(getContext(), "Thank You !! Spend your Gcoins wisely :)", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

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