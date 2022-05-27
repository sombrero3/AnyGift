package com.example.anygift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.anygift.Retrofit.User;
import com.example.anygift.feed.MainActivity;
import com.example.anygift.login.LoginActivity;
import com.example.anygift.model.Model;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    TextView title1Tv,title2Tv,title3Tv,title4Tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String user_id = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("id", "");
        String token = MyApplication.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE).getString("accessToken", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);

        title1Tv = findViewById(R.id.splash_title_1_tv);
        title2Tv = findViewById(R.id.splash_title_2_tv);
        title3Tv = findViewById(R.id.splash_title_3_tv);
        title4Tv = findViewById(R.id.splash_title_4_tv);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        title1Tv.setAnimation(topAnim);
        title2Tv.setAnimation(topAnim);
        title3Tv.setAnimation(topAnim);
        title4Tv.setAnimation(bottomAnim);


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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);

    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}