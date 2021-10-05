package com.example.ecommerce.actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.ecommerce.R;
import com.example.ecommerce.models.AddressModel;
import com.example.ecommerce.models.MyCartModel;

import java.util.ArrayList;
import java.util.List;

public class ShippingActivity extends AppCompatActivity {

    Toolbar toolbar;
    double amount=0.0;
    String name="";
    String img_url="";
    String id="";
    String address="";
    List<MyCartModel> myCartModelList;
    private List<AddressModel> addressModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);


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


        img_url=getIntent().getStringExtra("img_url");
        name=getIntent().getStringExtra("name");
        id=getIntent().getStringExtra("id");
        address = getIntent().getStringExtra("address");

        amount = getIntent().getDoubleExtra("amount",0.0);
        myCartModelList = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("cartModelList");
        addressModelList =(ArrayList<AddressModel>) getIntent().getSerializableExtra("addressModelList");
        if (myCartModelList != null && myCartModelList.size()>0){
            amount = 0.0;
            for (MyCartModel myCartModel: myCartModelList){
                amount+=myCartModel.getTotalPrice();
                id+=myCartModel.getDocumentId();
                name+=myCartModel.getProductName();
                img_url+=myCartModel.getImg_url();


            }

        }
        if (addressModelList != null && addressModelList.size()>0){

            for (AddressModel addressModel: addressModelList){
                address+=addressModel.getUserAddress();

            }

        }

    }
}