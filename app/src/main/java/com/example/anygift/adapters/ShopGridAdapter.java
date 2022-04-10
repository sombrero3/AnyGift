package com.example.anygift.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.feed.MyCardsFragment;
import com.example.anygift.view_holders.ShopGridViewHolder;
;

import java.util.List;

public class ShopGridAdapter extends RecyclerView.Adapter<ShopGridViewHolder> {

    OnItemClickListener listener;
    List<Integer> images;
    List<String> titles;
    LayoutInflater layoutInflater;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public ShopGridAdapter(Context ctx,List<String> titles, List<Integer> images){
        this.images=images;
        this.titles=titles;
        layoutInflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ShopGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = layoutInflater.inflate(R.layout.shop_grid_box,parent,false);
        return new ShopGridViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopGridViewHolder holder, int position) {
        holder.bind(titles.get(position),images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

}
