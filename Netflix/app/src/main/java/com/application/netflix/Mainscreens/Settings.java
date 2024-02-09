package com.application.netflix.Mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.netflix.Activities.PaymentGateway;
import com.application.netflix.Activities.PaymentOverDue;
import com.application.netflix.Activities.SignIn;
import com.application.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Settings extends AppCompatActivity {
    EditText newpassword;
    TextView email,plan,date;
    FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
    FirebaseUser user;
    DocumentReference reference;
    FirebaseAuth firebaseAuth;
    Button resetpassword,signout;
    String Uid,emailstring,planstring;
    Date validate;
    ProgressDialog progressDialog;
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(Settings.this, MainScreen.class);
        startActivity(i);
    }

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        firebaseAuth= FirebaseAuth.getInstance();
        newpassword=findViewById(R.id.resetpasswordedittext);
        resetpassword=findViewById(R.id.resetpasswordbutton);
        signout=findViewById(R.id.signoutbutton);
        email=findViewById(R.id.emailsettings);
        plan=findViewById(R.id.plansettings);
        date=findViewById(R.id.datesettings);
        user=firebaseAuth.getInstance().getCurrentUser();

        progressDialog=new ProgressDialog(Settings.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (firebaseAuth.getCurrentUser()!= null){
            Uid=firebaseAuth.getCurrentUser().getUid();
            reference=firebaseFirestore.collection("Users").document(Uid);
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    validate= documentSnapshot.getDate("Valid Date");
                    emailstring=documentSnapshot.getString("Email");
                    planstring=documentSnapshot.getString("Plan Cost");
                    email.setText(emailstring);
                    plan.setText("₹ "+planstring+"/mo.");
                    date.setText(validate.toString());
                    progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseNetworkException){
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Error data not Fetched", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            });

        }


        Menu menu =bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeicon) {
                    Intent k = new Intent(Settings.this, MainScreen.class);
                    startActivity(k);
                } else if (itemId == R.id.serachicon) {
                    Intent i = new Intent(Settings.this, Search.class);
                    startActivity(i);
                } else if (itemId == R.id.settingsicon) {
                }
                return false;
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder signout= new AlertDialog.Builder(view.getContext());
                signout.setTitle("Do you really want to signout ?");
                signout.setMessage("Press YES to signout");
                signout.setCancelable(false);
                signout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent x = new Intent(Settings.this,SignIn.class);
                        startActivity(x);
                        finish();

                    }
                });
                signout.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                signout.create().show();

            }
        });
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(Settings.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (newpassword.getText().toString().length()>7){
                    firebaseAuth.signInWithEmailAndPassword(emailstring,newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                            EditText changepassword = new EditText(view.getContext());
                            AlertDialog.Builder updatepassword = new AlertDialog.Builder(view.getContext());
                            updatepassword.setTitle("Update Password?");
                            changepassword.setSingleLine();
                            updatepassword.setCancelable(false);
                            changepassword.setHint("New Password");
                            updatepassword.setView(changepassword);
                            updatepassword.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressDialog.show();
                                    String newpasswordstring = changepassword.getText().toString();
                                    if (newpasswordstring.length() > 7) {
                                        user.updatePassword(newpasswordstring).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                                                newpassword.setText("");
                                                progressDialog.cancel();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if (e instanceof FirebaseNetworkException)
                                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getApplicationContext(), "Password not updated", Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_SHORT).show();
                                        progressDialog.cancel();
                                    }
                                }
                            });
                            updatepassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    newpassword.setText("");
                                    progressDialog.cancel();
                                }
                            });
                            updatepassword.create().show();
                        }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseNetworkException)
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            if (e instanceof FirebaseAuthInvalidCredentialsException)
                                newpassword.setError("Incorrect password");
                            else
                                newpassword.setError("Incorrect password");
                            progressDialog.cancel();

                        }
                    });

                }else{
                    newpassword.setError("Password too short");
                    progressDialog.cancel();
                }

            }
        });

    }
}