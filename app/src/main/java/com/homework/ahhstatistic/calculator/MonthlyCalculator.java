package com.homework.ahhstatistic.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.os.Bundle;

import com.homework.ahhstatistic.R;

public class MonthlyCalculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_calculator);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        WindowInsetsControllerCompat insetsControllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        insetsControllerCompat.show(WindowInsetsCompat.Type.systemBars());

        Toolbar toolbar = findViewById(R.id.calculator_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}