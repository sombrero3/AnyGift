package com.example.anygift.feed;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedViewModel extends ViewModel {
    private List<Card> gfList;

    public FeedViewModel() {
        Log.d("TAG", "FeedViewModel");
        gfList=new ArrayList<>();
        refreshMRList();
    }

    public List<Card> getMostRecList( ) {
        return gfList;
    }
    void refreshMRList(){
        Model.instance.modelRetrofit.getAllCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                gfList.clear();
                gfList.addAll(cards);
            }
        });
    }
}
