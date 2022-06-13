package com.example.anygift.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.model.Model;


public class TransactionsViewHolder extends RecyclerView.ViewHolder{
    TextView dateTv, numOfCoinsPayedTv,typeNameTv,otherTv,titleOtherTv,noFeedbackTv;
    ImageView typeIv,likeIv,unlikeIv;
    public TransactionsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        dateTv = itemView.findViewById(R.id.tran_row_date_tv);
        numOfCoinsPayedTv = itemView.findViewById(R.id.tran_row_gcoins_cost_tv);
        typeNameTv = itemView.findViewById(R.id.tran_row_type_name_tv);
        otherTv = itemView.findViewById(R.id.tran_row_other_tv);
        typeIv = itemView.findViewById(R.id.tran_row_type_iv);
        titleOtherTv= itemView.findViewById(R.id.tran_row_title_other_tv);
        likeIv = itemView.findViewById(R.id.tran_row_like_iv);
        unlikeIv = itemView.findViewById(R.id.tran_row_unlike_iv);
        noFeedbackTv = itemView.findViewById(R.id.tran_row_nofeedback_tv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }
    public void bind(CardTransaction tran) {
        dateTv.setText(tran.getDate().split("T")[0]);
        numOfCoinsPayedTv.setText(tran.getBoughtFor().toString());

        if(tran.getBuyer().equals(Model.instance.getSignedUser().getId())){
            otherTv.setText(tran.getSellerEmail());
            titleOtherTv.setText("Bought From : ");
        }else if(tran.getSeller().equals(Model.instance.getSignedUser().getId())){
            otherTv.setText(tran.getBuyerEmail());
            titleOtherTv.setText("Sold To : ");
        }else{
            otherTv.setText("ERROR!! That shouldn't be here!!");
        }

        for(CardType ct: Model.instance.cardTypes){
            if(ct.getId().equals(tran.getCardType())){
                typeNameTv.setText(ct.getName());
                if(ct.getPicture()!=null) {
                    typeIv.setImageBitmap(ct.getPicture());
                }else{
                    typeIv.setImageResource(R.drawable.gift_card_logo_card);
                }
            }
        }

        if(tran.getSatisfied()!=null) {
            if (tran.getSatisfied()) {
                likeIv.setVisibility(View.VISIBLE);
                unlikeIv.setVisibility(View.GONE);
                noFeedbackTv.setVisibility(View.GONE);
            } else if (!tran.getSatisfied()) {
                unlikeIv.setVisibility(View.VISIBLE);
                noFeedbackTv.setVisibility(View.GONE);
                likeIv.setVisibility(View.GONE);
            }
        }else{
            noFeedbackTv.setVisibility(View.VISIBLE);
            likeIv.setVisibility(View.GONE);
            unlikeIv.setVisibility(View.GONE);
        }
    }
}
