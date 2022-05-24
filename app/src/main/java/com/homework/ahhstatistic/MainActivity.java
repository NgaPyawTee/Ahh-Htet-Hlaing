package com.homework.ahhstatistic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.homework.ahhstatistic.Income.MyIncomeHomeActivity;
import com.homework.ahhstatistic.calculator.DailyProfitCalculator;
import com.homework.ahhstatistic.investor.InvestorCategoryActivity;
import com.homework.ahhstatistic.profit.MonthlyProfitActivity;

public class MainActivity extends AppCompatActivity {
    CardView investor, income, payment, calculator, sign_out;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        investor = findViewById(R.id.cv_investor);
        income = findViewById(R.id.cv_revenue);
        payment = findViewById(R.id.cv_payment);
        calculator = findViewById(R.id.cv_calculator);
        sign_out = findViewById(R.id.btn_sign_out);

        sign_out.setOnClickListener(new View.OnClickListener() {
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

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyIncomeHomeActivity.class));
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MonthlyProfitActivity.class));
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