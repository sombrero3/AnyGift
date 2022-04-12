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
    //    <----- Working ----->
    @POST("cards")
    Call<Card> addCard(@Body HashMap<String, Object> map);

    @GET("cards")
    Call<List<Card>> getAllCards();

    @PUT("cards/{id}")
    Call<Card> updateCard(@Path("id") String card_id, @Body HashMap<String, Object> map);

    @GET("cards/owner/{id}")
    Call<List<Card>> getAllUserCards(@Path("id") String user_id);

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