package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.anygift.MyApplication;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.EmailExists;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.Outcome;
import com.example.anygift.Retrofit.RetrofitInterface;
import com.example.anygift.Retrofit.SellerRatings;
import com.example.anygift.Retrofit.UploadImageResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

public class ModelRetrofit {
    public RetrofitInterface retrofitInterface;
    public String Base_URL = "http://193.106.55.135:8000/api/v1/";

    public ModelRetrofit() {
        retrofitInterface = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface.class);
    }

    public String getUserId() {
        return MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("id", "");
    }

    public String getAccessToken() {
        return MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("accessToken", "");
    }

    public String getRefreshToken() {
        return MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("refreshToken", "");
    }


    public void refreshToken(Model.StringListener listener) {
        String token = getRefreshToken();
        System.out.println(token);
        String user_id = getUserId();
        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.refreshToken(user_id, token);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    SharedPreferences userDetails = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = userDetails.edit();
                    assert user != null;
                    edit.putString("refreshToken", "bearer " + user.getRefreshToken());
                    edit.putString("accessToken", "bearer " + user.getAccessToken());
                    edit.putString("id", user.getId());
                    edit.apply();
                    listener.onComplete("refreshToken Succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete("refreshToken Failed");
                } else if (response.code() == 403) {
                    listener.onComplete("refresh token Failed " + response.body());
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });
    }

    public void login(HashMap<String, Object> map, Model.userLoginListener listener) {
        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.Login(map);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    SharedPreferences userDetails = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = userDetails.edit();
                    assert user != null;
                    edit.putString("accessToken", "bearer " + user.getAccessToken());
                    edit.putString("refreshToken", "bearer " + user.getRefreshToken());
                    edit.putString("id", user.getId());
                    edit.apply();
                    listener.onComplete(user, "Logging Succeeded");

                } else if (response.code() == 401) {
                    listener.onComplete(null, "Invalid Email or Password");
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
            }
        });
    }

    public void logout(Model.StringListener listener) {
        String token = getRefreshToken();
        String user_id = getUserId();
        Call<Void> call = retrofitInterface.Logout(user_id, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    SharedPreferences userDetails = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = userDetails.edit();
                    edit.putString("refreshToken", "");
                    edit.putString("accessToken", "");
                    edit.putString("id", "");
                    edit.apply();
                    listener.onComplete("Logout Succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete("Logout Failed");

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });
    }


    public void addCoinTransaction(HashMap<String, Object> map, Model.coinTransactionListener listener) {
        String token = getAccessToken();
        Call<Void> call = retrofitInterface.addCoinTransaction(map, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete("coinTransaction Added");

                } else if (response.code() == 400) {
                    System.out.println(response.message());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void getAllCardTypes(Model.cardTypesReturnListener listener) { // done
        String token = getAccessToken();
        Call<List<CardType>> call = retrofitInterface.getAllCardTypes(token);
        List<CardType> list = new ArrayList<>();
        call.enqueue(new Callback<List<CardType>>() {
            @Override
            public void onResponse(Call<List<CardType>> call, Response<List<CardType>> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            CardType ct = new CardType(response.body().get(i).getName(), response.body().get(i).getCategories(), response.body().get(i).getStores(), response.body().get(i).getId());
                            list.add(ct);
                        }
                        listener.onComplete(list);
                    }

                } else if (response.code() == 400) {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<List<CardType>> call, Throwable t) {
                listener.onComplete(null);
                System.out.println("Very BAD");
            }
        });
    }

    public List<Category> getAllCategories(Model.categoriesReturnListener listener) {
        String token = getAccessToken();
        Call<List<Category>> call = retrofitInterface.getAllCategories(token);
        List<Category> list = new ArrayList<>();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.body() != null) {
                    if (response.code() == 200) {
                        for (int i = 0; i < response.body().size(); i++) {
                            Category c = new Category(response.body().get(i).getName(), response.body().get(i).getId());
                            list.add(c);
                        }
                        listener.onComplete(list);
                    }
                } else if (response.code() == 400) {
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                System.out.println("Very BAD");
            }
        });
        return list;
    }

    public void getUserIncome(Model.incomeListener listener) {
        String token = getAccessToken();
        String user_id = getUserId();
        Call<Income> call = retrofitInterface.getUserIncome(user_id, token);
        call.enqueue(new Callback<Income>() {
            @Override
            public void onResponse(Call<Income> call, Response<Income> response) {
                if (response.code() == 200) {
                    Income income = response.body();
                    listener.onComplete(income);

                } else if (response.code() == 400) {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<Income> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void getUserOutCome(Model.outComeListener listener) {
        String token = getAccessToken();
        String user_id = getUserId();
        Call<Outcome> call = retrofitInterface.getUserOutcome(user_id, token);
        call.enqueue(new Callback<Outcome>() {
            @Override
            public void onResponse(Call<Outcome> call, Response<Outcome> response) {
                if (response.code() == 200) {
                    Outcome outcome = response.body();
                    System.out.println("HAHAHAHAHA");
                    System.out.println(outcome);
                    System.out.println("HAHAHAHAHA");
                    listener.onComplete(outcome);

                } else if (response.code() == 400) {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<Outcome> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void getUser(String user_id, Model.userReturnListener listener) {
        String token = getAccessToken();
        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.getUser(user_id, token);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    listener.onComplete(user, "getUser succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete(null, "getUser Failed");
                } else if (response.code() == 403) {
                    listener.onComplete(null, "getUser failed");
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, t.getMessage());
            }
        });
    }

    public void addUser(HashMap<String, Object> map, Model.userReturnListener listener) {

        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.addUser(map);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    listener.onComplete(user, "signup succeeded");

                } else if (response.code() == 401) {
                    listener.onComplete(null, "Signup Failed - Email already exists");
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, t.getMessage());
            }
        });
    }

    public void updateUser(HashMap<String, Object> map, Model.userLoginListener listener) {
        String user_id = getUserId();
        String token = getAccessToken();
        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.updateUser(user_id, map, token);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    listener.onComplete(user, "User Updated");

                } else if (response.code() == 400) {
                    listener.onComplete(null, "User Wasn't Managed to update");
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, "Very Bad");
            }
        });
    }

    public void addCard(HashMap<String, Object> map, Model.cardReturnListener listener) {
        String token = getAccessToken();
        Call<Card> call = retrofitInterface.addCard(map, token);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(@NonNull Call<Card> call, @NonNull Response<Card> response) {
                if (response.code() == 200) {
                    Card card = response.body();
                    listener.onComplete(card, "Add Card succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete(null, "Add Card Failed");
                } else if (response.code() == 500) {
                    listener.onComplete(null, "Add Card Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Card> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, t.getMessage());
            }
        });
    }

    public void getAllCards(Model.cardsReturnListener listener) { //done
        String token = getAccessToken();
        Call<List<Card>> call = retrofitInterface.getAllCards(token);
        List<Card> list = new ArrayList<>();
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        list.addAll(response.body());
                        listener.onComplete(list, "All Good");
                    }

                } else if (response.code() == 400) {
                    listener.onComplete(null, "this is bad");
                } else if (response.code() == 401) {
                    listener.onComplete(null, "invalid token");
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
            }
        });
    }

    public void getAllUserCards(Model.cardsReturnListener listener) { //done
        String user_id = getUserId();
        String token = getAccessToken();
        Call<List<Card>> call = retrofitInterface.getAllUserCards(user_id, token);
        List<Card> list = new ArrayList<>();
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        list.addAll(response.body());
                        listener.onComplete(list, "All Good");
                    }

                } else if (response.code() == 400) {
                    listener.onComplete(null, "this is bad");
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }

    public void updateCard(String card_id, HashMap<String, Object> map, Model.cardReturnListener listener) {
        String token = getAccessToken();
        Call<Card> call = retrofitInterface.updateCard(card_id, map, token);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(@NonNull Call<Card> call, @NonNull Response<Card> response) {
                if (response.code() == 200) {
                    Card card = response.body();
                    listener.onComplete(card, "Update Card Succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete(null, "Update Card Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Card> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, "Update Card Failed");
            }
        });
    }


    public void getCard(String card_id, Model.cardReturnListener listener) {
        String token = getAccessToken();
        Call<Card> call = retrofitInterface.getCard(card_id, token);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(@NonNull Call<Card> call, @NonNull Response<Card> response) {
                if (response.code() == 200) {
                    Card card = response.body();
                    listener.onComplete(card, "get Card Succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete(null, "get Card Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Card> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, "get Card Failed");
            }
        });
    }


    public void authenticateToken(Model.booleanReturnListener listener) {
        String token = getAccessToken();
        Call<Boolean> call = retrofitInterface.authenticateToken(token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.code() == 200) {
                    Boolean card = response.body();
                    listener.onComplete(card, "Token is Valid");

                } else if (response.code() == 403) {
                    listener.onComplete(false, "Token is invalid");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(false, "Token authentication Failed");
            }
        });
    }

    public void addCoinsToUser(String user_id, HashMap<String, Double> map, Model.userReturnListener listener) {
        String token = getAccessToken();
        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.addCoinsToUser(user_id, map, token);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    listener.onComplete(user, "User coins Updated");

                } else if (response.code() == 400) {
                    listener.onComplete(null, "User coins Wasn't Managed to update");
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, "Very Bad");
            }
        });
    }

    public void uploadImage(byte[] imageBytes, Model.uploadImageListener listener) {
        MediaType md = MediaType.parse("image/jpeg");
        okhttp3.RequestBody requestFile = okhttp3.RequestBody.create(md, imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<UploadImageResult> call = retrofitInterface.uploadImage(body, getUserId(), getAccessToken());
        call.enqueue(new Callback<UploadImageResult>() {
            @Override
            public void onResponse(Call<UploadImageResult> call, retrofit2.Response<UploadImageResult> response) {

                if (response.isSuccessful()) {

                    UploadImageResult responseBody = response.body();
                    listener.onComplete(responseBody);

                } else {
                    ResponseBody errorBody = response.errorBody();
                    System.out.println(errorBody.toString());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<UploadImageResult> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public static Bitmap convertCompressedByteArrayToBitmap(byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    public void downloadImage(String image, Model.byteArrayReturnListener listener) {
        Call<ResponseBody> call = retrofitInterface.downloadImage(image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = convertCompressedByteArrayToBitmap(response.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listener.onComplete(bitmap);

                } else {
                    ResponseBody errorBody = response.errorBody();
                    System.out.println(errorBody.toString());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }


    public void getAllUserCardsTransactions(Model.cardsTransactionsReturnListener listener) { //done
        String user_id = getUserId();
        String token = getAccessToken();
        Call<List<CardTransaction>> call = retrofitInterface.getCardsTransactionsByUserId(user_id, token);
        List<CardTransaction> list = new ArrayList<>();
        call.enqueue(new Callback<List<CardTransaction>>() {
            @Override
            public void onResponse(Call<List<CardTransaction>> call, Response<List<CardTransaction>> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        list.addAll(response.body());
                        listener.onComplete(list, "All Good");
                    }

                } else if (response.code() == 400) {
                    listener.onComplete(null, "this is bad");
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<List<CardTransaction>> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }

    public void getCardTransactionByTransactionId(String trans_id, Model.cardsTransactionReturnListener listener) { //done
        String token = getAccessToken();
        Call<CardTransaction> call = retrofitInterface.getCardsTransactionsByTransactionId(trans_id, token);
        call.enqueue(new Callback<CardTransaction>() {
            @Override
            public void onResponse(Call<CardTransaction> call, Response<CardTransaction> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        CardTransaction ct = response.body();
                        listener.onComplete(ct, "All Good");
                    }

                } else if (response.code() == 400) {
                    listener.onComplete(null, "this is bad");
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<CardTransaction> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }


    public void addCardTransaction(HashMap<String, Object> map, Model.booleanReturnListener listener) { //done
        String token = getAccessToken();
        Call<ResponseBody> call = retrofitInterface.addCardTransaction(map, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("TESTTTTTTTTTTTTTTTTTTTTTTT");
                    listener.onComplete(true, "All Good");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }


    public void checkIfEmailExists(String email, Model.booleanReturnListener listener) { //done
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        Call<EmailExists> call = retrofitInterface.getEmailExists(map);
        call.enqueue(new Callback<EmailExists>() {
            @Override
            public void onResponse(Call<EmailExists> call, Response<EmailExists> response) {

                if (response.code() == 200) {
                    EmailExists ee = response.body();
                    assert ee != null;
                    listener.onComplete(ee.getEmail_exists(), "Email exists returned");
                }
            }

            @Override
            public void onFailure(Call<EmailExists> call, Throwable t) {
                listener.onComplete(null, t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }

    public void getSellerRatings(String user_id, Model.sellerRatingsListener listener) {
        Call<SellerRatings> call = retrofitInterface.getSellerRatings(user_id, getAccessToken());
        call.enqueue(new Callback<SellerRatings>() {
            @Override
            public void onResponse(Call<SellerRatings> call, Response<SellerRatings> response) {

                if (response.code() == 200) {
                    SellerRatings ee = response.body();
                    listener.onComplete(ee);
                }
            }

            @Override
            public void onFailure(Call<SellerRatings> call, Throwable t) {
                listener.onComplete(null);
                System.out.println("Very BAD");
            }
        });
    }

    public void addCardTransSatis(String card_trans_id, HashMap<String, Object> m, Model.cardTransactionReturnListener l) {
        Call<CardTransaction> call = retrofitInterface.addCardTransSatis(card_trans_id, m, getAccessToken());
        call.enqueue(new Callback<CardTransaction>() {
            @Override
            public void onResponse(Call<CardTransaction> call, Response<CardTransaction> response) {
                if (response.code() == 200) {
                    CardTransaction ct = response.body();
                    l.onComplete(ct, "CardTransaction updated");
                }
            }

            @Override
            public void onFailure(Call<CardTransaction> call, Throwable t) {
                l.onComplete(null, "Fail");
            }
        });
    }

}
