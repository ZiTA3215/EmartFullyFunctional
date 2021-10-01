package com.example.ecommerce.actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;

import org.json.JSONObject;


public class PaymentActiviy extends AppCompatActivity {
    double amount=0.0;
    String name="";
    String img_url="";


    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;
    Button paymentBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activiy);


        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        img_url=getIntent().getStringExtra("img_url");
        name=getIntent().getStringExtra("name");

        amount = getIntent().getDoubleExtra("amount",0.0);


        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);

        subTotal.setText(amount+"$");
        total.setText(amount+"$");

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







    public void payWithStripe(View view) {
        Intent intent = new Intent(PaymentActiviy.this,CheckoutActivity.class);
        intent.putExtra("amount",amount);
        intent.putExtra("name",name);
        intent.putExtra("img_url",img_url);

        startActivity(intent);
    }
}
