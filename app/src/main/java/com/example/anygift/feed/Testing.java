package com.example.anygift.feed;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.User;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Testing {

    public void logout() {
        Model.instance.logout(new Model.StringListener() {
            @Override
            public void onComplete(String message) {
                System.out.println(message);
            }
        });
    }

    //TODO MAHTEL
    public void getallcardsByCardType() {
        Model.instance.getAllFeedCardsForSale(new Model.cardsReturnListener() {
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
                        }
                    }
                });
            }
        });
    }

    public void getUser() {
        Model.instance.getUserRetrofit("6256a44c1e29497041970877", new Model.userReturnListener() {
            @Override
            public void onComplete(User user, String message) {
                System.out.println(user);
                logout();
            }
        });
    }

    public Testing() {
//        SharedPreferences userDetails = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = userDetails.edit();
//                Model.instance.getAllCategories(new Model.categoriesReturnListener() {
//            @Override
//            public void onComplete(List<Category> cts)
//            {
//                System.out.println(cts);
//            }
//        });
//
//        Model.instance.getUserRetrofit("6252d5c4075b499f9d50bd8f", new Model.userReturnListener() {
//            @Override
//            public void onComplete(com.example.anygift.Retrofit.User user)
//            {
//                System.out.println(user);
//            }
//        });

//        HashMap<String,Object> map = com.example.anygift.Retrofit.CoinTransaction.mapToAddCoinTransaction("625416d119c6d1dcb6a1e6b3","6252d5c4075b499f9d50bd8f",88888.0);
//        Model.instance.addCoinTransaction(map, new Model.coinTransactionListener() {
//            @Override
//            public void onComplete(String message)
//            {
//                System.out.println(message);
//            }
//        });


//        HashMap<String,Object> map= com.example.anygift.Retrofit.User.mapToAddUser("Guy","Levy",
//                "guy@gmail.com","1234","jajaja","dsadsa","05050",false);
//        Model.instance.addUserRetrofit(map, new Model.userReturnListener() {
//            @Override
//            public void onComplete(com.example.anygift.Retrofit.User user)
//            {
//                System.out.println(user);
//            }
//        });

//        HashMap<String,Object> m = new HashMap<>();
//        m.put("address","newAdress");
//        String user_id = "625479cfbb9153f74e6b2cfc";
//        HashMap<String,Object> map2 = com.example.anygift.Retrofit.User.mapToUpdateUser(user_id,m);
//        Model.instance.updateUser(user_id, map2, new Model.userLoginListener() {
//            @Override
//            public void onComplete(LoginResult loginResult, String message)
//            {
//                System.out.println(message);
//                System.out.println(loginResult);
//            }
//        });

        getallcardsByCardType();

//        HashMap<String, Object> map = com.example.anygift.Retrofit.User.mapToLogin("omer@gmail.com", "1234");
//        Model.instance.login(map, new Model.userLoginListener() {
//            @Override
//            public void onComplete(com.example.anygift.Retrofit.User loginResult, String message) {
//                System.out.println(message);
//
//            }
//        });


        ;
        ;
//        HashMap<String,Object> m = new HashMap<>();
//        m.put("cardNumber","4444");
//        String card_id = "62548a8eb25ae8a7e9d2459d";
//        HashMap<String,Object> map = Card.mapToUpdateCard(card_id,m);
//
//        Model.instance.updateCardRetrofit(card_id,map, new Model.cardReturnListener() {
//            @Override
//            public void onComplete(Card card,String message) {
//                //use this loginResult
//                System.out.println(card);
//                System.out.println(message);
//            }
//        });
//        System.out.println(Utils.convertDateToLong("1","1","2023"));
//        System.out.println(Utils.ConvertLongToDate(1672531200000L));


//        HashMap<String,Object> m = new HashMap<>();
//        m.put("cardNumber","4444");
//        String card_id = "62548a8eb25ae8a7e9d2459d";
//        HashMap<String,Object> map = Card.mapToUpdateCard(card_id,m);

//        Model.instance.deleteCardRetrofit(card_id, new Model.cardReturnListener() {
//            @Override
//            public void onComplete(Card card,String message) {
//                System.out.println(card);
//                System.out.println(message);
//            }
//        });

    }
}
