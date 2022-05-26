package com.example.writocityapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.writocityapplication.fragments.HomeFragment;
import com.example.writocityapplication.fragments.SearchFragment;
import com.example.writocityapplication.fragments.UserProfileFragment;
import com.example.writocityapplication.fragments.WriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Fragment fragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.home_bottomNav);

        

        getSupportFragmentManager().beginTransaction().replace(R.id.home_FL_frame, fragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav: fragment = new HomeFragment();
                        break;
                    case R.id.search_nav: fragment = new SearchFragment();
                        break;
                    case R.id.write_nav: fragment = new WriteFragment();
                        break;
                    case R.id.user_nav:  fragment = new UserProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.home_FL_frame, fragment).commit();
                return true;
            }
        });
    }
}