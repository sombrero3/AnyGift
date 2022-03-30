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
    @POST("/user/signin")
    Call<LoginResult> executeLogin(@Body HashMap<String,String> map);
    @GET("/user/{email}")
    Call<User> getUser(@Body HashMap<String,String> map);
    @PUT("/user/{email}")
    Call<User> editUser(@Body HashMap<String,String> map);
    @POST("/user/signup")
    Call<User> userSignUp(@Body HashMap<String,String> map);
    @POST("/user/signout")
    Call<User> userSignout(@Body HashMap<String,String> map);

    //giftCards
    @GET("/giftcards")
    Call<GiftCard> getAllGiftcards(@Body HashMap<String,String> map);
    @POST("/giftcards")
    Call<GiftCard> addGiftcard(@Body HashMap<String,String> map);
    @GET("/giftcards/{id}")
    Call<GiftCard> getGiftcardById(@Body HashMap<String,String> map);
    @PUT("/giftcards/{id}")
    Call<GiftCard> editGiftCard(@Body HashMap<String,String> map);
    @GET("/giftcards/price/{pricemin=0}&{pricemax=1000000}")
    Call<GiftCard> getByPriceGiftcards(@Body HashMap<String,String> map);
    @GET("/giftcards/store/{storeName}")
    Call<GiftCard> getClosetStoreGiftcards(@Body HashMap<String,String> map);

}
