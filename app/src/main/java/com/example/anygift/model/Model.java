package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anygift.MyApplication;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.Outcome;
import com.example.anygift.Retrofit.SellerRatings;
import com.example.anygift.Retrofit.UploadImageResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Model {
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public com.example.anygift.Retrofit.User signedUser;
    public ModelRetrofit modelRetrofit = new ModelRetrofit();
    public MutableLiveData<List<GiftCard>> giftCardsList = new MutableLiveData<>();
    public List<Category> categories = new ArrayList<>();
    public List<CardType> cardTypes = new ArrayList<>();
    public HashMap<String,List<String>> categoryToTypesHash = new HashMap<>();

    private Model() {
        signedUser = new com.example.anygift.Retrofit.User();
        ListLoadingState.setValue(GiftListLoadingState.loaded);
    }

    public void getCardTypeById(String id,cardTypeReturnListener listener){
        boolean flag=false;
        for(CardType c :cardTypes){
            if(c.getId().equals(id)){
                flag = true;
                listener.onComplete(c);
            }
        }
        if(!flag) {
            setCardTypes(new VoidListener() {
                @Override
                public void onComplete() {
                    for(CardType c :cardTypes){
                        if(c.getId().equals(id)){
                            listener.onComplete(c);
                            break;
                        }
                    }
                }
            });
        }
    }

    public void setCategories(VoidListener listener) {
        getAllCategories(new categoriesReturnListener() {
            @Override
            public void onComplete(List<Category> cat) {
                categories.clear();
                categoryToTypesHash.clear();
                for (Category category : cat) {
                    categories.add(category);
                    List<String> list = new ArrayList<>();
                    categoryToTypesHash.put(category.getId(),list);
                    for(CardType ct:cardTypes){
                        if(ct.getCategories().contains(category.getId())){
                            categoryToTypesHash.get(category.getId()).add(ct.getId());
                        }
                    }
                }
                listener.onComplete();
            }
        });
    }

    public void setCardTypes(VoidListener listener) {
        getAllCardTypes(cts -> {
            cardTypes.clear();
            for (CardType ct : cts) {
                Model.instance.executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadImage(ct.getId() + ".jpeg", new byteArrayReturnListener() {
                            @Override
                            public void onComplete(Bitmap bitmap) {
                                if(bitmap!=null) {
                                    ct.setPicture(bitmap);
                                }
                                cardTypes.add(ct);
                                if(ct.getId().equals(cts.get(cts.size()-1).getId())) {
                                    listener.onComplete();
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    //Node- Retrofit
    public interface StringListener {
        void onComplete(String message);
    }

    public interface cardsTransactionsReturnListener {
        void onComplete(List<CardTransaction> cardTransaction,String message);
    }

    public interface cardsTransactionReturnListener {
        void onComplete(CardTransaction cardTransaction,String message);
    }


    public interface cardTransactionReturnListener {
        void onComplete(CardTransaction cardTransaction,String message);
    }


    public interface userLoginListener {
        void onComplete(com.example.anygift.Retrofit.User user, String message);
    }

    public interface userReturnListener {
        void onComplete(com.example.anygift.Retrofit.User user, String message);
    }

    public interface cardReturnListener {
        void onComplete(Card card, String message);
    }

    public interface cardsReturnListener {
        void onComplete(List<Card> cards, String message);
    }
    public interface cardsListener{
        void onComplete(List<Card> cards);
    }

    public interface uploadImageListener {
        void onComplete(UploadImageResult uploadImageResult);
    }

    public interface cardTypesReturnListener {
        void onComplete(List<CardType> cts);
    }

    public interface cardTypeReturnListener {
        void onComplete(CardType ct);
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

    public interface sellerRatingsListener {
        void onComplete(SellerRatings sr);
    }

    public interface coinTransactionListener {
        void onComplete(String message);
    }

    public interface booleanReturnListener {
        void onComplete(Boolean result, String message);
    }

    public interface byteArrayReturnListener {
        void onComplete(Bitmap bitmap);
    }

    //NO AUTHENTICAION
    public void login(HashMap<String, Object> map, userLoginListener listener) {
        modelRetrofit.login(map, listener);
    }

    public void logout(StringListener listener) {
        modelRetrofit.logout(listener);
    }

    public void downloadImage(String image, byteArrayReturnListener l) {
        modelRetrofit.downloadImage(image, l);
    }

    public void getUserRetrofit(String user_id, userReturnListener listener) {
        modelRetrofit.getUser(user_id, listener);
    }

    public void uploadImage(byte[] imageBytes, uploadImageListener listener) {
        modelRetrofit.uploadImage(imageBytes, listener);
    }

    public void getAllCategories(categoriesReturnListener l) {
        modelRetrofit.getAllCategories(l);
    }

    public void getAllCardTypes(cardTypesReturnListener l) {
        modelRetrofit.getAllCardTypes(l);
    }

    public void addCardType(HashMap<String, Object> map,cardTypeReturnListener l) {
        modelRetrofit.addCardType(map,l);
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
        modelRetrofit.addCoinTransaction(map, listener);
    }

    public void updateUser(HashMap<String, Object> map, userLoginListener listener) {
        modelRetrofit.updateUser(map, listener);
    }

    public void addCardRetrofit(HashMap<String, Object> map, cardReturnListener listener) {
        modelRetrofit.addCard(map, listener);
    }

    public void getAllCards(cardsReturnListener listener) {
        modelRetrofit.getAllCards(new cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                if(cards!=null) {
                    boolean cardTypesUpdateRequire = false;
                    for(Card c:cards){
                        boolean isTypeExist = false;
                        for(CardType ct:cardTypes){
                            if(c.getCardType().equals(ct.getId())){
                                isTypeExist = true;
                                break;
                            }
                        }
                        if(!isTypeExist){
                            cardTypesUpdateRequire = true;
                            break;
                        }

                    }
                    if(cardTypesUpdateRequire) {
                        setCardTypes(new VoidListener() {
                            @Override
                            public void onComplete() {
                                listener.onComplete(cards, "CardTypes Updated");
                            }
                        });
                    }
                    }else{
                        listener.onComplete(cards, "All Good");
                    }
            }
        });
    }

    public void getAllUserCards(cardsReturnListener listener) {
//        modelRetrofit.getAllUserCards(listener);
        modelRetrofit.getAllUserCards(new cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                if(cards!=null) {
                    boolean cardTypesUpdateRequire = false;
                    for(Card c:cards){
                        boolean isTypeExist = false;
                        for(CardType ct:cardTypes){
                            if(c.getCardType().equals(ct.getId())){
                                isTypeExist = true;
                                break;
                            }
                        }
                        if(!isTypeExist){
                            cardTypesUpdateRequire = true;
                            break;
                        }
                    }

                    if(cardTypesUpdateRequire) {
                        setCardTypes(new VoidListener() {
                            @Override
                            public void onComplete() {
                                listener.onComplete(cards, "CardTypes Updated");
                            }
                        });
                    }else{
                        listener.onComplete(cards, "All Good");
                    }
                }else{
                    listener.onComplete(new ArrayList<>(), "All Good");
                }
            }
        });
    }


    public void getSellerRatings(String user_id, sellerRatingsListener listener) {
        modelRetrofit.getSellerRatings(user_id, listener);
    }


    public void addReview(String card_trans_id, Boolean satisfied,String buyerComment,cardTransactionReturnListener l){
        HashMap<String,Object> map = new HashMap<>();
        map.put("buyerComment",buyerComment);
        map.put("satisfied",satisfied);
        modelRetrofit.addCardTransSatis(card_trans_id,map, l);
    }

    public void getAllFeedCardsForSale(cardsReturnListener listener) {
        modelRetrofit.getAllCards(new cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                if (cards != null) {
                    String user_id = modelRetrofit.getUserId();
                    List<Card> cs = cards.stream().filter(c -> c.getIsForSale() &&
                            !c.getOwner().equals(user_id) &&
                            System.currentTimeMillis() / 1000 < c.getExpirationDate()
                    ).collect(Collectors.toList());

                    boolean cardTypesUpdateRequire = false;
                    for (Card c : cs) {
                        boolean isTypeExist = false;
                        for (CardType ct : cardTypes) {
                            if (c.getCardType().equals(ct.getId())) {
                                isTypeExist = true;
                                break;
                            }
                        }
                        if (!isTypeExist) {
                            cardTypesUpdateRequire = true;
                            break;
                        }
                    }

                    if (cardTypesUpdateRequire) {
                        setCardTypes(new VoidListener() {
                            @Override
                            public void onComplete() {
                                listener.onComplete(cs, "CardTypes Updated Cards filtered");
                            }
                        });
                    } else {
                        listener.onComplete(cs, "All Good Cards filtered");
                    }
                }
            }
        });
    }

    public interface mapStringToCardsArrayListener {
        void onComplete(HashMap<String, ArrayList<Card>> map);
    }

    public void getallcardsByCardType(mapStringToCardsArrayListener listener) {
        getAllFeedCardsForSale(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                System.out.println(message);
                System.out.println(cards);
       /*         Model.instance.modelRetrofit.getAllCardTypes(new Model.cardTypesReturnListener() {
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
           */
            }
        });
    }

    public interface CardsListListener {
        void onComplete(List<Card> cards);
    }

    public void updateCardRetrofit(String card_id, HashMap<String, Object> map, cardReturnListener listener) {
        modelRetrofit.updateCard(card_id, map, listener);
    }

    public void getCardRetrofit(String card_id, cardReturnListener listener) {
        modelRetrofit.getCard(card_id, new cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                boolean isTypeExist = false;
                if(card.getCardType() !=null){
                    for(CardType ct:cardTypes){
                        if(ct.getId().equals(card.getCardType())){
                            isTypeExist = true;
                            break;
                        }
                    }
                }
                if(!isTypeExist) {
                    setCardTypes(new VoidListener() {
                        @Override
                        public void onComplete() {
                            listener.onComplete(card,"CardTypes Updated");
                        }
                    });
                }else{
                    listener.onComplete(card,"All Good");
                }
            }
        });
    }


    public void getCardsTransactionsRetrofit(cardsTransactionsReturnListener listener) {
        modelRetrofit.getAllUserCardsTransactions(listener);
    }

    public void getCardsTransactionByTransactionIdRetrofit(String trans_id,cardsTransactionReturnListener listener) {
        modelRetrofit.getCardTransactionByTransactionId(trans_id,listener);
    }



    public void addCardTransaction(HashMap<String,Object> map,booleanReturnListener listener) {
        modelRetrofit.addCardTransaction(map, listener);
    }


    public void checkIfEmailExists(String email, Model.booleanReturnListener listener){
        modelRetrofit.checkIfEmailExists(email,listener);
    }

    public void deleteCardRetrofit(String card_id, cardReturnListener listener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isDeleted", true);
        HashMap<String, Object> m = Card.mapToUpdateCard(card_id, map);
        updateCardRetrofit(card_id, m, listener);
    }

    public void addCoinsToUser(String user_id, double coinsToAdd, userReturnListener listener) {
        HashMap<String, Double> map = new HashMap<String, Double>();
        map.put("coins", coinsToAdd);
        modelRetrofit.addCoinsToUser(user_id, map, listener);

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


    public interface AddGiftCardListener {
        void onComplete();
    }

    /**
     * Authentication
     */
    public void isTokenValid(booleanReturnListener listener) {

        if (modelRetrofit.getAccessToken().isEmpty())
            listener.onComplete(false, "Empty Token");
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


    /**
     * search cards
     */
    public List<Card> filterByCategory(String categoryName,List<Card> cards){
        List<Card> result = new ArrayList<>();
        return result;
    }


    public void searchCards(int day,int month,int year,String price,String cardTypeId,String categoryId,cardsListener listener){
        List<Card> result = new ArrayList<>();
        getAllFeedCardsForSale(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                Log.d("TAG", "maxPrice: "+price);
                Log.d("TAG", "onDateSet: "+day+"/"+month+"/"+year);


                boolean isFiltered = false;

                System.out.println("cards Before Search: ");
                for(Card c :cards){
                    System.out.println("Type : "+c.getCardType()+" Price : "+c.getPrice());
                }
                System.out.println("Search Inputs: ");
                System.out.println("Exp : "+day+"/"+month+"/"+year+"    typeId : "+cardTypeId+"     categoryId : "+categoryId+ "      Price : "+price);

                if(!cardTypeId.equals("Any")){
                    isFiltered = true;
                    result.clear();
                    result.addAll(cards.stream().filter(c->c.getCardType().equals(cardTypeId)).collect(Collectors.toList()));
                    cards.clear();
                    cards.addAll(result);
                }

                if(!categoryId.equals("Any")){
                    isFiltered = true;
                    result.clear();
                    List<String> list = categoryToTypesHash.get(categoryId);
                    result.addAll(cards.stream().filter(c->list.contains(c.getCardType())).collect(Collectors.toList()));
                    cards.clear();
                    cards.addAll(result);
                }

                if(!price.isEmpty()){
                    isFiltered = true;
                    result.clear();
                    result.addAll(cards.stream().filter(c->c.getCalculatedPrice()<=Double.valueOf(price)).collect(Collectors.toList()));
                    cards.clear();
                    cards.addAll(result);
                }

                if(day!=0){
                    isFiltered = true;
                    result.clear();
                    result.addAll(cards.stream().filter(c ->c.getExpirationDate() <= Utils.convertDateToLong(Integer.toString(day), Integer.toString(month), Integer.toString(year)).longValue()).collect(Collectors.toList()));
                    cards.clear();
                    cards.addAll(result);
                }

                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                Long now = Utils.convertDateToLong(Integer.toString(d), Integer.toString(m), Integer.toString(y));
                String userId =  Model.instance.getSignedUser().getId();

                if(!isFiltered){
                    result.addAll(cards);
                }

                for (Card c:result) {

                    if(c.getExpirationDate()<now || c.getOwner().equals(userId)){
                        result.remove(c);
                    }
                }

                System.out.println("Search result: ");
                for(Card c :result){
                    System.out.println("Type : "+c.getCardType()+" Price : "+c.getPrice());
                }
                listener.onComplete(result);


            }
        });

    }
}
