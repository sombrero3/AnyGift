package com.example.anygift.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;

public class ShopGridViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    ImageView icon;
    public ShopGridViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        title = itemView.findViewById(R.id.shop_grid_text_tv);
        icon = itemView.findViewById(R.id.shop_grid_icon_iv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }

    public void bind(String title,Integer icon){
        this.title.setText(title);
        this.icon.setImageResource(icon);
    }
}
