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
    private LiveData<List<GiftCard>> gfList;
    private ArrayList<Card> dreamCardsList,shufersalList;
    private HashMap<String, ArrayList<Card>> map;

    public FeedViewModel() {
        Log.d("TAG", "FeedViewModel");
        gfList = Model.instance.getAll();
        refreshMap(() -> {});
    }

    void refreshMap(Model.VoidListener listener){
        Model.instance.getallcardsByCardType(m -> {
            map = m;
            dreamCardsList = map.get("dreamCard");
            shufersalList = map.get("Shufersal");
            listener.onComplete();
        });
    }

    public ArrayList<Card> getDreamCardsList() {
        return dreamCardsList;
    }

    public ArrayList<Card> getShufersalList() {
        return shufersalList;
    }

    HashMap<String, ArrayList<Card>> getMap() {return map;}
    LiveData<List<GiftCard>> getList() {
        return gfList;
    }
}
