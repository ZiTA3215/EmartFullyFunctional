package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.MyCartAdapter;
import com.example.ecommerce.fragments.HomeFragment;
import com.example.ecommerce.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment;

    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    Context context;

    MenuItem menuItem;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    TextView badgecounter;
    int pendingnotifications;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.home_toolbar);
        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setItemIconTintList(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.emart7_launcher_round);

        Intent intentbackgroundservice = new Intent(this, PushNotifications.class);
        startService(intentbackgroundservice);

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);


        homeFragment = new HomeFragment();
        loadFragment(homeFragment);


        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.messages:


                        startActivity(new Intent(MainActivity.this, MessagesActivity.class));

                        return true;


                    case R.id.account:

                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));


                        return true;

                    case R.id.shipping:

                        startActivity(new Intent(MainActivity.this, ShippingActivity.class));


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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.menu_my_cart);
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                        String documentId = doc.getId();


                        MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                        myCartModel.setDocumentId(documentId);
                        cartModelList.add(myCartModel);
                        cartAdapter.notifyDataSetChanged();
                    }

                    calculateTotalAmount(cartModelList);


                }


            }

            private void calculateTotalAmount(List<MyCartModel> cartModelList) {
                int totalAmount = 0;
                for (MyCartModel myCartModel : cartModelList) {

                    totalAmount += myCartModel.getCartbadge();
                }


                if (totalAmount == 0) {
                    menuItem.setActionView(null);
                } else {
                    menuItem.setActionView(R.layout.notification_badge);
                    View view = menuItem.getActionView();
                    badgecounter = view.findViewById(R.id.cart_badge_textview);
                    badgecounter.setText(String.valueOf(totalAmount));
                    menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, CartActivity.class));
                        }
                    });


                }

            }

        });


        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {

            auth.signOut();
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();

        } else if (id == R.id.menu_my_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));


        } else if (id == R.id.menu_ewalth_discount) {
            startActivity(new Intent(MainActivity.this, DialogActivity2.class));


        }


        return true;


    }

}





















