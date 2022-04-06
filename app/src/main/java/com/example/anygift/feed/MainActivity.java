package com.example.anygift.feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.databinding.ActivityMainBinding;
import com.example.anygift.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    NavController navCtr;
    FeedViewModel viewModel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView nameTv,emailTv;
    ImageView imageIv;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navCtr = navHost.getNavController();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        binding.feedActivityViewPager.setAdapter(new PageViewAdapter(getSupportFragmentManager()));
        binding.feedActivityTabLayout.setupWithViewPager(binding.feedActivityViewPager);



        nameTv = findViewById(R.id.menu_header_name_tv);
        emailTv = findViewById(R.id.menu_header_email_tv);
        imageIv = findViewById(R.id.menu_header_image_iv);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        navigationView = findViewById(R.id.Navigation_view);
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
                    case R.id.menu_profile: {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navCtr.navigate(R.id.action_global_userProfileFragment);
                        break;
                    }
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
                    case R.id.logout: {
                        Toast.makeText(getBaseContext(),"Logging Out",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        drawerLayout.closeDrawer(GravityCompat.START);
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

class PageViewAdapter extends FragmentPagerAdapter{

    public PageViewAdapter(@NonNull FragmentManager fn){
        super(fn);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return null;
            case 1: return null;
            case 2: return null;
            default: return null;

        }
//        switch(position){
//            case 0:
//                return new FeedFragment();
//                break;
//            case 1:
//                return new MyCardsFragment();
//                break;
//            case 2: return null;
//            default: return null;
//        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      String title =null;
        switch(position){
            case 0: title="HOME";
            case 1: title="WALLET";
            case 2: title="SEARCH";
        }
      return title;
    }
}