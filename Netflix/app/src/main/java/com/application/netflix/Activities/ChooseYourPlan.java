package com.application.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.application.netflix.R;

public class ChooseYourPlan extends AppCompatActivity {
    TextView signin;
    Button continuebtn;
    RadioButton radiobasic,radiostd,radioprem;
    String planname,plancost,planformatofcost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_plan);
        signin = findViewById(R.id.signinstepone);
        continuebtn= findViewById(R.id.continuebtn);

        radiobasic=findViewById(R.id.radiobasicbtn);
        radiobasic.setOnCheckedChangeListener(new Radio_check());

        radiostd=findViewById(R.id.radiostdbtn);
        radioprem=findViewById(R.id.radioprembtn);
        radioprem.setOnCheckedChangeListener(new Radio_check());
        radiostd.setOnCheckedChangeListener(new Radio_check());
        radioprem.setChecked(true);

        continuebtn.setOnClickListener(view -> {
            Intent i = new Intent(ChooseYourPlan.this,FinishUpAccount.class);
            i.putExtra("PlanName",planname);
            i.putExtra("PlanCost",plancost);
            i.putExtra("PlanCostFormat",planformatofcost);
            startActivity(i);
        });

        signin.setOnClickListener(view -> {
            Intent i = new Intent(ChooseYourPlan.this,SignIn.class);
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
}