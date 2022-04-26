package com.homework.ahhstatistic.investor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.homework.ahhstatistic.R;

public class InvestorDetail extends AppCompatActivity {
    public static final String SENT_NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_detail);
    }
}