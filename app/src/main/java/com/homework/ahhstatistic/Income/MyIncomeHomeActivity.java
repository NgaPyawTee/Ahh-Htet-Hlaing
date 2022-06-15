package com.homework.ahhstatistic.Income;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
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
    private TextView income, earning1, earning2, earning3, totalAmount, checkMethod;
    private TextView amount811, percent811, fullProfit811, paidProfit811, tv811;
    private TextView amount58, percent58, fullProfit58, paidProfit58, tv58;
    private TextView amount456, percent456, fullProfit456, paidProfit456, tv456;
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");
    private CollectionReference collRef2 = FirebaseFirestore.getInstance().collection("Earnings");
    private NumberFormat nf = NumberFormat.getInstance();
    private List<Investor> list;
    private Button btnCalulate;
    private ProgressBar progressBar;

    private int TotalIncome , TotalAmount, TotalpaidProfit, TotalfullProfit, intFullProfit1, intIncome1, intFullProfit2, intIncome2, intFullProfit3, intIncome3;
    private int ttamount1, ttamount2, ttamount3, paidProfit1, paidProfit2, paidProfit3, ttDailyProfit, ttCashBonus,
            amount1, amount2, amount3, percent1, percent2, percent3, cashBonus, dailyProfit, CashPlusDaily;
    private String date1, date2, date3;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income_home);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        toolbar = findViewById(R.id.income_home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        rl = findViewById(R.id.income_home_rl);
        income = findViewById(R.id.income_home_total_income);

        earning1 = findViewById(R.id.earning_1);
        earning2 = findViewById(R.id.earning_2);
        earning3 = findViewById(R.id.earning_3);
        totalAmount = findViewById(R.id.income_home_total_amount);
        checkMethod = findViewById(R.id.home_check_method);

        amount811 = findViewById(R.id.income_home_amount1);
        percent811 = findViewById(R.id.income_home_percent1);
        fullProfit811 = findViewById(R.id.income_home_full_profit1);
        paidProfit811 = findViewById(R.id.income_home_paid_profit1);
        tv811 = findViewById(R.id.income_home_tv2);

        amount58 = findViewById(R.id.income_home_amount2);
        percent58 = findViewById(R.id.income_home_percent2);
        fullProfit58 = findViewById(R.id.income_home_full_profit2);
        paidProfit58 = findViewById(R.id.income_home_paid_profit2);
        tv58 = findViewById(R.id.income_home_tv3);

        amount456 = findViewById(R.id.income_home_amount3);
        percent456 = findViewById(R.id.income_home_percent3);
        fullProfit456 = findViewById(R.id.income_home_full_profit3);
        paidProfit456 = findViewById(R.id.income_home_paid_profit3);
        tv456 = findViewById(R.id.income_home_tv4);


        progressBar = findViewById(R.id.income_home_progressbar);

        btnCalulate = findViewById(R.id.income_home_calculate);

        list = new ArrayList<>();

        btnCalulate.setOnClickListener(view -> {
            if (percent811.getText().toString().trim().equals("") | percent58.getText().toString().trim().equals("") | percent456.getText().toString().trim().equals("")) {
                Toast.makeText(MyIncomeHomeActivity.this, "Please insert all 3 percentage", Toast.LENGTH_SHORT).show();
            } else {
                CalculatePercent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_home_menu_item, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (income.getText().toString().equals("Not yet calculated")) {
                    Toast.makeText(MyIncomeHomeActivity.this, "Calculate your income first", Toast.LENGTH_SHORT).show();
                } else {
                    SaveMonthlyIncome();
                }
                break;
            case R.id.action_show_list:
                startActivity(new Intent(MyIncomeHomeActivity.this, MyIncomeCategoryActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SaveMonthlyIncome() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_progress_dialog_4);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setCancelable(false);

        String currentDate = sdf.format(Calendar.getInstance().getTime());
        String millisTime = String.valueOf(System.currentTimeMillis());

        collRef2.add(new Income(String.valueOf(TotalIncome), String.valueOf(TotalAmount), String.valueOf(CashPlusDaily),
                String.valueOf(ttamount1), percent811.getText().toString(), String.valueOf(intFullProfit1), String.valueOf(paidProfit1), String.valueOf(intIncome1),
                String.valueOf(ttamount2),  percent58.getText().toString(), String.valueOf(intFullProfit2), String.valueOf(paidProfit2), String.valueOf(intIncome2),
                String.valueOf(ttamount3), percent456.getText().toString(), String.valueOf(intFullProfit3), String.valueOf(paidProfit3), String.valueOf(intIncome3),
                currentDate, millisTime)).addOnSuccessListener(this, documentReference -> {
            Handler handler = new Handler();
            handler.postDelayed((Runnable) () -> {
                dialog.dismiss();
                Toast.makeText(MyIncomeHomeActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
            }, 3000);
        });
    }

    private void CalculatePercent() {
        intFullProfit1 = (int) (ttamount1 * Integer.parseInt(percent811.getText().toString()) * 0.01);
        intIncome1 = intFullProfit1 - paidProfit1;

        intFullProfit2 = (int) (ttamount2 * Integer.parseInt(percent58.getText().toString()) * 0.01);
        intIncome2 = intFullProfit2 - paidProfit2;

        intFullProfit3 = (int) (ttamount3 * Integer.parseInt(percent456.getText().toString()) * 0.01);
        intIncome3 = intFullProfit3 - paidProfit3;

        tv811.setText("(" + percent811.getText().toString() + "%) profit -");
        fullProfit811.setText(nf.format(intFullProfit1) + " Ks");
        earning1.setText(nf.format(intIncome1) + " Ks");

        tv58.setText("(" + percent58.getText().toString() + "%) profit -");
        fullProfit58.setText(nf.format(intFullProfit2) + " Ks");
        earning2.setText(nf.format(intIncome2) + " Ks");

        tv456.setText("(" + percent456.getText().toString() + "%) profit -");
        fullProfit456.setText(nf.format(intFullProfit3) + " Ks");
        earning3.setText(nf.format(intIncome3) + " Ks");

        TotalfullProfit = intFullProfit1 + intFullProfit2 + intFullProfit3;

        TotalIncome = TotalfullProfit - TotalpaidProfit;

        income.setText(nf.format(TotalIncome) + " Ks");
        income.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.normal_text));

    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
       Zero();

        collRef.get().addOnSuccessListener(this, queryDocumentSnapshots -> {
            progressBar.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);

            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                Investor item = ds.toObject(Investor.class);

                amount1 = Integer.parseInt(item.getAmount811Cash()) + Integer.parseInt(item.getAmount811Banking());
                amount2 = Integer.parseInt(item.getAmount58Cash()) + Integer.parseInt(item.getAmount58Banking());
                amount3 = Integer.parseInt(item.getAmount456Cash()) + Integer.parseInt(item.getAmount456Banking());
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

                CalculateTotalAmount(amount1, amount2, amount3, date1, date2, date3);
                CalculateProfit(amount1, amount2, amount3, percent1, percent2, percent3, date1, date2, date3, cashBonus, dailyProfit);
            }

            TotalAmount += ttamount1 + ttamount2 + ttamount3;
            TotalpaidProfit += paidProfit1 + paidProfit2 + paidProfit3 + ttDailyProfit + ttCashBonus;

            amount811.setText(nf.format(ttamount1) + " Ks");
            paidProfit811.setText(nf.format(paidProfit1) + " Ks");

            amount58.setText(nf.format(ttamount2) + " Ks");
            paidProfit58.setText(nf.format(paidProfit2) + " Ks");

            amount456.setText(nf.format(ttamount3) + " Ks");
            paidProfit456.setText(nf.format(paidProfit3) + " Ks");

            totalAmount.setText(nf.format(TotalAmount) + " Ks");

            if (ttDailyProfit == 0 && ttCashBonus == 0){
                checkMethod.setText("Check method :\n all earnings");
            }else{
                CashPlusDaily = ttDailyProfit + ttCashBonus;
                checkMethod.setText("Check method :\nall earnings - "+nf.format(CashPlusDaily)+" Ks");
            }
        });
    }

    private void Zero() {
        TotalAmount = 0;
        TotalIncome = 0;
        TotalpaidProfit = 0;
        TotalfullProfit = 0;

        intFullProfit1 = 0;
        intFullProfit2 = 0;
        intFullProfit3 = 0;

        intIncome1 = 0;
        intIncome2 = 0;
        intIncome3 = 0;

        ttamount1 = 0;
        ttamount2 = 0;
        ttamount3 = 0;

        paidProfit1 = 0;
        paidProfit2 = 0;
        paidProfit3 = 0;

        ttDailyProfit = 0;

        amount1 = 0;
        amount3 = 0;
        amount3 = 0;

        percent1 = 0;
        percent2 = 0;
        percent3 = 0;

        cashBonus = 0;
        dailyProfit = 0;
    }

    private void CalculateTotalAmount(int amount1, int amount2, int amount3,
                                      String date1, String date2, String date3) {

        int monthly1 = 0, monthly2 = 0, monthly3 = 0;
        int dateDiff1 = 0, dateDiff2 = 0, dateDiff3 = 0;

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

                if (dateDiff1 >= -1 && dateDiff1 < 4) {
                    monthly1 = amount1;
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

                if (dateDiff2 >= -1 && dateDiff2 < 4) {
                    monthly2 = amount2;
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

                if (dateDiff3 >= -1 && dateDiff3 < 4) {
                    monthly3 = amount3;
                } else {
                    monthly3 = 0;
                }
            }

            ttamount1 += monthly1;
            ttamount2 += monthly2;
            ttamount3 += monthly3;

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void CalculateProfit(int amount1, int amount2, int amount3, int percent1, int percent2, int percent3,
                                 String date1, String date2, String date3, int cashBonus, int dailyProfit) {
        int dateDiff1 = 0, dateDiff2 = 0, dateDiff3 = 0;
        int monthly1 = 0, monthly2 = 0, monthly3 = 0;

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
                    monthly1 = (int) (amount1 * percent1 * 0.01);
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

            paidProfit1 += monthly1;
            paidProfit2 += monthly2;
            paidProfit3 += monthly3;
            ttDailyProfit += dailyProfit;
            ttCashBonus += cashBonus;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}