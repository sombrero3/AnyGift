package com.example.anygift.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.widget.Toast;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.anygift.MyApplication;
import com.example.anygift.Retrofit.LoginResult;
import com.example.anygift.Retrofit.RetrofitInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model {
    public static final Model instance = new Model();
    public ModelFirebase modelFirebase = new ModelFirebase();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public User signedUser;
    public ModelRetrofit  modelRetrofit= new ModelRetrofit();
    public MutableLiveData<List<GiftCard>> giftCardsList = new MutableLiveData<>();

    private Model() {
        signedUser = new User();
        ListLoadingState.setValue(GiftListLoadingState.loaded);
    }

    public LiveData<List<GiftCard>> getAll() {
        if (giftCardsList.getValue() == null) {
            refreshGiftCardsList();
        }
        return giftCardsList;
    }

//Node- Retrofit
    public interface StringListener{
       void onComplete(String message);
    }
    public interface userReturnListener{
        void onComplete(User user);
    }

    public void login(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.login(map, listener);
    }

    public void signOutRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.signOut(map, listener);
    }

    public void signUpRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.signUp(map, listener);
    }

    public void addGiftCardRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.addGiftCard(map, listener);
    }
    public void getAllGiftCardsRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.getAllGiftCards(map, listener);
    }
    public void getGiftCardByIdRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.getGiftCardById(map, listener);
    }
    public void editGiftCardRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.editGiftCard(map, listener);
    }
    public void getByPriceGiftCardsRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.getByPriceGiftCards(map, listener);
    }

    public void getUserRetrofit(HashMap<String, String> map, userReturnListener listener) {
        modelRetrofit.getUser(map, listener);
    }

    public void editUserRetrofit(HashMap<String, String> map, StringListener listener) {
        modelRetrofit.editUser(map, listener);
    }






    //Firebase
    public enum GiftListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<GiftListLoadingState> ListLoadingState = new MutableLiveData<GiftListLoadingState>();

    public MutableLiveData<GiftListLoadingState> getListLoadingState() {
        return ListLoadingState;
    }

    public interface GetAllGiftCardListener {
        void onComplete();
    }
    public interface GetUserListener{
        void onComplete(User user);
    }

    public void refreshGiftCardsList() {
        ListLoadingState.setValue(GiftListLoadingState.loading);

        //1. get local last update date
        final SharedPreferences sp = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        executor.execute(() -> {
            List<GiftCard> gcList = AppLocalDb.db.giftCardDao().getAll();
            giftCardsList.postValue(gcList);
        });
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllProducts(lastUpdated, new ModelFirebase.GetAllGiftCardListener() {
            @Override
            public void onComplete(List<GiftCard> result) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        long lastU = 0;
                        for (GiftCard gf : result) {
                            AppLocalDb.db.giftCardDao().insertAll(gf);
                            if (gf.getLastUpdated() > lastU) {
                                lastU = gf.getLastUpdated();
                            }
                        }
                        MyApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("lastUpdated", lastU)
                                .commit();

                        List<GiftCard> stList = AppLocalDb.db.giftCardDao().getAll();
                        giftCardsList.postValue(stList);
                        ListLoadingState.postValue(GiftListLoadingState.loaded);


                    }
                });
            }
        });
    }


    public interface AddGiftCardListener {
        void onComplete();
    }
    public void addGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.addGiftCard(giftCard, listener);
    }


    public void updateGiftCard(final GiftCard giftCard, final AddGiftCardListener listener) {
        modelFirebase.updateGiftCard(giftCard, listener);
    }


    public interface GetGiftCardListener {
        void onComplete(GiftCard giftCard);
    }

    public void getGiftCard(String email, GetGiftCardListener listener) {
        modelFirebase.getGiftCard(email, listener);
    }

     /*
     //upload image
      public interface Listener<T> {
            void onComplete(T result);
        }


      public interface UploadImageListener extends Listener<String>{ }

      */


    public interface UploadImageListener {
        void onComplete(String url);
    }



    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }

    public interface UploadUserImageListener {
        void onComplete(String url);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, final UploadUserImageListener listener) {
        modelFirebase.uploadUserImage(imageBmp, name, listener);
    }



    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }

    //user
    public interface AddUserListener {
        void onComplete();
    }

    public void addUser(final User user, final AddUserListener listener) {
        modelFirebase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void updateUser(final User user, final AddUserListener listener) {
        modelFirebase.updateUser(user, listener);
    }


    public ModelFirebase getModelFirebase() {
        return this.modelFirebase;
    }

    /**
     * Authentication
     */
    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }
    public User getSignedUser() {
        return signedUser;
    }
    public void setCurrentUser(GetUserListener listener) {
        modelFirebase.setCurrentUser(user -> {
            signedUser = user;
            listener.onComplete(user);
        });
    }
}
