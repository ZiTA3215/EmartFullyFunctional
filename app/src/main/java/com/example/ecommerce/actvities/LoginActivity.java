package com.example.ecommerce.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText name, email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        //getSupportActionBar().hide();
        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
    }

        public void signin(View view) {

            String useremail = email.getText().toString();
            String userpasswrord = password.getText().toString();


            if (TextUtils.isEmpty(useremail)) {
                Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
                return;

            }


            if (TextUtils.isEmpty(userpasswrord)) {
                Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
                return;

            }


            if (userpasswrord.length() < 6) {

                Toast.makeText(this, "Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(useremail, userpasswrord)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            } else {

                                Toast.makeText(LoginActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }

        public void signup(View view){
            startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            finish();
        }

    }

