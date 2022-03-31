package com.example.anygift.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.databinding.MenuHeaderBinding;
import com.example.anygift.login.LoginActivity;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    NavController navCtr;
    FeedViewModel viewModel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView nameTv,emailTv;
    ImageView imageIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navCtr = navHost.getNavController();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.Navigation_view);
        nameTv = findViewById(R.id.menu_header_name_tv);
        emailTv = findViewById(R.id.menu_header_email_tv);
        imageIv = findViewById(R.id.menu_header_image_iv);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setMenuHeader();
        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                        Intent map = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(map);
                        break;
                    }
                    case R.id.logout: {
                        Toast.makeText(getBaseContext(),"Logging Out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void setMenuHeader() {
//        User user = Model.instance.getSignedUser();
//        MenuHeaderBinding.bind(findViewById(R.id.header_constraint));
//        nameTv.setText(user.getName());
//        emailTv.setText(user.getEmail());
//        if(user.getImageUrl()!=null) {
//            Picasso.get().load(user.getImageUrl()).into(imageIv);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}