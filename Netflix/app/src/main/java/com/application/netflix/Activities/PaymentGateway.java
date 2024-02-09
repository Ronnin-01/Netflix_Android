package com.application.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.application.netflix.Mainscreens.MainScreen;
import com.application.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {
    String PlanName,PlanCost,PlanCostFormat,UserEmailId,UserPassword,UserID;
    String firstName,lastNAme,contactNum;
    EditText firstNameET,lastNameET,contactET;
    Button startMembership;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date today,validate;
    CheckBox iAgree;
    String TAG= "payment error";
    TextView textUrl,step3of3,change,costToSet,planNameToSet;
    Intent i;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        i = getIntent();
        PlanName = i.getStringExtra("PlanName");
        PlanCost = i.getStringExtra("PlanCost");
        PlanCostFormat = i.getStringExtra("PlanCostFormat");
        UserEmailId =i.getStringExtra("EmailId");
        UserPassword=i.getStringExtra("Password");
        Checkout.preload(getApplicationContext());

        firstNameET=findViewById(R.id.firstNameEdT);
        lastNameET =findViewById(R.id.lastNameEdT);
        contactET =findViewById(R.id.contactNumEdT);
        startMembership=findViewById(R.id.startMembershipBtn);
        iAgree =findViewById(R.id.iAgreeCheck);
        textUrl  =findViewById(R.id.toPutUrlTxt);
        step3of3=findViewById(R.id.step3of3);
        change =findViewById(R.id.change);
        costToSet=findViewById(R.id.costToSet);
        costToSet.setText(PlanCostFormat);
        planNameToSet=findViewById(R.id.planNameToSet);
        planNameToSet.setText(PlanName);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar c =Calendar.getInstance();
        today=c.getTime();
        c.add(Calendar.MONTH,1);
        validate=c.getTime();


        SpannableString st = new SpannableString("STEP 3 OF 3");
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        step3of3.setText(st);

        SpannableString ss = new SpannableString("By checking the checkbox below, \nyou agree to our Terms of Use, Privacy Statement, and that you are over 18." +
                "\nNetflix will automatically continue your membership and charge the monthly membership fee to your payment method until you cancel. " +
                "\nYou may cancel at any time to avoid future charges.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en/legal/termofuse")));
            }
            public void updateDrawState(@NonNull TextPaint ds){
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        ClickableSpan clickableSpan1 =new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en/legal/privacy")));
            }
            public void updateDrawState(@NonNull TextPaint ds){
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };

        ss.setSpan(clickableSpan,49,62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan1,63,81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textUrl.setText(ss);
        textUrl.setMovementMethod(LinkMovementMethod.getInstance());

        change.setOnClickListener(view -> {
            Intent i = new Intent(PaymentGateway.this,ChooseYourPlan.class);
            startActivity(i);
        });
        startMembership.setOnClickListener(view -> {
            if (firstNameET.getText().toString().length()>3 && lastNameET.getText().toString().length()>3 &&
            firstNameET.getText().toString().matches("[a-z A-Z]+")&& lastNameET.getText().toString().matches("[a-z A-Z]+")&&
                    contactET.getText().toString().length()==10 && iAgree.isChecked()) {
                progressDialog=new ProgressDialog(PaymentGateway.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startPayment();
            } else {
                if (firstNameET.getText().toString().length()<=3 || !firstNameET.getText().toString().matches("[a-z A-Z]+")){
                    firstNameET.setError("Enter a valid first name");
                }
                if (lastNameET.getText().toString().length()<=3 || !lastNameET.getText().toString().matches("[a-z A-Z]+")){
                    lastNameET.setError("Enter a valid last name");
                    if (contactET.getText().toString().length()!=10){
                        contactET.setError("Enter a valid 10 digit contact Number");
                    }
                    if (!iAgree.isChecked()){
                        Toast.makeText(getApplicationContext(), "Please agree the policy", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Please fill the correct user information", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.cancel();
                }
            }
        });
    }
    public  void startPayment(){
        Checkout checkout = new Checkout();
        final Activity activity = this;
        checkout.setKeyID("rzp_test_0kWIM2FX8rtcgf");
        firstName =firstNameET.getText().toString();
        lastNAme =lastNameET.getText().toString();
        contactNum =contactET.getText().toString();
        String name = firstName+lastNAme;
        try {
            JSONObject options = new JSONObject();
            options.put("name",name);
            options.put("description","APP PAYMENT");
            options.put("currency","INR");
            String payment = PlanCost;
            double total = Double.parseDouble(payment);
            total=total*100;
            options.put("amount",total);
            options.put("prefill.email",UserEmailId);
            options.put("prefill.contact",contactNum);
            checkout.open(activity,options);

        }catch (Exception e){
            Log.e(TAG,"error occurred",e);
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        firebaseAuth.createUserWithEmailAndPassword(UserEmailId,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference= firebaseFirestore.collection("Users").document(UserID);
                    Map<String, Object>user = new HashMap<>();
                    user.put("Email",UserEmailId);
                    user.put("First Name",firstName);
                    user.put("Last Name",lastNAme);
                    user.put("Plan Cost",PlanCost);
                    user.put("Contact Number",contactNum);
                    user.put("Valid Date",validate);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i = new Intent(PaymentGateway.this, MainScreen.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseNetworkException){
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            }
                            Toast.makeText(getApplicationContext(), "values not stored", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });

                }
            }
        });
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        progressDialog.cancel();

    }
}