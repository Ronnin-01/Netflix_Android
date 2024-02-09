package com.application.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.application.netflix.Mainscreens.MainScreen;
import com.application.netflix.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DocumentReference reference;
    Date today,validate;
    FirebaseFirestore firebaseFirestore;
    static int counter = 0;
    String Uid,FireFirstname,FireLastname,FireContact,Fireemail;
    static int duration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressbar);
        Calendar c = Calendar.getInstance();
        today=c.getTime();

        progress();
        start();
    }
    public void progress(){
        final Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                counter ++;
                progressBar.setProgress(counter);
                if (counter == 3000){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timertask,0,100);
    }
    public void start(){
        new Handler().postDelayed(() -> {
            if (firebaseAuth.getCurrentUser() != null) {
                Uid = firebaseAuth.getCurrentUser().getUid();
                reference = firebaseFirestore.collection("Users").document(Uid);
                reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        validate = documentSnapshot.getDate("Valid Date");
                        FireFirstname = documentSnapshot.getString("First Name");
                        FireLastname = documentSnapshot.getString("Last Name");
                        FireContact = documentSnapshot.getString("Contact Number");
                        Fireemail = documentSnapshot.getString("Email");
                        if (validate.compareTo(today) >= 0) {
                            Intent i = new Intent(SplashScreen.this, MainScreen.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashScreen.this, PaymentOverDue.class);
                            i.putExtra("firstname", FireFirstname);
                            i.putExtra("lastname", FireLastname);
                            i.putExtra("contact", FireContact);
                            i.putExtra("email", Fireemail);
                            i.putExtra("Uid", Uid);
                            startActivity(i);
                            finish();
                            Toast.makeText(getApplicationContext(), "Plan Expired", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseNetworkException) {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Error Data Not Fetched", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
            startActivity(new Intent(SplashScreen.this, SignIn.class));
            finish();
            }
        },duration);

    }
}