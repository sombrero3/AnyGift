package com.example.anygift.feed;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Testing {
    Testing(){
//        Model.instance.getAllCategories(new Model.categoriesReturnListener() {
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


//    HashMap<String,String> map = LoginResult.mapToLogin("bbbb@gmail.com","1234");
//        Model.instance.login(map, new Model.userLoginListener() {
//            @Override
//            public void onComplete(LoginResult loginResult,String message) {
//                //use this loginResult
//                System.out.println(loginResult);
//                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//            }
//        });

        HashMap<String,Object> map = Card.mapToAddCard(192.3,250.4,"123",
                "6228b3339269dcd5a1de4f8e","625479cfbb9153f74e6b2cfc",true,(long) 1681242321);
        Model.instance.addCardRetrofit(map, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card) {
                //use this loginResult
                System.out.println(card);
            }
        });


    }
}
