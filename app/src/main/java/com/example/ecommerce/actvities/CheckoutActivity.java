package com.example.ecommerce.actvities;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.MyCartAdapter;
import com.example.ecommerce.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CheckoutActivity extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
    //private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    private static final String BACKEND_URL = "https://emart-backend-oldrich.herokuapp.com/";

     OkHttpClient httpClient = new OkHttpClient();
     String paymentIntentClientSecret;
     Stripe stripe;
    Double amountDobule= null;
    Double discount= null;
    Double discountamount= null;
    private FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    static String name="";
    static String id="";
    static String img_url = "";
    static String address = "";
    static String qty = "";
    Toolbar toolbar;

    MyCartModel cartModel;
    List<MyCartModel> list;
    MyCartAdapter cartAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //Toolbar
        toolbar = findViewById(R.id.checkou_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_244);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
         amountDobule = getIntent().getDoubleExtra("amount", 0.0);
        discount = getIntent().getDoubleExtra("discount", 0.0);
        discountamount = getIntent().getDoubleExtra("discountamount", 0.0);
        img_url = getIntent().getStringExtra("img_url");
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        address= getIntent().getStringExtra("address");
       qty= getIntent().getStringExtra("qty");



        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51J53C8AarcQmncWNN0aLaP1pnLs1DwkZeqbyy36URxCVddKjia32v7ZGlpVnvFPOUdWzHMWgTUI1CqvjdwFiw93M0064J9WiuB")
        );
        startCheckout();
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
      //  String json = "{"
               // + "\"currency\":\"usd\","
              //  + "\"items\":["
              //  + "{\"id\":\"photo_subscription\"}"
              //  + "]"
             //   + "}";

        double discount2=discount;
        double amount=amountDobule;


        Map<String,Object> payMap=new HashMap<>();
        Map<String,Object> itemMap=new HashMap<>();
        List<Map<String,Object>> itemList =new ArrayList<>();
        payMap.put("currency","usd");
        itemMap.put("id","photo_subscription");
        itemMap.put("amount",amount);
        itemMap.put("discount2",discount2);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));





        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckoutActivity> activityRef;
        PayCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }
    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivity> activityRef;
        PaymentResultCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast.makeText(activity, "Ordered Successfully", Toast.LENGTH_SHORT).show();

                String saveCurrentTime, saveCurrentDate;
                Calendar callForDate = Calendar.getInstance();


                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
                saveCurrentDate = currentDate.format(callForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(callForDate.getTime());



                Map<String,Object> mMap = new HashMap<>();
                mMap.put("name",name);
                mMap.put("qty",qty);
                mMap.put("img_url",img_url);
                mMap.put("price",amountDobule);
                mMap.put("pricediscount",discount);
                mMap.put("discountamount",discountamount);
                mMap.put("id",id);
                mMap.put("address",address);
                mMap.put("payment_id",paymentIntent.getPaymentMethodId());
                mMap.put("currentTime", saveCurrentTime);
                mMap.put("currentDate", saveCurrentDate);
                mMap.put("shippingname","");
                mMap.put("Tracking","");
                mMap.put("shippinglogourl","");
                mMap.put("shippingurl","");


                mStore.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                        .collection("User").document().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"onSuccess:");

                    }
                });


                mStore.collection("AddToCart").document(mAuth.getCurrentUser().getUid())
                        .collection("User").whereEqualTo("onPay", "Delete").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch b =FirebaseFirestore.getInstance().batch();
                        List<DocumentSnapshot> s = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: s)
                        {
                            b.delete(snapshot.getReference());
                        }
                        b.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });

                mStore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid())
                        .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {


                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {


                        




                        Intent intent=new Intent(CheckoutActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });


               /* activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)
                );*/
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }


}

