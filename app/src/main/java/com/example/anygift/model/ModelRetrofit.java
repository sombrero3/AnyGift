package com.example.anygift.model;

import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelRetrofit {
    public RetrofitInterface retrofitInterface;
    private String Base_URL="http://10.0.2.2:8000";

    public ModelRetrofit(){
        retrofitInterface=new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface.class);
    }

    public void login(HashMap<String, String> map, Model.StringListener listener) {
        Call<LoginResult> call=retrofitInterface.executeLogin(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response)
            {
                if(response.code()==200)
                {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Sign in complete");

                }else
                if(response.code()==400){
                    listener.onComplete("user exist");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });
    }
}
