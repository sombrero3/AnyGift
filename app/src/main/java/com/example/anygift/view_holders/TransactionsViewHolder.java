package com.example.anygift.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.model.Model;


public class TransactionsViewHolder extends RecyclerView.ViewHolder{
    TextView dateTv,amountTv,fromTv,toTv;
    ImageView typeIv;
    public TransactionsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        dateTv = itemView.findViewById(R.id.tran_row_date_tv);
        amountTv = itemView.findViewById(R.id.tran_row_amount_tv);
        fromTv = itemView.findViewById(R.id.tran_row_from_tv);
        toTv = itemView.findViewById(R.id.tran_row_to_tv);
        typeIv = itemView.findViewById(R.id.tran_row_type_iv);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }
    public void bind(CoinTransaction tran) {
        dateTv.setText(tran.getDate());
        amountTv.setText(tran.getAmount().toString());
        fromTv.setText(tran.getFrom());
        toTv.setText(tran.getTo().toString());

//        for(CardType ct: Model.instance.cardTypes){
//            if(ct.getId().e)
//        }
    }
}
