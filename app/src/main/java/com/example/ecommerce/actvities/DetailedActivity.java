package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce.R;
import com.example.ecommerce.adapters.ShowAllAdapter;
import com.example.ecommerce.models.NewProductsModel;
import com.example.ecommerce.models.PopularProductModel;
import com.example.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DetailedActivity extends AppCompatActivity {

    ImageView imageView1, imageView2, imageView3, imageView4;
    TextView rating, name, description, price, quantity;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;
    RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;


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


      

        bottomNavigationView = findViewById(R.id.bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.explore:


                        startActivity(new Intent(DetailedActivity.this, ShowAllActivity.class));

                        return true;


                    case R.id.menu_my_cart:

                        startActivity(new Intent(DetailedActivity.this, CartActivity.class));

                        return true;


                    case R.id.menu_home:

                        startActivity(new Intent(DetailedActivity.this, MainActivity.class));

                        return true;


                    default:
                        return false;
                }


            }
        });


        String type = getIntent().getStringExtra("type");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel) {
            newProductsModel = (NewProductsModel) obj;


        } else if (obj instanceof PopularProductModel) {
            popularProductModel = (PopularProductModel) obj;
        } else if (obj instanceof ShowAllModel) {
            showAllModel = (ShowAllModel) obj;
        }


        imageView1 = (ImageView) findViewById(R.id.image_view1);
        imageView2 = (ImageView) findViewById(R.id.image_view2);
        imageView3 = (ImageView) findViewById(R.id.image_view3);
        imageView4 = (ImageView) findViewById(R.id.image_view4);

        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);


        //New Products

        if (newProductsModel != null) {
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(imageView1);
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url2()).into(imageView2);
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url3()).into(imageView3);
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url4()).into(imageView4);
            name.setText(newProductsModel.getName());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;


        }

        //Popular Products

        if (popularProductModel != null) {
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(imageView1);
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url2()).into(imageView2);
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url3()).into(imageView3);
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url4()).into(imageView4);
            name.setText(popularProductModel.getName());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel.getPrice()));
            name.setText(popularProductModel.getName());

            totalPrice = popularProductModel.getPrice() * totalQuantity;


        }

        //Show All products

        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(imageView1);
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url2()).into(imageView2);
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url3()).into(imageView3);
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url4()).into(imageView4);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;

        }


        if (type == null || type.isEmpty()) {

            firestore.collection("ShowAll")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }

        if (type != null && type.equalsIgnoreCase("Men")) {

            firestore.collection("ShowAll").whereEqualTo("type", "men")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }
        if (type != null && type.equalsIgnoreCase("Woman")) {

            firestore.collection("ShowAll").whereEqualTo("type", "woman")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }
        if (type != null && type.equalsIgnoreCase("Pets")) {

            firestore.collection("ShowAll").whereEqualTo("type", "pets")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }
        if (type != null && type.equalsIgnoreCase("camera")) {

            firestore.collection("ShowAll").whereEqualTo("type", "camera")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });


        }
        if (type != null && type.equalsIgnoreCase("Tech")) {

            firestore.collection("ShowAll").whereEqualTo("type", "tech")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                                    ShowAllModel showAllModel = doc.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();

                                }
                            }

                        }
                    });

        }


        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);

                if (newProductsModel != null) {
                    intent.putExtra("item", newProductsModel);
                }

                if (popularProductModel != null) {
                    intent.putExtra("item", popularProductModel);

                }

                if (showAllModel != null) {
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

                if (totalQuantity < 10) {

                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null) {
                        totalPrice = newProductsModel.getPrice() * totalQuantity;

                    }

                    if (popularProductModel != null) {
                        totalPrice = popularProductModel.getPrice() * totalQuantity;


                    }
                    if (showAllModel != null) {
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }

                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity > 1) {

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













