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

import java.util.Calendar;
import java.util.GregorianCalendar;


public class CardsListViewHolder extends RecyclerView.ViewHolder{
    TextView typeNameTv,expTv,valueTv, calculatedPriceTv,firstPriceTv,savingTv,firstPriceTextTv;
    ImageView picIv,expIv;
    View line;
    public CardsListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        typeNameTv = itemView.findViewById(R.id.my_card_row_type_tv);
        expTv = itemView.findViewById(R.id.my_card_row_exp_date_tv);
        valueTv = itemView.findViewById(R.id.my_card_row_amount_in_card_tv);
        calculatedPriceTv = itemView.findViewById(R.id.my_card_row_calculated_price_tv);
        firstPriceTv = itemView.findViewById(R.id.my_card_row_first_price_tv);
        firstPriceTextTv = itemView.findViewById(R.id.my_card_row_first_price_text_tv);
        savingTv = itemView.findViewById(R.id.my_card_row_saving_tv);
        picIv = itemView.findViewById(R.id.my_card_row_iv);
        line = itemView.findViewById(R.id.my_card_row_line);
        expIv = itemView.findViewById(R.id.card_row_exp_iv);
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
        calculatedPriceTv.setText(card.getCalculatedPrice().toString());
        savingTv.setText(card.getPrecentageSaved());
        picIv.setImageResource(R.drawable.amazon_giftcard);

        if(card.getPrice().equals(card.getCalculatedPrice())){
                    firstPriceTextTv.setVisibility(View.INVISIBLE);
                    firstPriceTv.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
        }else{
            firstPriceTv.setText(card.getPrice().toString());
        }

        for(CardType ct: Model.instance.cardTypes) {
            if (card.getCardType().equals(ct.getId())) {
                typeNameTv.setText(ct.getName());
                picIv.setImageBitmap(ct.getPicture());
            }
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Long now = Utils.convertDateToLong(Integer.toString(day), Integer.toString(month), Integer.toString(year));
        if(card.getExpirationDate()<now){
            expIv.setVisibility(View.VISIBLE);
        }
    }
}
