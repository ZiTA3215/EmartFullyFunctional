package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get data from detaliled activity and cart

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

                double amount = 0.0;
                String url="";
                String name="";
                String id = "";


                if (obj instanceof MyCartModel) {
                    MyCartModel i = (MyCartModel) obj;
                    amount = i.getTotalPrice();
                    name = i.getProductName();
                    url = i.getImg_url();
                    id = i.getDocumentId();


                }
                    if (obj instanceof NewProductsModel) {
                        NewProductsModel newProductsModel = (NewProductsModel) obj;
                        amount = newProductsModel.getPrice();
                        url=newProductsModel.getImg_url();
                        name=newProductsModel.getName();
                    }
                    if (obj instanceof PopularProductModel) {
                        PopularProductModel popularProductModel = (PopularProductModel) obj;
                        amount = popularProductModel.getPrice();
                        url = popularProductModel.getImg_url();
                        name=popularProductModel.getName();

                    }
                    if (obj instanceof ShowAllModel) {
                        ShowAllModel showAllModel = (ShowAllModel) obj;
                        amount = showAllModel.getPrice();
                        url=showAllModel.getImg_url();
                        name=showAllModel.getName();
                    }



                if (cartModelList != null && cartModelList.size() > 0) {
                    Intent intent = new Intent(AddressActivity.this, PaymentActiviy.class);
                    intent.putExtra("cartModelList", (Serializable) cartModelList);
                    startActivity(intent);


                } else {
                    Intent intent = new Intent(AddressActivity.this, PaymentActiviy.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("img_url",url);
                    intent.putExtra("name",name);
                    intent.putExtra("id",id);
                    intent.putExtra("address",mAddress);
                    startActivity(intent);
                }
            }


        });


        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });
    }

    @Override
    public void setAddress(String address) {

        mAddress = address;

    }
}