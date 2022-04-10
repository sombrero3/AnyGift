package com.example.anygift.model;

import com.example.anygift.Retrofit.LoginResult;
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

    public void login(HashMap<String, String> map, Model.StringListener listener) {
        Call<LoginResult> call = retrofitInterface.executeLogin(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Sign in complete");

                } else if (response.code() == 400) {
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

    public List<CardType> getAllCardTypes(Model.cardTypesListener listener) {
        Call<List<CardType>> call = retrofitInterface.getAllCardTypes();
        List<CardType> list = new ArrayList<>();
        call.enqueue(new Callback<List<CardType>>() {
            @Override
            public void onResponse(Call<List<CardType>> call, Response<List<CardType>> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {
                        for(int i=0; i<response.body().size(); i++){
                            CardType ct = new CardType();
                            ct.setId(response.body().get(i).getId());
                            ct.setName(response.body().get(i).getName());
                            ct.setCategory(response.body().get(i).getCategory());
                            list.add(ct);
                        }
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
        return list;
    }


    public void getAllCategories() {
        Call<List<Category>> call = retrofitInterface.getAllCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.code() == 200) {
                    for(int i=0; i<response.body().size(); i++){
                        System.out.println(response.body().get(i).getId());
                        System.out.println(response.body().get(i).getName());
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
    }

    public void getUser(HashMap<String, String> map, Model.userReturnListener listener) {

        Call<User> call = retrofitInterface.getUser(map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    User user = response.body();
                    listener.onComplete(user);


                } else if (response.code() == 400) {
                    // listener.onComplete("user exist");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onComplete(null);
            }
        });
    }

    public void editUser(HashMap<String, String> map, Model.StringListener listener) {
        Call<String> call = retrofitInterface.editUser(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant edit");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void signOut(HashMap<String, String> map, Model.StringListener listener) {
        Call<String> call = retrofitInterface.userSignout(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant exit");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void signUp(HashMap<String, String> map, Model.StringListener listener) {
        Call<String> call = retrofitInterface.userSignUp(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant sign in");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void getAllGiftCards(HashMap<String, String> map, Model.StringListener listener) {
        Call<String> call = retrofitInterface.getAllGiftcards(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant get the gift Cards");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void addGiftCard(HashMap<String, String> map, Model.StringListener listener) {
        Call<GiftCard> call = retrofitInterface.addGiftcard(map);
        call.enqueue(new Callback<GiftCard>() {
            @Override
            public void onResponse(Call<GiftCard> call, Response<GiftCard> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant add gift cards");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GiftCard> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void getGiftCardById(HashMap<String, String> map, Model.StringListener listener) {
        Call<GiftCard> call = retrofitInterface.getGiftcardById(map);
        call.enqueue(new Callback<GiftCard>() {
            @Override
            public void onResponse(Call<GiftCard> call, Response<GiftCard> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant get the gift card");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GiftCard> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void editGiftCard(HashMap<String, String> map, Model.StringListener listener) {
        Call<GiftCard> call = retrofitInterface.editGiftCard(map);
        call.enqueue(new Callback<GiftCard>() {
            @Override
            public void onResponse(Call<GiftCard> call, Response<GiftCard> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant edit gift card");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GiftCard> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }

    public void getByPriceGiftCards(HashMap<String, String> map, Model.StringListener listener) {
        Call<GiftCard> call = retrofitInterface.getByPriceGiftcards(map);
        call.enqueue(new Callback<GiftCard>() {
            @Override
            public void onResponse(Call<GiftCard> call, Response<GiftCard> response) {
                if (response.code() == 200) {
                    //Toast.makeText(getContext(), "Login to app", Toast.LENGTH_LONG).show();
                    listener.onComplete("Done");

                } else if (response.code() == 400) {
                    listener.onComplete("cant get the gift Cards");
                    //Toast.makeText(getContext(), "Cant Login To App Right Now....", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<GiftCard> call, Throwable t) {
                listener.onComplete(t.getMessage());
            }
        });

    }



}
