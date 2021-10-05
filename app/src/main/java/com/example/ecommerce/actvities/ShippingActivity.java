package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.MyCartAdapter;
import com.example.ecommerce.adapters.ShippingAdapter;
import com.example.ecommerce.models.AddressModel;
import com.example.ecommerce.models.MyCartModel;
import com.example.ecommerce.models.ShippingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShippingActivity extends AppCompatActivity {

    Toolbar toolbar;
    List<ShippingModel> shippingModelList;
    ShippingAdapter shippingAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        shippingModelList = new ArrayList<>();
        toolbar = findViewById(R.id.my_shipping_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_244);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.shipping_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shippingModelList = new ArrayList<>();
        shippingAdapter = new ShippingAdapter(this, shippingModelList);
        recyclerView.setAdapter(shippingAdapter);
        shippingAdapter.notifyDataSetChanged();

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {

                        String documentId = doc.getId();


                        ShippingModel shippingModel = doc.toObject(ShippingModel.class);
                        shippingModel.setDocumentId(documentId);
                        shippingModelList.add(shippingModel);
                        shippingAdapter.notifyDataSetChanged();
                    }
                }

            }

        });


    }}




