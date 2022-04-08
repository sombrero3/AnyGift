package com.example.anygift.Retrofit;

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitInterface {
    //user
    @POST("users/logon")
    Call<LoginResult> executeLogin(@Body HashMap<String,String> map);
    @GET("users/{id}")
    Call<User> getUser(@Body HashMap<String,String> map);
    @PUT("users/{id}")
    Call<String> editUser(@Body HashMap<String,String> map);
    @POST("users/signup")
    Call<String> userSignUp(@Body HashMap<String,String> map);
    @POST("users/signout")
    Call<String> userSignout(@Body HashMap<String,String> map);

    //giftCards
    @GET("cards")
    Call<String> getAllGiftcards(@Body HashMap<String,String> map);
    @POST("cards")
    Call<GiftCard> addGiftcard(@Body HashMap<String,String> map);
    @GET("cards/{id}")
    Call<GiftCard> getGiftcardById(@Body HashMap<String,String> map);
    @PUT("cards/{id}")
    Call<GiftCard> editGiftCard(@Body HashMap<String,String> map);

//    @GET("giftcards/price/{pricemin=0}&{pricemax=1000000}")
//    Call<GiftCard> getByPriceGiftcards(@Body HashMap<String,String> map);
//    @GET("giftcards/store/{storeName}")
//    Call<GiftCard> getClosetStoreGiftcards(@Body HashMap<String,String> map);

}
