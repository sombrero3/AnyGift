package com.example.anygift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

public class ShopGridAdapter extends RecyclerView.Adapter<ShopGridViewHolder> {

    List<Integer> images;
    List<String> titles;
    LayoutInflater layoutInflater;

    public ShopGridAdapter(Context ctx,List<String> titles, List<Integer> images){
        this.images=images;
        this.titles=titles;
        layoutInflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ShopGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = layoutInflater.inflate(R.layout.shop_grid_box,parent,false);

        return new ShopGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopGridViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.icon.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
