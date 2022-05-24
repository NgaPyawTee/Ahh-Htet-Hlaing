package com.homework.ahhstatistic.Income;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Income;

import java.text.NumberFormat;

public class MyIncomeDetailActivity extends AppCompatActivity {
    String ID;
    public static final String ID_PASS = "ID_PASS";

    private RelativeLayout rl;
    private Toolbar toolbar;
    private TextView income, amount, percent, fullProfit, paidProfit, tv1, date;
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Earnings");
    private NumberFormat nf = NumberFormat.getInstance();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income_detail);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Intent intent = getIntent();
        ID = intent.getStringExtra(ID_PASS);

        toolbar = findViewById(R.id.income_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_normal_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        rl = findViewById(R.id.income_detail_rl);
        income = findViewById(R.id.income_detail_total_income);
        amount = findViewById(R.id.income_detail_amount);
        percent = findViewById(R.id.income_detail_percent);
        fullProfit = findViewById(R.id.income_detail_full_profit);
        paidProfit = findViewById(R.id.income_detail_paid_profit);
        tv1 = findViewById(R.id.income_detail_tv2);
        date = findViewById(R.id.income_detail_date);
        progressBar = findViewById(R.id.income_detail_progressbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveDate();
    }

    private void RetrieveDate() {
        collRef.document(ID).get().addOnSuccessListener(this, queryDocumentSnapshots -> {
            progressBar.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);

            Income item = queryDocumentSnapshots.toObject(Income.class);

            income.setText(nf.format(Integer.parseInt(item.getIncome())) + " Ks");
            income.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.normal_text));

            amount.setText(nf.format(Integer.parseInt(item.getTotalAmount())) + " Ks");
            percent.setText(item.getPercent() + "%");
            fullProfit.setText(nf.format(Integer.parseInt(item.getFullProfit())) + " Ks");
            paidProfit.setText(nf.format(Integer.parseInt(item.getPaidProfit())) + " Ks");

            tv1.setText("(" + item.getPercent() + "%) profit -");

            date.setText(item.getDate());
        });
    }
}