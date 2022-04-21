package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anygift.MyApplication;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    public ModelFirebase modelFirebase = new ModelFirebase();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public com.example.anygift.Retrofit.User signedUser;
    public ModelRetrofit modelRetrofit = new ModelRetrofit();
    public MutableLiveData<List<GiftCard>> giftCardsList = new MutableLiveData<>();
    public List<Category> categories = new ArrayList<>();

    private Model() {
        signedUser = new com.example.anygift.Retrofit.User();
        ListLoadingState.setValue(GiftListLoadingState.loaded);
    }

    public LiveData<List<GiftCard>> getAll() {
        if (giftCardsList.getValue() == null) {
            refreshGiftCardsList();
        }
        return giftCardsList;
    }

    //Node- Retrofit
    public interface StringListener {
        void onComplete(String message);
    }

    public interface userLoginListener {
        void onComplete(com.example.anygift.Retrofit.User user, String message);
    }

    public interface userReturnListener {
        void onComplete(com.example.anygift.Retrofit.User user,String message);
    }

    public interface cardReturnListener {
        void onComplete(Card card, String message);
    }

    public interface cardsReturnListener {
        void onComplete(List<Card> cards, String message);
    }

    public interface cardTypesReturnListener {
        void onComplete(List<CardType> cts);
    }

    public interface categoriesReturnListener {
        void onComplete(List<Category> cat);
    }

    public interface incomeListener {
        void onComplete(Income income);
    }

    public interface outComeListener {
        void onComplete(Outcome outcome);
    }

    public interface coinTransactionListener {
        void onComplete(String message);
    }
    public interface booleanReturnListener{
        void onComplete(Boolean result,String message);
    }

    //NO AUTHENTICAION
    public void login(HashMap<String, Object> map, userLoginListener listener) {
        modelRetrofit.login(map, listener);
    }

    public void logout(StringListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.logout(listener);
        });
    }

    public void getUserRetrofit(String user_id, userReturnListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.getUser(user_id, listener);
        });
    }

    public void getAllCategories(categoriesReturnListener l) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.getAllCategories(l);
        });
    }

    public void getAllCardTypes(cardTypesReturnListener l) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.getAllCardTypes(l);
        });
    }

    public void getUserIncome(incomeListener listener) {
            modelRetrofit.getUserIncome(listener);
    }

    public void getUserOutCome(outComeListener listener) {
            modelRetrofit.getUserOutCome(listener);
    }

    //NO AUTHENTICAION
    public void addUserRetrofit(HashMap<String, Object> map, userReturnListener listener) {
        modelRetrofit.addUser(map, listener);
    }

    public void addCoinTransaction(HashMap<String, Object> map, coinTransactionListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.addCoinTransaction(map, listener);
        });
    }

    public void updateUser(HashMap<String, Object> map, userLoginListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.updateUser(map, listener);
        });
    }

    public void addCardRetrofit(HashMap<String, Object> map, cardReturnListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.addCard(map, listener);
        });
    }

    public void getAllCards(cardsReturnListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.getAllCards(listener);
        });
    }

    public void getAllUserCards(cardsReturnListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.getAllUserCards(listener);
        });
    }

    public void getAllFeedCardsForSale(cardsReturnListener listener) {
            modelRetrofit.getAllCards(new cardsReturnListener() {
                @Override
                public void onComplete(List<Card> cards, String message) {
                    String user_id = modelRetrofit.getUserId();
                    cards.stream().filter(c->c.getIsForSale() && !c.getOwner().equals(user_id));
                    listener.onComplete(cards,"Cards filtered");
                }
            });
    }

    public interface mapStringToCardsArrayListener{
        void onComplete(HashMap<String,ArrayList<Card>> map);
    }
    public void getallcardsByCardType(mapStringToCardsArrayListener listener) {
        getAllFeedCardsForSale(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                System.out.println(message);
                System.out.println(cards);
                Model.instance.modelRetrofit.getAllCardTypes(new Model.cardTypesReturnListener() {
                    @Override
                    public void onComplete(List<CardType> cts) {
                        if (cts != null) {
                            HashMap<String, ArrayList<Card>> map = new HashMap<>();
                            for (CardType c : cts) {
                                map.put(c.getId(), new ArrayList<>());
                            }
                            for (Card c : cards) {
                                ArrayList<Card> arrayList = map.get(c.getCardType());
                                arrayList.add(c);
                                map.put(c.getCardType(), arrayList);
                            }
                            for (CardType c : cts) {
                                map.put(c.getName(), map.get(c.getId()));
                                map.remove(c.getId());
                            }
                            System.out.println(map);
                            listener.onComplete(map);
                        }
                    }
                });
            }
        });
    }


    public void updateCardRetrofit(String card_id, HashMap<String, Object> map, cardReturnListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.updateCard(card_id, map, listener);
        });
    }

    public void getCardRetrofit(String card_id, cardReturnListener listener) {
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.getCard(card_id, listener);
        });
    }


    public void deleteCardRetrofit(String card_id, cardReturnListener listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isDeleted", true);
        HashMap<String, Object> m = Card.mapToUpdateCard(card_id, map);
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            updateCardRetrofit(card_id, m, listener);
        });
    }

    public void addCoinsToUser(String user_id, double coinsToAdd, userReturnListener listener) {
        HashMap<String,Double>map = new  HashMap<String,Double>();
        map.put("coins",coinsToAdd);
        modelRetrofit.refreshToken(message -> {
            System.out.println(message);
            modelRetrofit.addCoinsToUser(user_id, map, listener);
        });

    }


    //Firebase
    public enum GiftListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<GiftListLoadingState> ListLoadingState = new MutableLiveData<GiftListLoadingState>();

    public MutableLiveData<GiftListLoadingState> getListLoadingState() {
        return ListLoadingState;
    }

    public interface VoidListener {
        void onComplete();
    }

    public interface GetUserListener {
        void onComplete(User user);
    }

    public void refreshGiftCardsList() {
        ListLoadingState.setValue(GiftListLoadingState.loading);

        //1. get local last update date
        final SharedPreferences sp = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        executor.execute(() -> {
            List<GiftCard> gcList = AppLocalDb.db.giftCardDao().getAll();
            giftCardsList.postValue(gcList);
        });
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllProducts(lastUpdated, new ModelFirebase.GetAllGiftCardListener() {
            @Override
            public void onComplete(List<GiftCard> result) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        long lastU = 0;
                        for (GiftCard gf : result) {
                            AppLocalDb.db.giftCardDao().insertAll(gf);
                            if (gf.getLastUpdated() > lastU) {
                                lastU = gf.getLastUpdated();
                            }
                        }
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("lastUpdated", lastU)
                                .commit();

                        List<GiftCard> stList = AppLocalDb.db.giftCardDao().getAll();
                        giftCardsList.postValue(stList);
                        ListLoadingState.postValue(GiftListLoadingState.loaded);


                    }
                });
            }
        });
    }


    public interface AddGiftCardListener {
        void onComplete();
    }

    public void addGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.addGiftCard(giftCard, listener);
    }


    public void updateGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.updateGiftCard(giftCard, listener);
    }


    public interface GetGiftCardListener {
        void onComplete(GiftCard giftCard);
    }

    public void getGiftCard(String email, GetGiftCardListener listener) {
        modelFirebase.getGiftCard(email, listener);
    }

     /*
     //upload image
      public interface Listener<T> {
            void onComplete(T result);
        }


      public interface UploadImageListener extends Listener<String>{ }

      */


    public interface UploadImageListener {
        void onComplete(String url);
    }


    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

    public interface UploadUserImageListener {
        void onComplete(String url);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, final UploadUserImageListener listener) {
        modelFirebase.uploadUserImage(imageBmp, name, listener);
    }


    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }

    //user
    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(final User user, final AddUserListener listener) {
        modelFirebase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void updateUser(final User user, final AddUserListener listener) {
        modelFirebase.updateUser(user, listener);
    }


    public ModelFirebase getModelFirebase() {
        return this.modelFirebase;
    }

    /**
     * Authentication
     */
    public void isTokenValid(booleanReturnListener listener) {

        if (modelRetrofit.getAccessToken().isEmpty())
            listener.onComplete(false,"Empty Token");
        else
            modelRetrofit.refreshToken(message -> {
                System.out.println(message);
                modelRetrofit.authenticateToken(listener);
            });

    }

    public com.example.anygift.Retrofit.User getSignedUser() {
        return signedUser;
    }

    public void setCurrentUser(com.example.anygift.Retrofit.User user) {
        signedUser = user;
    }
}
