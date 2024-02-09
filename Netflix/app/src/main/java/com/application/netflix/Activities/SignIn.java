package com.application.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.application.netflix.Mainscreens.MainScreen;
import com.application.netflix.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SignIn extends AppCompatActivity {
    ProgressBar progressBar;
    EditText email,password;
    String authEmail,resetEmail,authPassword,UserID,FireContact,FireFirstname,FireLastname;
    FirebaseAuth firebaseAuth;
    TextView signuptv,forgetpsswrdtv;
    Button signinbtn;
    FirebaseFirestore firebaseFirestore;
    Date validate,today;
    DocumentReference userRef;
    static int counter = 0;
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        progressBar = findViewById(R.id.signinpgbar);
        signuptv = findViewById(R.id.signuptextv);
        forgetpsswrdtv = findViewById(R.id.forgotpsswrdtxtv);
        signinbtn = findViewById(R.id.btnsignin);
        email=findViewById(R.id.emailedittext);
        password=findViewById(R.id.passwordedittext);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar c =Calendar.getInstance();
        today=c.getTime();
        c.add(Calendar.MONTH,1);
        validate=c.getTime();

        progressBar.setVisibility(View.GONE);

        signinbtn.setOnClickListener(view -> {
            authEmail = email.getText().toString();
            authPassword = password.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (email.getText().toString().length() > 8 && email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
                    && password.getText().toString().length() > 7)
            {

            firebaseAuth.signInWithEmailAndPassword(authEmail, authPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserID=firebaseAuth.getCurrentUser().getUid();
                    userRef=firebaseFirestore.collection("Users").document(UserID);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            validate= documentSnapshot.getDate("Valid Date");
                            FireFirstname=documentSnapshot.getString("First Name");
                            FireLastname=documentSnapshot.getString("Last Name");
                            FireContact=documentSnapshot.getString("Contact Number");
                            if (validate.compareTo(today)>=0){
                                Intent i = new Intent(SignIn.this, MainScreen.class);
                                startActivity(i);
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                            else {
                                Intent i = new Intent(SignIn.this,PaymentOverDue.class);
                                i.putExtra("firstname",FireFirstname);
                                i.putExtra("lastname",FireLastname);
                                i.putExtra("contact",FireContact);
                                i.putExtra("email",authEmail);
                                i.putExtra("Uid",UserID);
                                startActivity(i);
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(getApplicationContext(), "Plan Expired", Toast.LENGTH_SHORT).show();
                                }
                            }
                    });

                } else {
                    if (task.getException() instanceof FirebaseNetworkException)
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    if (task.getException() instanceof FirebaseAuthEmailException)
                        email.setError("User does not exist");
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        Toast.makeText(getApplicationContext(), "Invalid email and Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        }
            else{
                if(email.getText().toString().length()<=7 ||!email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") )
                {email.setError("Enter a valid email id");
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                if(password.getText().toString().length()<=7)
                {password.setError("Wrong password");
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                if(email.getText().toString().length()==0)
                {email.setError("Field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                if(password.getText().toString().length()==0)
                {password.setError("Field cannot be empty");
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                else
                {
                    password.setError("Wrong password");
                }
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        });
        signuptv.setOnClickListener(view -> {
            Intent i = new Intent(SignIn.this, SwipeScreen.class);
            startActivity(i);
        });
        forgetpsswrdtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length()>8 && email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                {
                    AlertDialog.Builder passwordreset= new AlertDialog.Builder(view.getContext());
                    passwordreset.setTitle("Reset Password?");
                    passwordreset.setMessage("Press YES to receive the reset link");
                    passwordreset.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetEmail=email.getText().toString();
                            firebaseAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Email reset link sent",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if(e instanceof FirebaseNetworkException)
                                    { Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();}
                                    Toast.makeText(getApplicationContext(),"Email reset link not sent as no user exist by this email",Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });
                    passwordreset.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    passwordreset.create().show();

                }
                else {
                    email.setError("Enter a valid email");
                }
            }
        });

    }
    public void progress(){
        final Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                counter ++;
                progressBar.setProgress(counter);
                if (counter == 500){
                    timer.cancel();
                }

            }
        };
        timer.schedule(timertask,0,100);
    }
}