package com.homework.ahhstatistic.Revenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.os.Bundle;

import com.homework.ahhstatistic.R;

public class MyRevenue extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        WindowInsetsControllerCompat windowInsetsControllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
    }
}