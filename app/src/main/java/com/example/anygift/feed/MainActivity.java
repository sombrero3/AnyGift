package com.example.anygift.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    NavController navCtr;
    FeedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navCtr = navHost.getNavController();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.menu_feed: {
                    navCtr.navigate(R.id.action_global_feedFragment);
                    break;
                }
                case R.id.menu_profile: {
                    navCtr.navigate(R.id.action_global_userProfileFragment);
                    break;
                }
                case R.id.menu_cards: {
                    navCtr.navigate(R.id.action_global_myCardsFragment);
                    break;
                }
                case R.id.add_card: {
                    navCtr.navigate(R.id.action_global_addCardFragment);
                    break;
                }
                case R.id.cards_map: {

                    Intent map = new Intent(this, MapsActivity.class);
                    startActivity(map);

                    break;
                }
                case R.id.logout: {
                    Toast.makeText(this,"Logging Out",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        } else {
            return true;
        }
        return false;
    }

}