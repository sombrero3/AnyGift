package com.example.anygift;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShopGridViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    ImageView icon;
    public ShopGridViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.shop_grid_text_tv);
        icon = itemView.findViewById(R.id.shop_grid_icon_iv);
    }
}
