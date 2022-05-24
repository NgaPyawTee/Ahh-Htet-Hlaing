package com.homework.ahhstatistic.Income;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Income;
import com.homework.ahhstatistic.model.Investor;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyIncomeHomeActivity extends AppCompatActivity {
    private RelativeLayout rl;
    private Toolbar toolbar;
    private TextView income, amount, percent, fullProfit, totalProfit, tv1;
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");
    private NumberFormat nf = NumberFormat.getInstance();
    private List<Investor> list;
    private Button btnCalulate, btnShow;
    private ProgressBar progressBar;

    private int TotalAmount, TotalProfit, intFullProfit, intIncome;
    private int amount1, amount2, amount3, percent1, percent2, percent3, cashBonus, dailyProfit;
    private int Tamount1, Tamount2, Tamount3;
    private String date1, date2, date3;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        toolbar = findViewById(R.id.income_home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        rl = findViewById(R.id.income_home_rl);
        income = findViewById(R.id.income_home_total_income);
        amount = findViewById(R.id.income_home_amount);
        percent = findViewById(R.id.income_home_percent);
        fullProfit = findViewById(R.id.income_home_full_profit);
        totalProfit = findViewById(R.id.income_home_total_profit);
        tv1 = findViewById(R.id.income_home_tv2);
        progressBar = findViewById(R.id.income_home_progressbar);

        btnCalulate = findViewById(R.id.income_home_calculate);
        btnShow = findViewById(R.id.income_home_show);

        list = new ArrayList<>();

        btnCalulate.setOnClickListener(view -> {
            if (percent.getText().toString().trim().equals("")) {
                Toast.makeText(MyIncomeHomeActivity.this, "Please insert percentage", Toast.LENGTH_SHORT).show();
            } else {
                CalculatePercent();
            }
        });

        btnShow.setOnClickListener(view -> {
            startActivity(new Intent(MyIncomeHomeActivity.this, MyIncomeCategoryActivity.class));
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_home_menu_item,menu);
      
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if (income.getText().toString().equals("Not yet calculated")){
                    Toast.makeText(MyIncomeHomeActivity.this,"Calculate first",Toast.LENGTH_SHORT).show();
                }else{
                    SaveMonthlyIncome();
                }
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void SaveMonthlyIncome() {
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        String strTotalAmount = String.valueOf(TotalAmount);
        String strPercent = percent.getText().toString();
        String strFullProfit = String.valueOf(intFullProfit);
        String strPaidProfit = String.valueOf(TotalProfit);
        String strIncome = String.valueOf(intIncome);
        String milleTime = String.valueOf(System.currentTimeMillis());
        
        FirebaseFirestore.getInstance().collection("Earnings")
                .add(new Income(strTotalAmount,strPercent,strFullProfit,strPaidProfit,strIncome,currentDate,milleTime))
        .addOnSuccessListener(documentReference -> {
            Toast.makeText(MyIncomeHomeActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void CalculatePercent() {
        intFullProfit = (int) (TotalAmount * Integer.parseInt(percent.getText().toString()) * 0.01);
        intIncome = intFullProfit - TotalProfit;

        tv1.setText("(" + percent.getText().toString() + "%) profit -");
        fullProfit.setText(nf.format(intFullProfit) + " Ks");

        income.setText(nf.format(intIncome) + " Ks");
        income.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.normal_text));
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        TotalProfit = 0;
        TotalAmount = 0;

        collRef.get().addOnSuccessListener(this, queryDocumentSnapshots -> {
            progressBar.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);

            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                Investor item = ds.toObject(Investor.class);

                Tamount1 = Integer.parseInt(item.getAmount811());
                Tamount2 = Integer.parseInt(item.getAmount58());
                Tamount3 = Integer.parseInt(item.getAmount456());

                TotalAmount += Tamount1 + Tamount2 + Tamount3;

                amount1 = Integer.parseInt(item.getAmount811());
                amount2 = Integer.parseInt(item.getAmount58());
                amount3 = Integer.parseInt(item.getAmount456());
                cashBonus = Integer.parseInt(item.getCashBonus());
                dailyProfit = Integer.parseInt(item.getDailyProfit());

                if (item.getPercent811().equals("")) {
                    percent1 = 0;
                } else {
                    percent1 = Integer.parseInt(item.getPercent811());
                }

                if (item.getPercent58().equals("")) {
                    percent2 = 0;
                } else {
                    percent2 = Integer.parseInt(item.getPercent58());
                }

                if (item.getPercent456().equals("")) {
                    percent3 = 0;
                } else {
                    percent3 = Integer.parseInt(item.getPercent456());
                }

                date1 = item.getDate811();
                date2 = item.getDate58();
                date3 = item.getDate456();

                CalculateProfit(amount1, amount2, amount3, percent1, percent2, percent3, date1, date2, date3, cashBonus, dailyProfit);
            }

            amount.setText(nf.format(TotalAmount) + " Ks");
            totalProfit.setText(nf.format(TotalProfit));
        });
    }

    private void CalculateProfit(int amount1, int amount2, int amount3, int percent1, int percent2, int percent3,
                                 String date1, String date2, String date3, int cashBonus, int dailyProfit) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff1 = 0, dateDiff2 = 0, dateDiff3 = 0;
        int monthly1 = 0, monthly2 = 0, monthly3 = 0;
        NumberFormat nf = NumberFormat.getInstance();

        Calendar c = Calendar.getInstance();
        String strCurrentDate = sdf.format(c.getTime());

        try {
            Date currentDate = sdf.parse(strCurrentDate);
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonth();
            long currentLong = currentDate.getTime();

            if (date1.equals("")) {
                monthly1 = 0;
            } else {
                Date date811 = sdf.parse(date1);
                long long1 = date811.getTime();
                int year811 = date811.getYear();

                //Check year for 1st
                if (currentYear == year811) {
                    dateDiff1 = currentMonth - date811.getMonth();
                } else {
                    Period period = new Period(long1, currentLong, PeriodType.yearMonthDay());
                    dateDiff1 = period.getMonths();
                }

                if (dateDiff1 >= 0 && dateDiff1 < 4) {
                    monthly1 = (int) (amount1 * percent1 * 0.01) + cashBonus;
                } else {
                    monthly1 = 0;
                }
            }

            if (date2.equals("")) {
                monthly2 = 0;
            } else {
                Date date58 = sdf.parse(date2);
                long long2 = date58.getTime();
                int year58 = date58.getYear();

                //Check year for 2nd
                if (currentYear == year58) {
                    dateDiff2 = currentMonth - date58.getMonth();
                } else {
                    Period period = new Period(long2, currentLong, PeriodType.yearMonthDay());
                    dateDiff2 = period.getMonths();
                }

                if (dateDiff2 >= 0 && dateDiff2 < 4) {
                    monthly2 = (int) (amount2 * percent2 * 0.01);
                } else {
                    monthly2 = 0;
                }
            }

            if (date3.equals("")) {
                monthly3 = 0;
            } else {
                Date date456 = sdf.parse(date3);
                long long3 = date456.getTime();
                int year456 = date456.getYear();

                //Check year for 3rd
                if (currentYear == year456) {
                    dateDiff3 = currentMonth - date456.getMonth();
                } else {
                    Period period = new Period(long3, currentLong, PeriodType.yearMonthDay());
                    dateDiff3 = period.getMonths();
                }

                if (dateDiff3 >= 0 && dateDiff3 < 4) {
                    monthly3 = (int) (amount3 * percent3 * 0.01);
                } else {
                    monthly3 = 0;
                }
            }

            TotalProfit += monthly1 + monthly2 + monthly3 + dailyProfit;

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}