package com.example.anygift.Retrofit;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/signin")
    Call<LoginResult> executeLogin(@Body HashMap<String,String> map);

}
