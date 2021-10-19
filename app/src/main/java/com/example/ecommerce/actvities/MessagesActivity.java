package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button enterchat;
    Button request;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        toolbar = findViewById(R.id.my_shipping_toolbar);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        enterchat = findViewById(R.id.submit1);
        request = findViewById(R.id.submit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_244);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.chatzy.com/sign.htm";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveCurrentTime, saveCurrentDate;
                Calendar callForDate = Calendar.getInstance();


                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(callForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(callForDate.getTime());



                Map<String,Object> mMap = new HashMap<>();
                mMap.put("date",saveCurrentDate);
                mMap.put("time",saveCurrentTime);
                firestore.collection("eMartChatroom").document(auth.getCurrentUser().getUid())
                        .collection("UserRequest").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MessagesActivity.this, "Request Successful", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(MessagesActivity.this, MainActivity.class));

                        } else {

                            Toast.makeText(MessagesActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }


                    }

                });

            }
        });
    }


}