package com.oldrich.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.models.NewProductsModel;
import com.oldrich.ecommerce.models.PopularProductModel;
import com.oldrich.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    Toolbar toolbar;





    Context context;
    int cartbadge = 1;


    ImageView imageView1, imageView2, imageView3;
    TextView rating, name, description, price, quantity;
    String imgurl, imgurl2, imgurl3;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;

  //show all on detail view may implement in the future
   /* RecyclerView recyclerView;
    ShowAllAdapter showAllAdapter;
    List<ShowAllModel> showAllModelList;
*/



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



        toolbar = findViewById(R.id.my_detailtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//this is for show all on detail view
       // String type = getIntent().getStringExtra("type");

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


        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);


//for similar items on detail view
      /*  recyclerView = findViewById(R.id.show_all_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(this, showAllModelList);
        recyclerView.setAdapter(showAllAdapter);
*/
        context = this;


        //New Products

        if (newProductsModel != null) {
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(imageView1);
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url2()).into(imageView2);
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url3()).into(imageView3);

            name.setText(newProductsModel.getName());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(newProductsModel.getImg_url2()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });

            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(newProductsModel.getImg_url3()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });


        }

        //Popular Products

        if (popularProductModel != null) {
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(imageView1);
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url2()).into(imageView2);
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url3()).into(imageView3);

            name.setText(popularProductModel.getName());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel.getPrice()));
            name.setText(popularProductModel.getName());

            totalPrice = popularProductModel.getPrice() * totalQuantity;

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(popularProductModel.getImg_url2()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });

            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(popularProductModel.getImg_url3()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });




        }

        //Show All products

        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(imageView1);
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url2()).into(imageView2);
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url3()).into(imageView3);

            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(showAllModel.getImg_url2()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });

            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_for_detail);






                    ImageView d_icon, exit;

                    d_icon = dialog.findViewById(R.id.dialog_image);



                    exit = dialog.findViewById(R.id.exit);


                    Glide.with(getApplicationContext()).load(showAllModel.getImg_url3()).into(d_icon);


                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });





                    dialog.show();

                }
            });



        }

// for similar items on detail view
   /*     if (type == null || type.isEmpty()) {

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
*/

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

        if (popularProductModel != null) {
            imgurl = popularProductModel.getImg_url();


        }


        if (showAllModel != null) {
            imgurl = showAllModel.getImg_url();

        }

        if (newProductsModel != null) {
            imgurl = newProductsModel.getImg_url();

        }









        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
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
        cartMap.put("img_url" , imgurl);
        cartMap.put("cartbadge" , cartbadge);
        cartMap.put("onPay" , "Delete");











        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                Toast.makeText(DetailedActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                startActivity(intent);

                finish();

            }


        });


    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }



}













