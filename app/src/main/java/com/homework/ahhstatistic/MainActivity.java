package com.homework.ahhstatistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.homework.ahhstatistic.calculator.MonthlyCalculator;
import com.homework.ahhstatistic.level.CompanyPercentPlanActivity;

public class MainActivity extends AppCompatActivity {
    CardView investor, plan, payment, calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        WindowInsetsControllerCompat insetsControllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        insetsControllerCompat.show(WindowInsetsCompat.Type.systemBars());

        investor = findViewById(R.id.cv_investor);
        plan = findViewById(R.id.cv_level);
        payment = findViewById(R.id.cv_payment);
        calculator = findViewById(R.id.cv_calculator);

        investor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                startActivity(new Intent(MainActivity.this, MonthlyCalculator.class));
            }
        });
    }
}