package com.application.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.application.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Timer;
import java.util.TimerTask;

public class StepTwo extends AppCompatActivity {
    String PlanName,PlanCost,PlanCostFormat,UserEmailId,UserPassword;
    TextView step1of3,signin;
    Button continuebtn;
    FirebaseAuth firebaseAuth;
    Boolean x;
    EditText emailiduser,userpassword;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    int counter=0;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);
        signin = findViewById(R.id.signinstepone);
        progressBar = findViewById(R.id.progressbarstep2);
        continuebtn = findViewById(R.id.continuestp2);
        step1of3=findViewById(R.id.step2of3);
        progressBar.setVisibility(View.GONE);
        emailiduser = findViewById(R.id.emailedittextstep2);
        userpassword=findViewById(R.id.passwordedittextstep2);
        firebaseAuth=FirebaseAuth.getInstance();


         i = getIntent();
        PlanName = i.getStringExtra("PlanName");
        PlanCost = i.getStringExtra("PlanCost");
        PlanCostFormat = i.getStringExtra("PlanCostFormat");
       // Toast.makeText(this, ""+PlanName+"\n"+PlanCost+"\n"+PlanCostFormat, Toast.LENGTH_SHORT).show();

        signin.setOnClickListener(view -> {
            Intent n = new Intent(StepTwo.this,SignIn.class);
            startActivity(n);
        });

        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserEmailId=emailiduser.getText().toString();
                UserPassword=userpassword.getText().toString();
                x=true;
                if(UserEmailId.length()<10||!UserEmailId.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                {emailiduser.setError("Enter a valid email id");
                    x=false;}
                if(UserPassword.length()<8)
                {userpassword.setError("Password to short");
                    x=false;}
                if(x){
                    progressDialog=new ProgressDialog(StepTwo.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    firebaseAuth.signInWithEmailAndPassword(UserEmailId,UserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            { x=false;

                                Toast.makeText(getApplicationContext(),"Please enter via the main login screen",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(StepTwo.this,SignIn.class);
                                startActivity(i);


                            }else {
                                if(task.getException() instanceof FirebaseNetworkException)
                                {Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
                                    x=false;
                                    progressDialog.cancel();
                                }
                            }
                            if(UserEmailId.length()>9 && UserPassword.length()>7&& UserEmailId.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")&& x){
                                Intent i=new Intent(StepTwo.this,StepThree.class);
                                i.putExtra("PlanName",PlanName);
                                i.putExtra("PlanCost",PlanCost);
                                i.putExtra("PlanCostFormat",PlanCostFormat);
                                i.putExtra("EmailId",UserEmailId);
                                i.putExtra("Password",UserPassword);
                                startActivity(i);
                                progressDialog.cancel();
                            }

                        }
                    });
                }
            }
        });
        SpannableString st = new SpannableString("STEP 2 OF 3");
        StyleSpan boldspan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2 = new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        step1of3.setText(st);
    }
    public void progress(){
        final Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                counter ++;
                progressBar.setProgress(counter);
                if (counter == 1000){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timertask,0,100);

    }
}