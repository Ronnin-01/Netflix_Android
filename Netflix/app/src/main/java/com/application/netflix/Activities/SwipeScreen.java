package com.application.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.netflix.Adapters.ViewPagerAdapter;
import com.application.netflix.R;

public class SwipeScreen extends AppCompatActivity {
    TextView privacy, help, signin;
    LinearLayout sliderDots;
    Button getstarted;
    ViewPager viewPagerSwipe;
    private int dotscount;
    private ImageView[] dots;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        help = findViewById(R.id.helptv);
        privacy = findViewById(R.id.privacytv);
        signin = findViewById(R.id.signintvss);
        getstarted = findViewById(R.id.getstarted);
        viewPagerSwipe = findViewById(R.id.viewpagerswipescrn);
        sliderDots = findViewById(R.id.sliderdots);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerSwipe.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactivedots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedots));


        viewPagerSwipe.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactivedots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SwipeScreen.this, SignIn.class);
                startActivity(i);

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy")));

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en")));

            }
        });
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SwipeScreen.this, StepOne.class);
                startActivity(i);

            }
        });

    }
}