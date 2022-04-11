package com.example.anygift.Retrofit;

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @POST("users")
    Call<Void> userSignUp(@Body HashMap<String, String> map);

    @POST("users/signout")
    Call<String> userSignout(@Body HashMap<String, String> map);

    //giftCards
    @GET("cards")
    Call<String> getAllGiftcards(@Body HashMap<String, String> map);

    @POST("cards")
    Call<GiftCard> addGiftcard(@Body HashMap<String, String> map);

    @GET("cards/{id}")
    Call<GiftCard> getGiftcardById(@Body HashMap<String, String> map);

    @PUT("cards/{id}")
    Call<GiftCard> editGiftCard(@Body HashMap<String, String> map);

//    <----- Working ----->
    @POST("cards")
    Call<Card> addCard(@Body HashMap<String, Object> map);

    @POST("users/login")
    Call<LoginResult> Login(@Body HashMap<String, String> map);

    @GET("users/{id}")
    Call<com.example.anygift.Retrofit.User> getUser(@Path("id") String user_id);

    @POST("users")
    Call<com.example.anygift.Retrofit.User> addUser(@Body HashMap<String,Object> map);

    @PUT("users/{id}") //can update everything besides email and password
    Call<LoginResult> updateUser(@Path("id") String user_id, @Body HashMap<String, Object> map);

    @POST("coinTransactions")
    Call<Void> addCoinTransaction(@Body HashMap<String,Object> map);

    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("cardTypes")
    Call<List<CardType>> getAllCardTypes();

    @GET("coinTransactions/income/{id}") // WORKS!
    Call<Income> getUserIncome(@Path("id") String user_id);
    @GET("coinTransactions/outcome/{id}") // WORKS!
    Call<Outcome> getUserOutcome(@Path("id") String user_id);


}