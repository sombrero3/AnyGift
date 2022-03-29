package com.example.anygift.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.anygift.R;

public class LoginActivity extends AppCompatActivity {
    NavController navCtl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_nav);
        navCtl = navHost.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navCtl);
    }
}