package com.example.anygift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.view_holders.CardsListViewHolder;
import com.example.anygift.view_holders.ShopGridViewHolder;

import java.util.List;

public class CardsListAdapter extends RecyclerView.Adapter<CardsListViewHolder> {

    OnItemClickListener listener;
    List<Card> cardsList;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public CardsListAdapter(List<Card> cardsList){
        this.cardsList = cardsList;
    }
    @NonNull
    @Override
    public CardsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_card_row,parent,false);
        CardsListViewHolder holder = new CardsListViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardsListViewHolder holder, int position) {
        Card card = cardsList.get(position);
        holder.bind(card);
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }
}
