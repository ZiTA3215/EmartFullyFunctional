package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.AddressAdapter;
import com.example.ecommerce.adapters.MyCartAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {


    TextView overAllAmount;

    Toolbar toolbar;
    RecyclerView recyclerView;
    Button paymentBtn;
    private List<AddressModel> addressModelList;


    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        overAllAmount = findViewById(R.id.textView3);

        recyclerView = findViewById(R.id.cart_rec);
        paymentBtn = findViewById(R.id.buy_now);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);

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
                //get data from detaliled activity

                Object obj= getIntent().getSerializableExtra("item");

                firestore = FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();


                addressModelList = new ArrayList<>();

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

                            }

                        }
                    }
                });
                paymentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CartActivity.this,AddressActivity.class));

                        double amount = 0.0;

                        if(obj instanceof NewProductsModel){
                            NewProductsModel newProductsModel = (NewProductsModel) obj;
                            amount = newProductsModel.getPrice();
                        }if(obj instanceof PopularProductModel){
                            PopularProductModel popularProductModel = (PopularProductModel) obj;
                            amount = popularProductModel.getPrice();

                        }if(obj instanceof ShowAllModel) {
                            ShowAllModel showAllModel = (ShowAllModel) obj;
                            amount = showAllModel.getPrice();
                        }

                        Intent intent = new Intent(CartActivity.this, PaymentActiviy.class);
                        intent.putExtra("amount", amount);
                        startActivity(intent);
                    }
                });



            }
        });
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {

        double totalAmount = 0.0;
        for (MyCartModel myCartModel : cartModelList) {

            totalAmount += myCartModel.getTotalPrice();
        }

        overAllAmount.setText("Total Amount :" + totalAmount);
    }

}







