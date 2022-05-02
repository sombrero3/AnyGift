package com.example.anygift.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.view_holders.TransactionsViewHolder;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsViewHolder>{

    OnItemClickListener listener;
    List<CoinTransaction> transList;

    public TransactionsAdapter(List<CoinTransaction> transList){
        this.transList = transList;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_row,parent,false);
        TransactionsViewHolder holder = new TransactionsViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        CoinTransaction tran = transList.get(position);
        holder.bind(tran);
    }

    @Override
    public int getItemCount() {
        return transList.size();
    }
}
