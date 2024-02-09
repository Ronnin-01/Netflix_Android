package com.application.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;
import com.application.netflix.R;

public class FinishUpAccount extends AppCompatActivity {
    String PlanName,PlanCost,PlanCostFormat;
    TextView step1of3,signin;
    Button continuebtn;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up_account);

        i = getIntent();
        PlanName = i.getStringExtra("PlanName");
        PlanCost = i.getStringExtra("PlanCost");
        PlanCostFormat = i.getStringExtra("PlanCostFormat");
        step1of3 = findViewById(R.id.step1of3finish);
        continuebtn = findViewById(R.id.continuebtn);

        signin = findViewById(R.id.signinstepone);
        signin.setOnClickListener(view -> {
            Intent n = new Intent(FinishUpAccount.this,SignIn.class);
            startActivity(n);
        });
        continuebtn.setOnClickListener(view -> {
            Intent i = new Intent(FinishUpAccount.this,StepTwo.class);
            i.putExtra("PlanName",PlanName);
            i.putExtra("PlanCost",PlanCost);
            i.putExtra("PlanCostFormat",PlanCostFormat);
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