package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.AddressAdapter;
import com.example.ecommerce.models.AddressModel;
import com.example.ecommerce.models.MyCartModel;
import com.example.ecommerce.models.NewProductsModel;
import com.example.ecommerce.models.PopularProductModel;
import com.example.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    Button addAddress;
    RecyclerView recyclerView;
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button paymentBtn;
    Toolbar toolbar;
    String mAddress = "";

    double amount=0.0;
    String name="";
    String img_url="";
    String id="";
    String qty="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
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

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_244);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get data from detaliled activity and cart

        img_url=getIntent().getStringExtra("img_url");
        name=getIntent().getStringExtra("name");
        id=getIntent().getStringExtra("id");
        qty=getIntent().getStringExtra("qty");
        amount = getIntent().getDoubleExtra("amount",0.0);


        Object obj = getIntent().getSerializableExtra("item");
        List<MyCartModel> cartModelList = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("cartModelList");


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        addAddress = findViewById(R.id.add_address_btn);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getApplicationContext(), addressModelList, this);
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                        String documentId = doc.getId();

                        AddressModel addressModel = doc.toObject(AddressModel.class);
                        addressModel.setDocumentId(documentId);
                        addressModelList.add(addressModel);
                        addressAdapter.notifyDataSetChanged();
                    }

                }
            }
        });


        paymentBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {





                                              if (obj instanceof MyCartModel) {
                                                  MyCartModel i = (MyCartModel) obj;
                                                  amount = i.getTotalPrice();
                                                  name = i.getProductName();
                                                  img_url = i.getImg_url();
                                                  id = i.getDocumentId();
                                                  qty = i.getTotalQuantity();


                                              }
                                              if (obj instanceof NewProductsModel) {
                                                  NewProductsModel newProductsModel = (NewProductsModel) obj;
                                                  amount = newProductsModel.getPrice();
                                                  img_url = newProductsModel.getImg_url();
                                                  name = newProductsModel.getName();

                                              }
                                              if (obj instanceof PopularProductModel) {
                                                  PopularProductModel popularProductModel = (PopularProductModel) obj;
                                                  amount = popularProductModel.getPrice();
                                                  img_url = popularProductModel.getImg_url();
                                                  name = popularProductModel.getName();

                                              }
                                              if (obj instanceof ShowAllModel) {
                                                  ShowAllModel showAllModel = (ShowAllModel) obj;
                                                  amount = showAllModel.getPrice();
                                                  img_url = showAllModel.getImg_url();
                                                  name = showAllModel.getName();
                                              }


                                              if (cartModelList != null && cartModelList.size() > 0) {
                                                  Intent intent = new Intent(AddressActivity.this, PaymentActiviy.class);
                                                  intent.putExtra("cartModelList", (Serializable) cartModelList);
                                                  intent.putExtra("address", mAddress);
                                                  intent.putExtra("addressModelList", (Serializable) addressModelList);
                                                  startActivity(intent);


                                              } else {
                                                  Intent intent = new Intent(AddressActivity.this, PaymentActiviy.class);
                                                  intent.putExtra("amount", amount);
                                                  intent.putExtra("img_url", img_url);
                                                  intent.putExtra("name", name);
                                                  intent.putExtra("id", id);
                                                  intent.putExtra("address", mAddress);
                                                  intent.putExtra("qty", qty);
                                                  intent.putExtra("addressModelList", (Serializable) addressModelList);
                                                  startActivity(intent);

                                              }
                                          }
                                      });



        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cartModelList != null && cartModelList.size() > 0) {
                    Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                    intent.putExtra("cartModelList", (Serializable) cartModelList);
                    intent.putExtra("address", mAddress);
                    intent.putExtra("addressModelList", (Serializable) addressModelList);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);

                    if (obj instanceof MyCartModel) {
                        MyCartModel i = (MyCartModel) obj;
                        amount = i.getTotalPrice();
                        name = i.getProductName();
                        img_url = i.getImg_url();
                        id = i.getDocumentId();
                        intent.putExtra("qty", qty);
                        intent.putExtra("push", i);


                    }
                    if (obj instanceof NewProductsModel) {
                        NewProductsModel newProductsModel = (NewProductsModel) obj;
                        amount = newProductsModel.getPrice();
                        img_url = newProductsModel.getImg_url();
                        name = newProductsModel.getName();
                        intent.putExtra("push", newProductsModel);
                    }
                    if (obj instanceof PopularProductModel) {
                        PopularProductModel popularProductModel = (PopularProductModel) obj;
                        amount = popularProductModel.getPrice();
                        img_url = popularProductModel.getImg_url();
                        name = popularProductModel.getName();
                        intent.putExtra("push", popularProductModel);

                    }
                    if (obj instanceof ShowAllModel) {
                        ShowAllModel showAllModel = (ShowAllModel) obj;
                        amount = showAllModel.getPrice();
                        img_url = showAllModel.getImg_url();
                        name = showAllModel.getName();
                        intent.putExtra("push", showAllModel);
                    }

                    startActivity(intent);
                }

            }










        });

    }



    @Override
    public void setAddress(String address) {

        mAddress = address;

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
}