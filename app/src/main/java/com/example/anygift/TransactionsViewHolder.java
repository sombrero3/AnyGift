package com.example.anygift;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.Retrofit.CoinTransaction;


public class TransactionsViewHolder extends RecyclerView.ViewHolder{
    //TextView tv;
    public TransactionsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        //tv = itemView.findViewById(R.id.)

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }
    public void bind(CoinTransaction tran) {

    }
}
