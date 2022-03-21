package com.oldrich.ecommerce.actvities;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.adapters.MyCartAdapter;
import com.oldrich.ecommerce.fragments.HomeFragment;
import com.oldrich.ecommerce.fragments.MesseagesFragment;
import com.oldrich.ecommerce.fragments.ProfileFragment;
import com.oldrich.ecommerce.fragments.ShippingFragment;
import com.oldrich.ecommerce.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String ONESIGNAL_APP_ID = "0ba38925-fb7b-4f75-b3c9-2dc9f3e419e8";

    Fragment homeFragment;
    BottomNavigationView bottomNavigationView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.home_toolbar);
        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,
                new HomeFragment()).commit();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.emart7_launcher_round);

        Intent intentbackgroundservice = new Intent(this, PushNotifications.class);
        startService(intentbackgroundservice);

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);




    homeFragment = new HomeFragment();
        loadFragment(homeFragment);


    }









private BottomNavigationView.OnNavigationItemSelectedListener navListner =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
              switch (item.getItemId()){
                  case R.id.home:
                     selectedFragment = new HomeFragment();
                      break;

                  case R.id.shipping:
                      selectedFragment = new ShippingFragment();
                      break;


                  case R.id.messages:
                      selectedFragment = new MesseagesFragment();
                      break;


                  case R.id.account:
                      selectedFragment = new ProfileFragment();
                      break;



              }

              getSupportFragmentManager().beginTransaction().replace(R.id.home_container,
                      selectedFragment).commit();

              return true;
            }
        };




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

























