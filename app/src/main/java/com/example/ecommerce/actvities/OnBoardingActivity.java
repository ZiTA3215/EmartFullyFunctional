package com.example.ecommerce.actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.SlideAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager viewpager;
    LinearLayout dotsLayout;

    Button btn;

    SlideAdapter slideadpater;

    TextView[] dots;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        //getSupportActionBar().hide();

        viewpager=findViewById(R.id.slider);
        btn = findViewById(R.id.get_started_btn);
        dotsLayout=findViewById(R.id.dots);

        addDots(0);

        viewpager.addOnPageChangeListener(changeListener);

        //call adapter

        slideadpater = new SlideAdapter(this);
        viewpager.setAdapter(slideadpater);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoardingActivity.this,MainActivity.class));

                finish();
            }
        });




    }

    private void addDots (int position){
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for (int i = 0; i< dots.length; i++){

            dots[i]= new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length >0){
            dots[position].setTextColor(getResources().getColor(R.color.purple_200));
        }
    }

ViewPager.OnPageChangeListener changeListener =new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        addDots(position);

        if (position == 0) {

            btn.setVisibility(View.INVISIBLE);
        }else if (position == 1){
            btn.setVisibility(View.INVISIBLE);


        }else{

            animation = AnimationUtils.loadAnimation(OnBoardingActivity.this,R.anim.slide_animation);
            btn.setAnimation(animation);
            btn.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
};

}