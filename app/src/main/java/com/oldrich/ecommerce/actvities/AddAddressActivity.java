package com.oldrich.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oldrich.ecommerce.R;
import com.oldrich.ecommerce.models.MyCartModel;
import com.oldrich.ecommerce.models.NewProductsModel;
import com.oldrich.ecommerce.models.PopularProductModel;
import com.oldrich.ecommerce.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    EditText name,address,city,postalCode,phoneNumber;
    Toolbar toolbar;
    Button addAddressBtn;
    double amount=0.00;
    String name2="";
    String img_url="";
    String id="";
    String qty="";
    List<MyCartModel> myCartModelList;


    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

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

        toolbar=findViewById(R.id.add_address_toolbar);
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
        name2=getIntent().getStringExtra("name");
        id=getIntent().getStringExtra("id");
        qty=getIntent().getStringExtra("qty");




        amount = getIntent().getDoubleExtra("amount", 0.00);
        Object obj = getIntent().getSerializableExtra("push");

        if (myCartModelList != null && myCartModelList.size()>0){
            amount = 0.00;
            for (MyCartModel myCartModel: myCartModelList){
                amount+=myCartModel.getTotalPrice();
                id+=myCartModel.getDocumentId();
                name2+=myCartModel.getProductName();
                img_url+=myCartModel.getImg_url();
                qty+=myCartModel.getTotalQuantity();


            }

        }


        myCartModelList = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("cartModelList");

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        phoneNumber = findViewById(R.id.ad_phone);
        postalCode = findViewById(R.id.ad_code);
        addAddressBtn = findViewById(R.id.ad_add_address);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();



                String final_address = "";

                if (!userName.isEmpty()){

                    final_address+=userName+", ";
                }

                if (!userCity.isEmpty()){

                    final_address+=userCity+", ";
                }

                if (!userAddress.isEmpty()){

                    final_address+=userAddress+", ";
                }

                if (!userCode.isEmpty()){

                    final_address+=userCode+", ";
                }

                if (!userNumber.isEmpty()){

                    final_address+=userNumber+", ";
                }

                if (! userName.isEmpty() && ! userCity.isEmpty() && ! userAddress.isEmpty() && ! userCode.isEmpty() && ! userNumber.isEmpty()){
                    Map<String,String> map = new HashMap<>();
                    map.put("userAddress", final_address);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){


                                Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                                double amount = 0.00;
                                String img_url = "";
                                String name2 = "";
                                String id = "";


                                if (obj instanceof MyCartModel) {
                                    MyCartModel i = (MyCartModel) obj;
                                    amount = i.getTotalPrice();
                                    name2 = i.getProductName();
                                    img_url = i.getImg_url();
                                    id = i.getDocumentId();
                                    qty=i.getTotalQuantity();


                                }
                                if (obj instanceof NewProductsModel) {
                                    NewProductsModel newProductsModel = (NewProductsModel) obj;
                                    amount = newProductsModel.getPrice();
                                    img_url = newProductsModel.getImg_url();
                                    name2 = newProductsModel.getName();
                                }
                                if (obj instanceof PopularProductModel) {
                                    PopularProductModel popularProductModel = (PopularProductModel) obj;
                                    amount = popularProductModel.getPrice();
                                    img_url = popularProductModel.getImg_url();
                                    name2 = popularProductModel.getName();

                                }
                                if (obj instanceof ShowAllModel) {
                                    ShowAllModel showAllModel = (ShowAllModel) obj;
                                    amount = showAllModel.getPrice();
                                    img_url = showAllModel.getImg_url();
                                    name2 = showAllModel.getName();
                                }

                                if (myCartModelList != null && myCartModelList.size() > 0) {
                                    Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                                    intent.putExtra("cartModelList", (Serializable) myCartModelList);

                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                                    intent.putExtra("amount", amount);
                                    intent.putExtra("img_url", img_url);
                                    intent.putExtra("name", name2);
                                    intent.putExtra("id", id);
                                    intent.putExtra("qty", qty);


                                    startActivity(intent);
                                }

                                }
                        }
                    });
                }else{
                    Toast.makeText(AddAddressActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
}