package com.example.anygift.Retrofit;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RetrofitInterface {
    //    <----- Working ----->
    @POST("cards")
    Call<Card> addCard(@Body HashMap<String, Object> map,  @Header("Authorization") String token);

    @GET("cards")
    Call<List<Card>> getAllCards(@Header("Authorization") String token);

    @PUT("cards/{id}")
    Call<Card> updateCard(@Path("id") String card_id, @Body HashMap<String, Object> map, @Header("Authorization") String token);

    @GET("cards/{id}")
    Call<Card> getCard(@Path("id") String card_id, @Header("Authorization") String token);

    @GET("cards/owner/{id}")
    Call<List<Card>> getAllUserCards(@Path("id") String user_id, @Header("Authorization") String token);

    @POST("users/login")
    Call<com.example.anygift.Retrofit.User> Login(@Body HashMap<String, Object> map);

    @POST("users/logout/{id}")
    Call<Void> Logout(@Path("id") String user_id, @Header("Authorization") String token);

    @POST("users/refreshToken/{id}")
    Call<com.example.anygift.Retrofit.User> refreshToken(@Path("id") String user_id, @Header("Authorization") String token);

    @GET("users/{id}")
    Call<com.example.anygift.Retrofit.User> getUser(@Path("id") String user_id, @Header("Authorization") String token);

    @POST("users/authenticate")
    Call<Boolean> authenticateToken(@Header("Authorization") String token);

    @POST("users")
    Call<com.example.anygift.Retrofit.User> addUser(@Body HashMap<String,Object> map);

    @PUT("users/{id}") //can update everything besides email and password
    Call<com.example.anygift.Retrofit.User> updateUser(@Path("id") String user_id, @Body HashMap<String, Object> map, @Header("Authorization") String token);

    @POST("users/coins/{id}") //can update everything besides email and password
    Call<com.example.anygift.Retrofit.User> addCoinsToUser(@Path("id") String user_id, @Body HashMap<String,Double> coins, @Header("Authorization") String token);

    @POST("coinTransactions")
    Call<Void> addCoinTransaction(@Body HashMap<String,Object> map, @Header("Authorization") String token);

    @GET("categories")
    Call<List<Category>> getAllCategories(@Header("Authorization") String token);

    @GET("cardTypes")
    Call<List<CardType>> getAllCardTypes(@Header("Authorization") String token);

    @GET("coinTransactions/income/{id}") // WORKS!
    Call<Income> getUserIncome(@Path("id") String user_id, @Header("Authorization") String token);
    @GET("coinTransactions/outcome/{id}") // WORKS!
    Call<Outcome> getUserOutcome(@Path("id") String user_id, @Header("Authorization") String token);

    @Multipart
    @POST("images/upload")
    Call<UploadImageResult> uploadImage(@Part MultipartBody.Part image);

    @GET("images/{imageName}")
    Call<ResponseBody> downloadImage(@Path("imageName") String image_name);


}
