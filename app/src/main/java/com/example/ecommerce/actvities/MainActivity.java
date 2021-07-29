package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ecommerce.R;
import com.example.ecommerce.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment;

    FirebaseAuth auth;


    Toolbar toolbar;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.home_toolbar);
        bottomNavigationView = findViewById(R.id.bottombar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.logo3_launcher_round);


        homeFragment = new HomeFragment();
        loadFragment(homeFragment);

        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.explore:


                        startActivity(new Intent(MainActivity.this, ShowAllActivity.class));

                        return true;

                    case R.id.settings:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));


                        return true;

                    case R.id.account:

                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));


                        return true;



                    default:
                        return false;
                }



            }
        });
    }


    private void loadFragment(Fragment homeFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,  menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {

            auth.signOut();
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            finish();

        } else if (id == R.id.menu_my_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));

        }


        return true;




    }



}




