package com.example.ecommerce.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.models.AddressModel;
import com.example.ecommerce.models.DiscountModel;
import com.example.ecommerce.models.MyCartModel;
import com.example.ecommerce.models.ShippingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PaymentActiviy extends AppCompatActivity {
    double amount = 0.00;
    String name = "";
    String img_url = "";
    String id = "";
    String address = "";
    String qty = "";
    List<MyCartModel> myCartModelList;
    private List<AddressModel> addressModelList;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    DiscountModel discountModel;

    double discountbtn = 0.00;
    double discountamount = 0.00;


    Toolbar toolbar;
    TextView subTotal, shipping, total;
    TickerView discount;
    Button paymentBtn;
    Button apply;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activiy);

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
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_244);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final String coupon = "eWealth$";



        img_url = getIntent().getStringExtra("img_url");
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        address = getIntent().getStringExtra("address");
        qty = getIntent().getStringExtra("qty");

        editText = findViewById(R.id.discountcodefill);
        apply = findViewById(R.id.pay_btndiscount);



        amount = getIntent().getDoubleExtra("amount", 0.00);

        myCartModelList = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("cartModelList");
        addressModelList = (ArrayList<AddressModel>) getIntent().getSerializableExtra("addressModelList");

        if (myCartModelList != null && myCartModelList.size() > 0) {
            amount = 0.00;
            for (MyCartModel myCartModel : myCartModelList) {
                amount += myCartModel.getTotalPrice();
                id += myCartModel.getDocumentId();
                name += myCartModel.getProductName();
                img_url += myCartModel.getImg_url();
                qty += myCartModel.getTotalQuantity();


            }

        }
        if (addressModelList != null && addressModelList.size() > 0) {

            for (AddressModel addressModel : addressModelList) {
                address += addressModel.getUserAddress();

            }

        }
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);


        subTotal.setText("$" + amount + 0);
        total.setText("$" + amount + 0);

        discountbtn = amount - amount / 100 * 10;
        discountamount = amount / 100 * 10;


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (editText.getText().toString().equals(coupon)) {
                    final TickerView tickerView = findViewById(R.id.textView17);
                    tickerView.setCharacterLists(TickerUtils.provideNumberList());
                    tickerView.setAnimationDuration(2000);
                    tickerView.setAnimationInterpolator(new OvershootInterpolator());


                    tickerView.setText(("$" + amount / 1000 + 0 * 10));
                    total.setText("$" + discountbtn + 0);


                } else {
                    Toast.makeText(getApplicationContext(), "Discount code is incorrect", Toast.LENGTH_SHORT).show();

                }
            }

        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals(coupon)) {
                    Intent intent = new Intent(PaymentActiviy.this, CheckoutActivity.class);
                    intent.putExtra("amount", amount - amount / 100 * 10);
                    intent.putExtra("name", name);
                    intent.putExtra("img_url", img_url);
                    intent.putExtra("id", id);
                    intent.putExtra("address", address);
                    intent.putExtra("qty", qty);
                    startActivity(intent);


                } else {
                    Intent intent = new Intent(PaymentActiviy.this, CheckoutActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("discount", discountbtn);
                    intent.putExtra("discountamount", discountamount);
                    intent.putExtra("name", name);
                    intent.putExtra("img_url", img_url);
                    intent.putExtra("id", id);
                    intent.putExtra("address", address);
                    intent.putExtra("qty", qty);

                    startActivity(intent);
                }
            }
        });




        final Activity activity = PaymentActiviy.this;

        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "eMart");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://firebasestorage.googleapis.com/v0/b/ecommerce-project-emart.appspot.com/o/emartfinal.jpg?alt=media&token=76416cb9-8a58-456e-96ea-890c83de684b");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "usd");
            //double total = Double.parseDouble(mAmountText.getText().toString());
            //multiply with 100 to get exact amount in rupee
            amount = amount * 100;
            //amount
            options.put("amount", amount);
            options.put("discount", discountbtn);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "emart@oldrichllc.com");
            //contact
            preFill.put("contact", "2142540698");

            options.put("prefill", preFill);


        } catch (Exception e) {
            Log.e("TAG", "", e);
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    }








