package com.example.anygift.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Model {
    public static final Model instance = new Model();
    public ModelFirebase modelFirebase = new ModelFirebase();
    ModelRoom room=new ModelRoom();
/*
    Executor executor = Executors.newFixedThreadPool(1);
    Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
*/

    private Model() {

    }

   // MutableLiveData<List<GiftCard>> giftCardsList= new MutableLiveData<>();
  LiveData<List<GiftCard>> giftCardsList;

    public LiveData<List<GiftCard>> getAllGiftCard() {
        if (giftCardsList == null){
            giftCardsList = room.getAllGiftCards();
           // refreshAllGiftCards(null);
        }
        return giftCardsList;
    }

    public interface GetAllGiftCardListener {
        void onComplete(List<GiftCard> giftCards);
    }

    public interface AddGiftCardListener {
        void onComplete();
    }

    public interface GetGiftCardListener {
        void onComplete(GiftCard giftCard);
    }

    public interface AddUserListener {
        void onComplete();
    }
}
