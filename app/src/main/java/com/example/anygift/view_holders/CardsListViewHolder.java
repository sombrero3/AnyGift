package com.example.anygift.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.model.Utils;


public class CardsListViewHolder extends RecyclerView.ViewHolder{
    TextView typeNameTv,expTv,valueTv,priceTv;
    ImageView picIv;
    public CardsListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        typeNameTv = itemView.findViewById(R.id.my_card_row_type_tv);
        expTv = itemView.findViewById(R.id.my_card_row_exp_date_tv);
        valueTv = itemView.findViewById(R.id.my_card_row_amount_in_card_tv);
        priceTv = itemView.findViewById(R.id.my_card_row_price_now_tv);
        picIv = itemView.findViewById(R.id.my_card_row_iv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }
    public void bind(Card card){
        typeNameTv.setText(card.getCardType());
        expTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));
        valueTv.setText(card.getValue().toString());

        priceTv.setText(card.getPrice().toString());
        picIv.setImageResource(R.drawable.amazon_giftcard);
        if(card.getCardType().equals("62532859427c06ccbf55d31e")) {
            //Picasso.get().load(review.getImageUrl()).into(image);
            picIv.setImageResource(R.drawable.shufersal_card);
            typeNameTv.setText("Shufersal");
        }
    }
}
