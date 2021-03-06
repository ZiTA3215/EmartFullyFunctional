package com.oldrich.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oldrich.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VerifyActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText code;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

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

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        code = findViewById(R.id.ewealthcodeenter);
        submit = findViewById(R.id.submit);


        //Toolbar
        toolbar = findViewById(R.id.my_emartdiscount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCode = code.getText().toString();

                String final_code = "";

                if (!userCode.isEmpty()) {

                    final_code += userCode + ", ";
                }

                if (!userCode.isEmpty()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("EwealthDiscountCode", final_code);

                    firestore.collection("eWealthDiscount").document(auth.getCurrentUser().getUid())
                            .collection("UserCode").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(VerifyActivity.this, "Code was Successfully Submitted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(VerifyActivity.this, MainActivity.class));
                                finish();


                            }
                        }
                    });
                }else{
                    Toast.makeText(VerifyActivity.this, "Please Enter Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
}