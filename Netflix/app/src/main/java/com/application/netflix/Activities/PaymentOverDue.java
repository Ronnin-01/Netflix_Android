package com.application.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.application.netflix.Mainscreens.MainScreen;
import com.application.netflix.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentOverDue extends AppCompatActivity {

    TextView signin;
    Button payBtn;
    RadioButton radiobasic,radiostd,radioprem;
    Date today,validate;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    DocumentReference userRef;
    Intent i;
    String TAG = "payment error";
    String planname,plancost,planformatofcost,Intfirstname,Intlastname,IntUid,Intemail,Intcontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_over_due);
        i = getIntent();
        Intfirstname = i.getStringExtra("firstname");
        Intlastname=i.getStringExtra("lastname");
        IntUid=i.getStringExtra("Uid");
        Intemail=i.getStringExtra("email");
        Intcontact=i.getStringExtra("contact");
        signin = findViewById(R.id.signinstepone);
        payBtn= findViewById(R.id.payoverdue);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar c =Calendar.getInstance();
        today=c.getTime();
        c.add(Calendar.MONTH,1);
        validate=c.getTime();

        radiobasic=findViewById(R.id.radiobasicbtn);
        radiobasic.setOnCheckedChangeListener(new PaymentOverDue.Radio_check());

        radiostd=findViewById(R.id.radiostdbtn);
        radioprem=findViewById(R.id.radioprembtn);
        radioprem.setOnCheckedChangeListener(new PaymentOverDue.Radio_check());
        radiostd.setOnCheckedChangeListener(new PaymentOverDue.Radio_check());
        radioprem.setChecked(true);

        payBtn.setOnClickListener(view -> {
            startPayment();
        });

        signin.setOnClickListener(view -> {
            Intent i = new Intent(PaymentOverDue.this,SignIn.class);
            startActivity(i);
        });
    }
    class Radio_check implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked){
                if (compoundButton.getId()==R.id.radiobasicbtn){
                    planname="Basic";
                    plancost="349";
                    planformatofcost="₹349/month";
                    radiostd.setChecked(false);
                    radioprem.setChecked(false);
                }
                if (compoundButton.getId()==R.id.radiostdbtn){
                    planname="Standard";
                    plancost="649";
                    planformatofcost="₹649/month";
                    radiobasic.setChecked(false);
                    radioprem.setChecked(false);
                }
                if (compoundButton.getId()==R.id.radioprembtn){
                    planname="Premium";
                    plancost="799";
                    planformatofcost="₹799/month";
                    radiostd.setChecked(false);
                    radiobasic.setChecked(false);
                }
            }
        }
    }
    public  void startPayment(){
        progressDialog=new ProgressDialog(PaymentOverDue.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Checkout checkout = new Checkout();
        final Activity activity = this;
        checkout.setKeyID("rzp_test_0kWIM2FX8rtcgf");
        String name = Intfirstname+Intlastname;


        try {
            JSONObject options = new JSONObject();
            options.put("name",name);
            options.put("description","APP PAYMENT");
            options.put("currency","INR");
            String payment = plancost;
            double total = Double.parseDouble(payment);
            total=total*100;
            options.put("amount",total);
            options.put("prefill.email",Intemail);
            options.put("prefill.contact",Intcontact);
            checkout.open(activity,options);

        }catch (Exception e){
            Log.e(TAG,"error occurred",e);
        }
    }
    public void onPaymentSuccess(String s) {
                    DocumentReference documentReference= firebaseFirestore.collection("Users").document(IntUid);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Email",Intemail);
                    user.put("First Name",Intfirstname);
                    user.put("Last Name",Intlastname);
                    user.put("Plan Cost",plancost);
                    user.put("Contact Number",Intcontact);
                    user.put("Valid Date",validate);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i = new Intent(PaymentOverDue.this, MainScreen.class);
                            startActivity(i);
                            progressDialog.cancel();
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
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        progressDialog.cancel();

    }
}