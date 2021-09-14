package com.example.ecommerce.actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }


    public void joinnow(View view) {

        Intent intent = new Intent(StartActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void loginstart(View view) {

        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}