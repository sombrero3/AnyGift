package com.example.anygift.feed;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;

import java.util.ArrayList;
import java.util.Calendar;
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
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                Long now = Utils.convertDateToLong(Integer.toString(day), Integer.toString(month), Integer.toString(year));
                String userId =  Model.instance.getSignedUser().getId();
                for (Card c:cards) {
                    if(c.getExpirationDate()>now && !c.getOwner().equals(userId)){
                        gfList.add(c);
                    }
                }

            }
        });
    }
}
