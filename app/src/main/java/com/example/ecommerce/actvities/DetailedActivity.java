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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.models.NewProductsModel;
import com.example.ecommerce.models.PopularProductModel;
import com.example.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView  name, description, price, quantity;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    int totalQuantity = 1;
    int totalPrice = 0;

    //New Products


    NewProductsModel newProductsModel = null;

    //Popular Products

    PopularProductModel popularProductModel = null;

    //Show All products
    ShowAllModel showAllModel = null;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.home_toolbar);
        bottomNavigationView = findViewById(R.id.bottombar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firestore = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel) {
            newProductsModel = (NewProductsModel) obj;


        } else if (obj instanceof PopularProductModel) {
            popularProductModel = (PopularProductModel) obj;
        } else if (obj instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) obj;
        }


        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //New Products

        if (newProductsModel != null) {
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;


        }

        //Popular Products

        if (popularProductModel != null) {
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel.getPrice()));
            name.setText(popularProductModel.getName());

            totalPrice =popularProductModel.getPrice() * totalQuantity;


        }

        //Show All products

        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;

        }
        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);

               if(newProductsModel != null){
                   intent.putExtra("item",newProductsModel);
               }

               if(popularProductModel !=null){
                   intent.putExtra("item",popularProductModel);

               }

                if(showAllModel !=null) {
                    intent.putExtra("item", showAllModel);

                }

               startActivity(intent);
            }
        });
        //Add To Cart

        addToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToCart();

            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity<10){

                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null){
                        totalPrice = newProductsModel.getPrice() * totalQuantity;

                    }

                    if (popularProductModel != null){
                        totalPrice = popularProductModel.getPrice() * totalQuantity;


                    }if (showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }

                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity > 1){

                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                }

            }
        });
    }

    private void addToCart() {

        String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM DD, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();

                finish();

            }



        });








    }



}






