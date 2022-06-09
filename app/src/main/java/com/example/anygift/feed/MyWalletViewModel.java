package com.example.anygift.feed;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.ModelRetrofit;

import java.util.ArrayList;
import java.util.List;

public class MyWalletViewModel extends ViewModel {
    private List<Card> gfList;
    boolean flag;
    public MyWalletViewModel() {
        flag=false;
        gfList = new ArrayList<>();
        Log.d("TAG", "FeedViewModel");

    }

    private void setList() {
        Model.instance.getAllUserCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                gfList.clear();
                gfList.addAll(cards);
            }
        });
    }

    public void getListWithListener(Model.CardsListListener listener){
        Model.instance.getAllUserCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                if(cards!=null) {
                    gfList.clear();
                    gfList.addAll(cards);
                    listener.onComplete(cards);
                }
            }
        });
    }

    List<Card> getList() {
        return gfList;
    }
}
