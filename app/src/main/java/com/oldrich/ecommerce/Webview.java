package com.oldrich.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oldrich.ecommerce.actvities.MainActivity;

public class Webview extends AppCompatActivity {
Toolbar toolbar;

  private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://direct.lc.chat/13290273/");
        WebSettings webSettings = webview.getSettings();
       webSettings.setJavaScriptEnabled(true);
        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Webview.this, MainActivity.class));
            }
        });



    }
    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();



        } else {

            super.onBackPressed();

        }

    }


}