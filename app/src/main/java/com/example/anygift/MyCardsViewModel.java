package com.example.anygift;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;

import java.util.List;

public class MyCardsViewModel extends ViewModel {
    private LiveData<List<GiftCard>> gfList;

    public MyCardsViewModel() {
        Log.d("TAG", "FeedViewModel");
        gfList = Model.instance.getAll();
    }

    LiveData<List<GiftCard>> getList() {
        return gfList;
    }
}
