package com.homework.ahhstatistic.profit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthlyProfitDetailActivity extends AppCompatActivity {
    public static final String ID_PASS = "idPass";
    public static final String POS_PASS = "posPass";

    private String ID;
    private String position;

    private TextView count, name, cashBonus, dailyProfit, profit1, profit2, profit3,
            amount1, percent1, amount2, percent2, amount3, percent3, totalProfit;
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");

    private NumberFormat nf = NumberFormat.getInstance();
    private ProgressBar progressBar;
    private RelativeLayout rl;
    private int intProfit1, intProfit2, intProfit3, intTotalProfit;

    //Profit Dialog
    private Dialog ProfitDialog;
    private TextView tvTitle;
    private EditText edtProfit;
    private Button btnProfit;

    private RelativeLayout rl1, rl2, rl3;

    //Contract Dialog
    private Dialog ContractDialog;
    private TextView contractTitle, contractCash, contractBanking;

    private String strCash811, strBanking811, strCash58, strBanking58, strCash456, strBanking456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_profit_detail);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Intent intent = getIntent();
        ID = intent.getStringExtra(ID_PASS);
        position = intent.getStringExtra(POS_PASS);

        ProfitDialog = new Dialog(this);
        ProfitDialog.setContentView(R.layout.layout_daily_profit_dialog);
        ProfitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tvTitle = ProfitDialog.findViewById(R.id.profit_detail_dialog_title);
        edtProfit = ProfitDialog.findViewById(R.id.profit_detail_dialog_edt);
        btnProfit = ProfitDialog.findViewById(R.id.profit_detail_dialog_btn);

        count = findViewById(R.id.profit_detail_count);
        count.setText(position);

        name = findViewById(R.id.profit_detail_name);
        cashBonus = findViewById(R.id.profit_detail_cashBonus);
        dailyProfit = findViewById(R.id.profit_detail_dailyProfit);
        profit1 = findViewById(R.id.profit_detail_1st_profit);
        profit2 = findViewById(R.id.profit_detail_2nd_profit);
        profit3 = findViewById(R.id.profit_detail_3rd_profit);
        amount1 = findViewById(R.id.profit_detail_1st_amount);
        amount2 = findViewById(R.id.profit_detail_2nd_amount);
        amount3 = findViewById(R.id.profit_detail_3rd_amount);
        percent1 = findViewById(R.id.profit_detail_1st_percent);
        percent2 = findViewById(R.id.profit_detail_2nd_percent);
        percent3 = findViewById(R.id.profit_detail_3rd_percent);
        totalProfit = findViewById(R.id.profit_detail_total_profit);

        progressBar = findViewById(R.id.profit_detail_progress_bar);
        rl = findViewById(R.id.profit_detail_rl);

        cashBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtProfit.setText(null);
                ProfitDialog.show();
                btnProfit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtProfit.getText().toString().trim().equals("")) {
                            ProfitDialog.dismiss();
                        } else {
                            UpdateCashBonus();
                        }
                    }
                });
            }
        });
        dailyProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProfit.setText(null);
                ProfitDialog.show();
                btnProfit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtProfit.getText().toString().trim().equals("")) {
                            ProfitDialog.dismiss();
                        } else {
                            UpdateDailyProfit();
                        }
                    }
                });
            }
        });

        ContractDialog = new Dialog(this);
        ContractDialog.setContentView(R.layout.layout_contract_dialog);
        ContractDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        contractTitle = ContractDialog.findViewById(R.id.PD_dialog_title);
        contractCash = ContractDialog.findViewById(R.id.PD_dialog_tv_cash);
        contractBanking = ContractDialog.findViewById(R.id.PD_dialog_tv_banking);

        rl1 = findViewById(R.id.profit_detail_1st_contract);
        rl2 = findViewById(R.id.profit_detail_2nd_contract);
        rl3 = findViewById(R.id.profit_detail_3rd_contract);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profit1.getText().toString().equals("Unavaliable")) {
                    contractTitle.setText("1st Contract");
                    contractCash.setText(nf.format(Integer.parseInt(strCash811)) + " Ks");
                    contractBanking.setText(nf.format(Integer.parseInt(strBanking811)) + " Ks");
                    ContractDialog.show();
                }
            }
        });

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profit2.getText().toString().equals("Unavaliable")) {
                    contractTitle.setText("2nd Contract");
                    contractCash.setText(nf.format(Integer.parseInt(strCash58)) + " Ks");
                    contractBanking.setText(nf.format(Integer.parseInt(strBanking58)) + " Ks");
                    ContractDialog.show();
                }
            }
        });

        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profit3.getText().toString().equals("Unavaliable")) {
                    contractTitle.setText("3rd Contract");
                    contractCash.setText(nf.format(Integer.parseInt(strCash456)) + " Ks");
                    contractBanking.setText(nf.format(Integer.parseInt(strBanking456)) + " Ks");
                    ContractDialog.show();
                }
            }
        });
    }

    private void UpdateCashBonus() {
        tvTitle.setText("Updating...");
        collRef.document(ID).update("cashBonus", edtProfit.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                collRef.document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        intTotalProfit = intProfit1 + intProfit2 + intProfit3 +
                                Integer.parseInt(documentSnapshot.getString("cashBonus")) + Integer.parseInt(documentSnapshot.getString("dailyProfit"));
                        totalProfit.setText(nf.format(intTotalProfit) + " Ks");
                        cashBonus.setText(nf.format(Integer.parseInt(edtProfit.getText().toString())) + " Ks");
                        ProfitDialog.dismiss();

                        tvTitle.setText("Enter Amount");
                    }
                });
            }
        });
    }

    private void UpdateDailyProfit() {
        tvTitle.setText("Updating...");
        collRef.document(ID).update("dailyProfit", edtProfit.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                collRef.document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int preProfit = Integer.parseInt(documentSnapshot.getString("preProfit"));
                        int newProfit = preProfit + Integer.parseInt(edtProfit.getText().toString());

                        collRef.document(ID).update("preProfit", String.valueOf(newProfit)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                intTotalProfit = intProfit1 + intProfit2 + intProfit3 +
                                        Integer.parseInt(documentSnapshot.getString("dailyProfit")) + Integer.parseInt(documentSnapshot.getString("cashBonus"));
                                totalProfit.setText(nf.format(intTotalProfit) + " Ks");
                                dailyProfit.setText(nf.format(Integer.parseInt(edtProfit.getText().toString())) + " Ks");
                                ProfitDialog.dismiss();

                                tvTitle.setText("Enter Amount");
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveDate();
    }

    private void RetrieveDate() {
        collRef.document(ID).get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Investor item = documentSnapshot.toObject(Investor.class);

                name.setText(item.getName());
                cashBonus.setText(nf.format(Integer.parseInt(item.getCashBonus())) + " Ks");
                dailyProfit.setText(nf.format(Integer.parseInt(item.getDailyProfit())) + " Ks");

                if (item.getAmount811Cash().equals("0") && item.getAmount811Banking().equals("0") && item.getPercent811().equals("") && item.getDate811().equals("")) {
                    amount1.setText("Unavaliable");
                    percent1.setText("Unavaliable");
                    profit1.setText("Unavaliable");
                } else {
                    strCash811 = item.getAmount811Cash();
                    strBanking811 = item.getAmount811Banking();

                    int amount811 = Integer.parseInt(item.getAmount811Cash()) + Integer.parseInt(item.getAmount811Banking());
                    amount1.setText(nf.format(amount811) + " Ks");
                    percent1.setText(item.getPercent811() + "%");
                    CalculateFirstProfit(amount811, item.getPercent811(), item.getDate811());
                }

                if (item.getAmount58Cash().equals("0") && item.getAmount58Banking().equals("0") && item.getPercent58().equals("") && item.getDate58().equals("")) {
                    amount2.setText("Unavaliable");
                    percent2.setText("Unavaliable");
                    profit2.setText("Unavaliable");
                } else {
                    strCash58 = item.getAmount58Cash();
                    strBanking58 = item.getAmount58Banking();

                    int amount58 = Integer.parseInt(item.getAmount58Cash()) + Integer.parseInt(item.getAmount58Banking());
                    amount2.setText(nf.format(amount58) + " Ks");
                    percent2.setText(item.getPercent58() + "%");
                    CalculateSecondProfit(amount58, item.getPercent58(), item.getDate58());
                }

                if (item.getAmount456Cash().equals("0") && item.getAmount456Banking().equals("0") && item.getPercent456().equals("") && item.getDate456().equals("")) {
                    amount3.setText("Unavaliable");
                    percent3.setText("Unavaliable");
                    profit3.setText("Unavaliable");
                } else {
                    strCash456 = item.getAmount456Cash();
                    strBanking456 = item.getAmount456Banking();

                    int amount456 = Integer.parseInt(item.getAmount456Cash()) + Integer.parseInt(item.getAmount456Banking());
                    amount3.setText(nf.format(amount456) + " Ks");
                    percent3.setText(item.getPercent456() + "%");
                    CalculateThirdProfit(amount456, item.getPercent456(), item.getDate456());
                }

                progressBar.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);

                intTotalProfit = intProfit1 + intProfit2 + intProfit3 + Integer.parseInt(item.getCashBonus()) + Integer.parseInt(item.getDailyProfit());
                totalProfit.setText(nf.format(intTotalProfit) + " Ks");
            }
        });
    }

    private void CalculateFirstProfit(int amount, String percent, String date) {
        int amount811 = amount;
        int percent811 = Integer.parseInt(percent);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff1 = 0;
        int monthly1 = 0;

        Calendar c = Calendar.getInstance();
        String strCurrentDate = sdf.format(c.getTime());

        try {
            Date currentDate = sdf.parse(strCurrentDate);
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonth();
            long currentLong = currentDate.getTime();

            Date date811 = sdf.parse(date);
            long long1 = date811.getTime();
            int year811 = date811.getYear();

            //Check year for 1st
            if (currentYear == year811) {
                dateDiff1 = currentMonth - date811.getMonth();
            } else {
                Period period = new Period(long1, currentLong, PeriodType.yearMonthDay());
                dateDiff1 = period.getMonths();
            }

            if (dateDiff1 < 0) {
                profit1.setText("Not yet valid");

                amount1.setTextColor(this.getResources().getColor(R.color.dark_green));
                percent1.setTextColor(this.getResources().getColor(R.color.dark_green));
                profit1.setTextColor(this.getResources().getColor(R.color.dark_green));
            } else if (dateDiff1 >= 0 && dateDiff1 < 4) {
                monthly1 = (int) (amount811 * percent811 * 0.01);
                profit1.setText(nf.format(monthly1) + " Ks");
                intProfit1 = monthly1;

                amount1.setTextColor(this.getResources().getColor(R.color.dark_cyan));
                percent1.setTextColor(this.getResources().getColor(R.color.dark_cyan));
                profit1.setTextColor(this.getResources().getColor(R.color.dark_cyan));
            } else {
                profit1.setText("Expired");

                amount1.setTextColor(this.getResources().getColor(R.color.red));
                percent1.setTextColor(this.getResources().getColor(R.color.red));
                profit1.setTextColor(this.getResources().getColor(R.color.red));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void CalculateSecondProfit(int amount, String percent, String date) {
        int amount58 = amount;
        int percent58 = Integer.parseInt(percent);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff2 = 0;
        int monthly2 = 0;

        Calendar c = Calendar.getInstance();
        String strCurrentDate = sdf.format(c.getTime());

        try {
            Date currentDate = sdf.parse(strCurrentDate);
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonth();
            long currentLong = currentDate.getTime();

            Date date58 = sdf.parse(date);
            long long2 = date58.getTime();
            int year58 = date58.getYear();

            //Check year for 1st
            if (currentYear == year58) {
                dateDiff2 = currentMonth - date58.getMonth();
            } else {
                Period period = new Period(long2, currentLong, PeriodType.yearMonthDay());
                dateDiff2 = period.getMonths();
            }

            if (dateDiff2 < 0) {
                profit2.setText("Not yet valid");

                amount2.setTextColor(this.getResources().getColor(R.color.dark_green));
                percent2.setTextColor(this.getResources().getColor(R.color.dark_green));
                profit2.setTextColor(this.getResources().getColor(R.color.dark_green));
            } else if (dateDiff2 >= 0 && dateDiff2 < 4) {
                monthly2 = (int) (amount58 * percent58 * 0.01);
                profit2.setText(nf.format(monthly2) + " Ks");
                intProfit2 = monthly2;

                amount2.setTextColor(this.getResources().getColor(R.color.blue));
                percent2.setTextColor(this.getResources().getColor(R.color.blue));
                profit2.setTextColor(this.getResources().getColor(R.color.blue));
            } else {
                profit2.setText("Expired");

                amount2.setTextColor(this.getResources().getColor(R.color.red));
                percent2.setTextColor(this.getResources().getColor(R.color.red));
                profit2.setTextColor(this.getResources().getColor(R.color.red));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void CalculateThirdProfit(int amount, String percent, String date) {
        int amount456 = amount;
        int percent456 = Integer.parseInt(percent);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff3 = 0;
        int monthly3 = 0;

        Calendar c = Calendar.getInstance();
        String strCurrentDate = sdf.format(c.getTime());

        try {
            Date currentDate = sdf.parse(strCurrentDate);
            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonth();
            long currentLong = currentDate.getTime();

            Date date456 = sdf.parse(date);
            long long3 = date456.getTime();
            int year456 = date456.getYear();

            //Check year for 1st
            if (currentYear == year456) {
                dateDiff3 = currentMonth - date456.getMonth();
            } else {
                Period period = new Period(long3, currentLong, PeriodType.yearMonthDay());
                dateDiff3 = period.getMonths();
            }

            if (dateDiff3 < 0) {
                profit3.setText("Not yet valid");

                amount3.setTextColor(this.getResources().getColor(R.color.dark_green));
                percent3.setTextColor(this.getResources().getColor(R.color.dark_green));
                profit3.setTextColor(this.getResources().getColor(R.color.dark_green));
            } else if (dateDiff3 >= 0 && dateDiff3 < 4) {
                monthly3 = (int) (amount456 * percent456 * 0.01);
                profit3.setText(nf.format(monthly3) + " Ks");
                intProfit3 = monthly3;

                amount3.setTextColor(this.getResources().getColor(R.color.bright_blue));
                percent3.setTextColor(this.getResources().getColor(R.color.bright_blue));
                profit3.setTextColor(this.getResources().getColor(R.color.bright_blue));
            } else {
                profit3.setText("Expired");

                amount3.setTextColor(this.getResources().getColor(R.color.red));
                percent3.setTextColor(this.getResources().getColor(R.color.red));
                profit3.setTextColor(this.getResources().getColor(R.color.red));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}