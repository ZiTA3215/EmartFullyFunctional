package com.example.ecommerce.actvities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password;
    private FirebaseAuth auth;

    ProgressBar progressBar;

    FirebaseDatabase database;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        progressBar = findViewById(R.id.PBar);


        if (!isConnected()) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Error")
                    .setMessage("Please Check Your Internet Connection\n  .Turn On WIFI\n  .Turn On Mobile Data\n  .Try Again Later")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {


        }

    }




        public void signup(View view) {
            progressBar.setVisibility(View.VISIBLE);

            String username = name.getText().toString();
            String useremail = email.getText().toString();
            String userpasswrord = password.getText().toString();
            if (TextUtils.isEmpty(username)) {

                Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                return;


            }
            if (TextUtils.isEmpty(useremail)) {
                Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                return;

            }





            if (TextUtils.isEmpty(userpasswrord)) {
                Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                return;

            }




                if (userpasswrord.length() < 6) {

                    Toast.makeText(this, "Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                auth.createUserWithEmailAndPassword(useremail, userpasswrord)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                UserModel userModel = new UserModel(username,useremail,userpasswrord);
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(userModel);

                                if (task.isSuccessful()){
                Toast.makeText(RegistrationActivity.this, "Succesfully Registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }else {

                Toast.makeText(RegistrationActivity.this, "Registration Failed" , Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
            }

        }
});

                }

                public void signin (View view){
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

            }
