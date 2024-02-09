package com.application.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.netflix.R;

public class StepOne extends AppCompatActivity {
    TextView signin,step1of3;
    Button seeyourplanbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);
        seeyourplanbtn = findViewById(R.id.seeyourplanbtn);
        step1of3 = findViewById(R.id.step1of3);
        signin = findViewById(R.id.signinstepone);
        signin.setOnClickListener(view -> {
            Intent i = new Intent(StepOne.this,SignIn.class);
            startActivity(i);
        });
        seeyourplanbtn.setOnClickListener(view -> {
            Intent i = new Intent(StepOne.this,ChooseYourPlan.class);
            startActivity(i);
        });
        SpannableString st = new SpannableString("STEP 1 OF 3");
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        step1of3.setText(st);
    }
}