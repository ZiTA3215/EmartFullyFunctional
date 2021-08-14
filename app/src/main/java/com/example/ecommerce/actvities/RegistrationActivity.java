package com.example.ecommerce.actvities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
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

    SharedPreferences sharedPreferences;

    FirebaseDatabase database;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //getSupportActionBar().hide();



        if (auth.getCurrentUser() !=null){

            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            finish();
        }
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);


        /*

        sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("FirstTime", true);

        if (isFirstTime){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firsttime", false);
            editor.commit();

            //Currently determines where the application starts just change Main activity to onboarding

            Intent intent = new Intent(RegistrationActivity.this,OnBoardingActivity.class);

            startActivity(intent);
            finish();
        }

*/

    }

        public void signup(View view) {

            String username = name.getText().toString();
            String useremail = email.getText().toString();
            String userpasswrord = password.getText().toString();
            if (TextUtils.isEmpty(username)) {

                Toast.makeText(this, "Enter Name!", Toast.LENGTH_SHORT).show();
                return;


            }
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

                                    Toast.makeText(RegistrationActivity.this, "Registration Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });




                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                }

                public void signin (View view){
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }

            }
