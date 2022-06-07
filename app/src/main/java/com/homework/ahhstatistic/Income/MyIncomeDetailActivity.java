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
    private TextView income, earning1, earning2, earning3, totalAmount, checkMethod, detailDate;
    private TextView amount811, percent811, fullProfit811, paidProfit811, tv811;
    private TextView amount58, percent58, fullProfit58, paidProfit58, tv58;
    private TextView amount456, percent456, fullProfit456, paidProfit456, tv456;
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rl = findViewById(R.id.income_detail_rl);
        income = findViewById(R.id.income_detail_total_income);

        earning1 = findViewById(R.id.income_detail_earning_1);
        earning2 = findViewById(R.id.income_detail_earning_2);
        earning3 = findViewById(R.id.income_detail_earning_3);
        totalAmount = findViewById(R.id.income_detail_total_amount);
        checkMethod = findViewById(R.id.detail_check_method);
        detailDate = findViewById(R.id.income_detail_date);

        amount811 = findViewById(R.id.income_detail_amount1);
        percent811 = findViewById(R.id.income_detail_percent1);
        fullProfit811 = findViewById(R.id.income_detail_full_profit1);
        paidProfit811 = findViewById(R.id.income_detail_paid_profit1);
        tv811 = findViewById(R.id.income_detail_tv2);

        amount58 = findViewById(R.id.income_detail_amount2);
        percent58 = findViewById(R.id.income_detail_percent2);
        fullProfit58 = findViewById(R.id.income_detail_full_profit2);
        paidProfit58 = findViewById(R.id.income_detail_paid_profit2);
        tv58 = findViewById(R.id.income_detail_tv3);

        amount456 = findViewById(R.id.income_detail_amount3);
        percent456 = findViewById(R.id.income_detail_percent3);
        fullProfit456 = findViewById(R.id.income_detail_full_profit3);
        paidProfit456 = findViewById(R.id.income_detail_paid_profit3);
        tv456 = findViewById(R.id.income_detail_tv4);

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

            income.setText(nf.format(Integer.parseInt(item.getTotalIncome())) + " Ks");
            income.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.normal_text));

            totalAmount.setText(nf.format(Integer.parseInt(item.getTotalAmount())) + " Ks");

            amount811.setText(nf.format(Integer.parseInt(item.getAmount811())) + " Ks");
            amount58.setText(nf.format(Integer.parseInt(item.getAmount58())) + " Ks");
            amount456.setText(nf.format(Integer.parseInt(item.getAmount456())) + " Ks");

            percent811.setText(nf.format(Integer.parseInt(item.getPercent811())));
            percent58.setText(nf.format(Integer.parseInt(item.getPercent58())));
            percent456.setText(nf.format(Integer.parseInt(item.getPercent456())));

            paidProfit811.setText(nf.format(Integer.parseInt(item.getPaidProfit811())) + " Ks");
            paidProfit58.setText(nf.format(Integer.parseInt(item.getPaidProfit58())) + " Ks");
            paidProfit456.setText(nf.format(Integer.parseInt(item.getPaidProfit456())) + " Ks");

            tv811.setText("(" +item.getPercent811() + "%) profit -");
            fullProfit811.setText(nf.format(Integer.parseInt(item.getFullProfit811())) + " Ks");
            earning1.setText(nf.format(Integer.parseInt(item.getEarning811())) + " Ks");

            tv58.setText("(" + item.getPercent58() + "%) profit -");
            fullProfit58.setText(nf.format(Integer.parseInt(item.getFullProfit58())) + " Ks");
            earning2.setText(nf.format(Integer.parseInt(item.getEarning58())) + " Ks");

            tv456.setText("(" + item.getPercent456() + "%) profit -");
            fullProfit456.setText(nf.format(Integer.parseInt(item.getFullProfit456())) + " Ks");
            earning3.setText(nf.format(Integer.parseInt(item.getEarning456())) + " Ks");

            checkMethod.setText("check method:\nall earnings - " + nf.format(Integer.parseInt(item.getTotalDailyProfit())) + " Ks (all daily profit)");
            detailDate.setText(item.getCurrentDate());

        });
    }
}