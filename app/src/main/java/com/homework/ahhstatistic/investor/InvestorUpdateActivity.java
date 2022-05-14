package com.homework.ahhstatistic.investor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;

import com.homework.ahhstatistic.R;

public class InvestorUpdateActivity extends AppCompatActivity {
    public static final String ID_PASS = "IntentPass";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_update);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.blue));

        toolbar = findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Updating the investor...");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}