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
    //private List<GiftCard> gfList;
    private ArrayList<Card> dreamCardsList,shufersalList,gfList;
    private HashMap<String, ArrayList<Card>> map;

    public FeedViewModel() {
        Log.d("TAG", "FeedViewModel");
        gfList = new ArrayList<>();
        dreamCardsList = new ArrayList<>();
        shufersalList = new ArrayList<>();
        refreshMap(() -> {});
    }

    void refreshMap(Model.VoidListener listener){
        clearLists();
        Model.instance.getallcardsByCardType(m -> {
            map = m;
            dreamCardsList = map.get("dreamCard");
            shufersalList = map.get("Shufersal");
            Model.instance.getAllFeedCardsForSale(new Model.cardsReturnListener() {
                @Override
                public void onComplete(List<Card> cards, String message) {
                    cards.sort((c1, c2) -> {
                        double c1value = Double.parseDouble(c1.getPrecentageSaved().replace("%", ""));
                        double c2value = Double.parseDouble(c2.getPrecentageSaved().replace("%", ""));
                        return Double.compare(c1value, c2value);
                    });
                    System.out.println(cards);
                    gfList.addAll(cards);
                    listener.onComplete();
                }
            });

        });

    }

    private void clearLists() {
        gfList.clear();
        dreamCardsList.clear();
        shufersalList.clear();
    }

    public ArrayList<Card> getDreamCardsList() {
        return dreamCardsList;
    }

    public ArrayList<Card> getShufersalList() {
        return shufersalList;
    }

    HashMap<String, ArrayList<Card>> getMap() {return map;}
    List<Card> getMostRecList() {
        return gfList;
    }
}
