package com.homework.ahhstatistic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.homework.ahhstatistic.calculator.DailyProfitCalculator;
import com.homework.ahhstatistic.investor.InvestorCategoryActivity;
import com.homework.ahhstatistic.level.CompanyPercentPlanActivity;

public class MainActivity extends AppCompatActivity {
    CardView investor, plan, payment, calculator;
    FirebaseAuth mAuth;
    Button btnSignout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        investor = findViewById(R.id.cv_investor);
        plan = findViewById(R.id.cv_level);
        payment = findViewById(R.id.cv_payment);
        calculator = findViewById(R.id.cv_calculator);
        btnSignout = findViewById(R.id.btn_sign_out);

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOutFirebase();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        investor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InvestorCategoryActivity.class));
            }
        });

        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CompanyPercentPlanActivity.class));
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DailyProfitCalculator.class));
            }
        });
    }

    private void SignOutFirebase() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Sign Out")
                .setMessage("Do you want to sign out?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}