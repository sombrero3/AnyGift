package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anygift.MyApplication;

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

    public interface GetAllGiftCardListener{
        void onComplete();
    }

    public void refreshGiftCardsList(final GetAllGiftCardListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated",0);
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllProducts (lastUpdated, new ModelFirebase.GetAllGiftCardListener() {
            @Override
            public void onComplete(List<GiftCard> result) {
                //3. insert the new updates to the local db
                long lastU = 0;
                for (GiftCard gf: result) {
                    room.addGiftCard(gf,null);
                    if (gf.getLastUpdated()>lastU){
                        lastU = gf.getLastUpdated();
                    }
                }
                //4. update the local last update date
                sp.edit().putLong("lastUpdated", lastU).commit();
                //5. return the updates data to the listeners
                if(listener != null){
                    listener.onComplete();
                }
            }
        });
    }

    public interface AddGiftCardListener {
        void onComplete();
    }

    public void addGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.addGiftCard(giftCard, new AddGiftCardListener() {
            @Override
            public void onComplete() {
                refreshGiftCardsList(new GetAllGiftCardListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });
    }

    public void updateProduct(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.updateGiftCard(giftCard, listener);
    }


    public interface GetGiftCardListener {
        void onComplete(GiftCard giftCard);
    }

    public void getGiftCard(String email, GetGiftCardListener listener) {
        modelFirebase.getGiftCard(email, listener);
    }

/* upload image
  public interface Listener<T> {
        void onComplete(T result);
    }


  public interface UploadImageListener extends Listener<String>{ }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

 */

    //user
    public interface  AddUserListener{
        void onComplete();
    }
    public void addUser(final User user , final AddUserListener listener) {
        modelFirebase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public ModelFirebase getModelFirebase(){
        return this.modelFirebase;
    }
}
