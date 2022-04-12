package com.example.anygift.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.Retrofit.Outcome;
import com.example.anygift.Retrofit.RetrofitInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelRetrofit {
    public RetrofitInterface retrofitInterface;
    public String Base_URL = "http://10.0.2.2:8001/api/v1/";

    public ModelRetrofit() {
        retrofitInterface = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface.class);
    }

    public void login(HashMap<String, String> map, Model.userLoginListener listener) {
        Call<LoginResult> call = retrofitInterface.Login(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    LoginResult loginResult = response.body();
                    listener.onComplete(loginResult, "Logging Succeeded");

                } else if (response.code() == 400) {
                    listener.onComplete(null,"Login Failed");

                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                listener.onComplete(null,t.getMessage());
            }
        });
    }

    public void addCoinTransaction(HashMap<String,Object> map, Model.coinTransactionListener listener) {

        Call<Void> call = retrofitInterface.addCoinTransaction(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete("coinTransaction Added");

                } else if (response.code() == 400) {
                    System.out.println(response.message());
                    // listener.onComplete("user exist");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();
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

    public void getAllCardTypes(Model.cardTypesReturnListener listener) {
        Call<List<CardType>> call = retrofitInterface.getAllCardTypes();
        List<CardType> list = new ArrayList<>();
        call.enqueue(new Callback<List<CardType>>() {
            @Override
            public void onResponse(Call<List<CardType>> call, Response<List<CardType>> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            CardType ct = new CardType(response.body().get(i).getName(), response.body().get(i).getCategories(), response.body().get(i).getId());
                            list.add(ct);
                        }
                        listener.onComplete(list);
                    }

                } else if (response.code() == 400) {
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<List<CardType>> call, Throwable t) {
                System.out.println("Very BAD");
            }
        });
    }

    public List<Category> getAllCategories(Model.categoriesReturnListener listener) {
        Call<List<Category>> call = retrofitInterface.getAllCategories();
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

    public void getUserIncome(String user_id, Model.incomeListener listener) {

        Call<Income> call = retrofitInterface.getUserIncome(user_id);
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

    public void getUserOutCome(String user_id, Model.outComeListener listener) {

        Call<Outcome> call = retrofitInterface.getUserOutcome(user_id);
        call.enqueue(new Callback<Outcome>() {
            @Override
            public void onResponse(Call<Outcome> call, Response<Outcome> response) {
                if (response.code() == 200) {
                    Outcome outcome = response.body();
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

        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.getUser(user_id);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    listener.onComplete(user);

                } else if (response.code() == 400) {
                    // listener.onComplete("user exist");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void addUser(HashMap<String,Object> map, Model.userReturnListener listener) {

        Call<com.example.anygift.Retrofit.User> call = retrofitInterface.addUser(map);
        call.enqueue(new Callback<com.example.anygift.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.example.anygift.Retrofit.User> call, Response<com.example.anygift.Retrofit.User> response) {
                if (response.code() == 200) {
                    com.example.anygift.Retrofit.User user = response.body();
                    listener.onComplete(user);

                } else if (response.code() == 400) {
                    // listener.onComplete("user exist");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<com.example.anygift.Retrofit.User> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void updateUser(String user_id, HashMap<String,Object> map, Model.userLoginListener listener) {
        Call<LoginResult> call = retrofitInterface.updateUser(user_id, map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    LoginResult user = response.body();
                    listener.onComplete(user, "User Updated");

                } else if (response.code() == 400) {
                    // listener.onComplete("user exist");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();
                    listener.onComplete(null,"User Wasn't Managed to update");
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null, "Very Bad");
            }
        });
    }

    public void addCard(HashMap<String,Object> map, Model.cardReturnListener listener){
        Call<Card> call = retrofitInterface.addCard(map);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(@NonNull Call<Card> call, @NonNull Response<Card> response) {
                if (response.code() == 200) {
                    Card card = response.body();
                    listener.onComplete(card);

                } else if (response.code() == 400) {
                    System.out.println(response.body());
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Card> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                listener.onComplete(null);
            }
        });
    }

    public void getAllCards(Model.cardsReturnListener listener) {
        Call<List<Card>> call = retrofitInterface.getAllCards();
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
                    listener.onComplete(null,"this is bad");
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                listener.onComplete(null,t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }

    public void getAllUserCards(String user_id, Model.cardsReturnListener listener) {
        Call<List<Card>> call = retrofitInterface.getAllUserCards(user_id);
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
                    listener.onComplete(null,"this is bad");
                    System.out.println("THIS IS BAD");
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                listener.onComplete(null,t.getMessage());
                System.out.println("Very BAD");
            }
        });
    }


}
