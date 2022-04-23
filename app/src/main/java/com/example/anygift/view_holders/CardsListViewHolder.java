package com.example.anygift.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;


public class CardsListViewHolder extends RecyclerView.ViewHolder{
    TextView typeNameTv,expTv,valueTv, calculatedPriceTv,firstPriceTv,savingTv;
    ImageView picIv;
    public CardsListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        typeNameTv = itemView.findViewById(R.id.my_card_row_type_tv);
        expTv = itemView.findViewById(R.id.my_card_row_exp_date_tv);
        valueTv = itemView.findViewById(R.id.my_card_row_amount_in_card_tv);
        calculatedPriceTv = itemView.findViewById(R.id.my_card_row_calculated_price_tv);
        firstPriceTv = itemView.findViewById(R.id.my_card_row_first_price_tv);
        savingTv = itemView.findViewById(R.id.my_card_row_saving_tv);
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

        expTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));
        valueTv.setText(card.getValue().toString());
        firstPriceTv.setText(card.getPrice().toString());
        calculatedPriceTv.setText(card.getCalculatedPrice().toString());
        savingTv.setText(card.getPrecentageSaved());
        picIv.setImageResource(R.drawable.amazon_giftcard);

        for(CardType ct: Model.instance.cardTypes) {
            if (card.getCardType().equals(ct.getId())) {
                typeNameTv.setText(ct.getName());
                picIv.setImageBitmap(ct.getPicture());
            }
        }


//        if(card.getCardType().equals("62532859427c06ccbf55d31e")) {
//            //Picasso.get().load(review.getImageUrl()).into(image);
//            picIv.setImageResource(R.drawable.shufersal_card);
//        }
    }
}
