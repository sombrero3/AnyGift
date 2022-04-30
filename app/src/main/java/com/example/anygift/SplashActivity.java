package com.example.anygift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.anygift.Retrofit.User;
import com.example.anygift.feed.MainActivity;
import com.example.anygift.login.LoginActivity;
import com.example.anygift.model.Model;
import com.example.anygift.model.ModelRetrofit;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String user_id = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("id", "");
        String token = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("accessToken", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Model.instance.isTokenValid(new Model.booleanReturnListener() {
                @Override
                public void onComplete(Boolean result, String message) {
                    if (result) {
                        Model.instance.mainThread.post(() -> {
                            Model.instance.getUserRetrofit(user_id, new Model.userReturnListener() {
                                @Override
                                public void onComplete(User user,String message) {
                                    user.setAccessToken(Model.instance.modelRetrofit.getAccessToken());
                                    user.setRefreshToken(Model.instance.modelRetrofit.getRefreshToken());
                                    Model.instance.setCurrentUser(user);
                                    Model.instance.setCardTypes(new Model.VoidListener() {
                                        @Override
                                        public void onComplete() {
                                            toFeedActivity();
                                        }
                                    });

                                }
                            });
                        });
                    } else {
                        Model.instance.mainThread.post(() -> {
                            toLoginActivity();
                        });
                    }
                }
            });
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}