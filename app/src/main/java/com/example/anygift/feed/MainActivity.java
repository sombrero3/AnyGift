package com.example.anygift.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.SellerRatings;
import com.example.anygift.Retrofit.User;
import com.example.anygift.login.LoginActivity;
import com.example.anygift.model.Model;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavController navCtr;
    FeedViewModel viewModel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView nameTv,emailTv,numUnlikeTv,numLikeTv,coinsTv;
    ImageView imageIv,verifiedIv;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navCtr = navHost.getNavController();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        user = new User();
        user = Model.instance.getSignedUser();

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.Navigation_view);

        //--drawer navigation menu--//
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setItemIconTintList(null);
        setMenuHeader();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_feed: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_feedFragment);
                        break;
                    }
//                    case R.id.menu_profile: {
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        navCtr.navigate(R.id.action_global_userProfileFragment);
//                        break;
//                    }
                    case R.id.menu_cards: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_myCardsFragment);
                        break;
                    }
                    case R.id.add_card: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_addCardFragment);
                        break;
                    }
                    case R.id.cards_map: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent map = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(map);
                        break;
                    }
                    case R.id.shop: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Bundle args = new Bundle();
                        args.putString("cardId","");
                        navCtr.navigate(R.id.action_global_shopFragment,args);
                        break;
                    }
                    case R.id.transactions:{
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_transactionsFragment);
                        break;
                    }
                    case R.id.menu_search: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_searchGiftCardFragment);
                        break;
                    }
                    case R.id.logout: {
                        logout();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void logout() {
        Model.instance.logout(new Model.StringListener() {
            @Override
            public void onComplete(String message) {
                Toast.makeText(getBaseContext(),"Logging Out",Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void setMenuHeader() {

        System.out.println(user);
        View header= (View)navigationView.getHeaderView(0);
        nameTv = header.findViewById(R.id.menu_header_name_tv);
        emailTv = header.findViewById(R.id.menu_header_email_tv);
        imageIv = header.findViewById(R.id.menu_header_image_iv);
        numUnlikeTv = header.findViewById(R.id.header_num_unlike_tv);
        numLikeTv = header.findViewById(R.id.header_num_like_tv);
        verifiedIv = header.findViewById(R.id.header_verification_iv);
        coinsTv = header.findViewById(R.id.header_coins_tv);
        nameTv.setText(  user.getFirstName() + " " + user.getLastName());
        emailTv.setText( user.getEmail());
        if(user.getVerified()){
            verifiedIv.setVisibility(View.VISIBLE);
        }
        if(Model.instance.getSignedUser().getCoins() == null){
            Model.instance.getSignedUser().setCoins(0);
        }else {
            coinsTv.setText(Model.instance.getSignedUser().getCoins().toString());
        }

        Model.instance.getSellerRatings(Model.instance.getSignedUser().getId(), new Model.sellerRatingsListener() {
            @Override
            public void onComplete(SellerRatings sr) {
                if(sr!=null) {
                    numLikeTv.setText("" + sr.getGood());
                    numUnlikeTv.setText("" + sr.getBad());

                    if (user.getProfilePicture() != null && user.getProfilePicture().compareTo("") != 0) {
                        Model.instance.downloadImage(user.getProfilePicture().replace("/image/", ""),
                                new Model.byteArrayReturnListener() {
                                    @Override
                                    public void onComplete(Bitmap bitmap) {
                                        if (bitmap == null) {
                                            return;
                                        }
                                        imageIv.setImageBitmap(bitmap);
                                        imageIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        imageIv.setClipToOutline(true);

                                    }
                                });
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}