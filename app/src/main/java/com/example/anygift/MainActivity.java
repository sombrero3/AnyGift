package com.example.anygift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    NavController navCtr;
    FeedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navCtr = navHost.getNavController();
        viewModel=  new ViewModelProvider(this).get(FeedViewModel.class);;
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



                    break;
                }
            }
        } else {
            return true;
        }
        return false;
    }

}