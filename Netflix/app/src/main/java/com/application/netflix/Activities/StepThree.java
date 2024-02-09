package com.application.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.application.netflix.R;

public class StepThree extends AppCompatActivity {
    String PlanName,PlanCost,PlanCostFormat,UserEmailId,UserPassword;
    TextView step1of3,signOut;
    //EditText emailiduser,userpassword;
    LinearLayout paymentLinearLayout;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_three);

        signOut=findViewById(R.id.signoutstep3);
        step1of3 = findViewById(R.id.step3);
        paymentLinearLayout=findViewById(R.id.paymentlinearlyt);

        i = getIntent();
        PlanName = i.getStringExtra("PlanName");
        PlanCost = i.getStringExtra("PlanCost");
        PlanCostFormat = i.getStringExtra("PlanCostFormat");
        UserEmailId =i.getStringExtra("EmailId");
        UserPassword=i.getStringExtra("Password");

        signOut.setOnClickListener(view -> {
            Intent n = new Intent(StepThree.this,SignIn.class);
            startActivity(n);
        });
        SpannableString st = new SpannableString("STEP 3 OF 3");
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        step1of3.setText(st);

        paymentLinearLayout.setOnClickListener(view -> {
            i = new Intent(StepThree.this,PaymentGateway.class);
            i.putExtra("PlanName",PlanName);
            i.putExtra("PlanCost",PlanCost);
            i.putExtra("PlanCostFormat",PlanCostFormat);
            i.putExtra("EmailId",UserEmailId);
            i.putExtra("Password",UserPassword);
            startActivity(i);
        });

    }
}