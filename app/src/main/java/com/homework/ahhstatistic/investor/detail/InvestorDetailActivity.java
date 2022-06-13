package com.homework.ahhstatistic.investor.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.investor.UpdateInvestorActivity;
import com.homework.ahhstatistic.model.DeletedInvestor;
import com.homework.ahhstatistic.model.ExpiredDate;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InvestorDetailActivity extends AppCompatActivity {
    private String ID;
    public static final String ID_PASS = "ID Pass";
    public static final String BUNDLE_PASS = "Bundle Pass";
    private Toolbar toolbar;
    private ViewPager pager;
    private TabLayout tabLayout;
    Bundle bundle = new Bundle();

    //Total profit dialog
    private Dialog totalProfitDiaglog;
    private TextView tvTotalProfit;
    private Button btnTotalProfit;

    //Alert Dialog
    private Dialog alertDialog;
    private TextView alert_title, alert_description, alert_tv_1, alert_tv_2;

    //Deleted Investors
    private String name, companyID, phone, nrc, address, date811, date58, date456;
    private String strAmount811Cash, strAmount811Banking, strPercent811, strDate811,
            strAmount58Cash, strAmount58Banking, strPercent58, strDate58,
            strAmount456Cash, strAmount456Banking, strPercent456, strDate456, strNRCImgUrl;
    private String ImgOne, ImgTwo, ImgThree, TotalProfit;
    private List<ExpiredDate> firstData = new ArrayList<>();
    private List<ExpiredDate> secondData = new ArrayList<>();
    private List<ExpiredDate> thirdData = new ArrayList<>();

    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");
    private int intAmount811, intPercent811, intAmount58, intPercent58, intAmount456, intPercent456, intCashBonus, preProfit, totalProfit;
    private NumberFormat nf = NumberFormat.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_detail);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        Intent intent = getIntent();
        ID = intent.getStringExtra(ID_PASS);

         //Alert Dialog
        alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.layout_alert_dialog);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alert_title = alertDialog.findViewById(R.id.alert_dialog_title);
        alert_title.setText("Remove Investor");
        alert_description = alertDialog.findViewById(R.id.alert_dialog_description);
        alert_description.setText("Are you sure you want to remove this investor?");
        alert_tv_1 = alertDialog.findViewById(R.id.alert_dialog_tv_1);
        alert_tv_1.setText("NO");
        alert_tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alert_tv_2 = alertDialog.findViewById(R.id.alert_dialog_tv_2);
        alert_tv_2.setText("YES");
        alert_tv_2.setOnClickListener(view1 -> {
            if (intAmount811 == 0 && intAmount58 == 0 && intAmount456 == 0) {
                alert_title.setText("Removing...");
                alert_description.setText("Please wait a few seconds.");
                alert_tv_1.setText("");
                alert_tv_2.setText("");

                FirebaseFirestore.getInstance().collection("Deleted Investors")
                        .add(new DeletedInvestor (name, companyID, phone, nrc, address,
                                strAmount811Cash, strAmount811Banking, strPercent811, strDate811,
                                strAmount58Cash, strAmount58Banking, strPercent58, strDate58,
                                strAmount456Cash, strAmount456Banking, strPercent456, strDate456,
                                TotalProfit, ImgOne, ImgTwo, ImgThree,  strNRCImgUrl))
                        .addOnSuccessListener(InvestorDetailActivity.this, documentReference -> {
                            DeleteSubCollection();
                            DeleteCollection();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.dismiss();
                                    Toast.makeText(InvestorDetailActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, 3000);
                        });
            } else {
                Toast.makeText(InvestorDetailActivity.this, "You need to delete all contracts", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        //Total Profit Dialog
        totalProfitDiaglog = new Dialog(this);
        totalProfitDiaglog.setContentView(R.layout.layout_total_profit_dialog);
        totalProfitDiaglog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tvTotalProfit = totalProfitDiaglog.findViewById(R.id.total_profit);
        btnTotalProfit = totalProfitDiaglog.findViewById(R.id.btn_total_profit);
        btnTotalProfit.setOnClickListener(view13 -> totalProfitDiaglog.dismiss());

        bundle.putString(BUNDLE_PASS,ID);

        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager = findViewById(R.id.detail_pager);
        tabLayout = findViewById(R.id.detail_tablayout);
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    fragment = new FirstContractFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 2:
                    fragment = new SecondContractFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 3:
                    fragment = new ThirdContractFragment();
                    fragment.setArguments(bundle);
                    return fragment;
            }
            return new HomeFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    String home = "Home";
                    return home;
                case 1:
                    String firstPlan = "1st";
                    return firstPlan;
                case 2:
                    String secondPlan = "2nd";
                    return secondPlan;
                case 3:
                    String finalPlan = "3rd";
                    return finalPlan;
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.investor_detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                alertDialog.show();
                break;
            case R.id.action_profit:
                CalculateTotalProfit();
                totalProfitDiaglog.show();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(InvestorDetailActivity.this, UpdateInvestorActivity.class);
                intent.putExtra(UpdateInvestorActivity.ID_PASS, ID);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void CalculateTotalProfit() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff1 = 0, dateDiff2 = 0, dateDiff3 = 0;
        int monthly1 = 0, monthly2 = 0, monthly3 = 0;

        Calendar c = Calendar.getInstance();
        String strcurrentDate = sdf.format(c.getTime());

        try {
            if (date811.equals("Unavaliable")) {
                dateDiff1 = 0;
            } else {
                Date currentDate = sdf.parse(strcurrentDate);
                Date date = sdf.parse(date811);

                long currentLong = currentDate.getTime();
                long long1 = date.getTime();

                int currentYear = currentDate.getYear();
                int year = date.getYear();

                if (currentYear == year) {
                    dateDiff1 = Calendar.getInstance().get(Calendar.MONTH) - date.getMonth();
                } else {
                    Period period = new Period(long1, currentLong, PeriodType.yearMonthDay());
                    dateDiff1 = period.getMonths();
                }

                if (dateDiff1 >= 4) {
                    monthly1 = (int) (intAmount811 * intPercent811 * 0.01 * 4);
                } else if (dateDiff1 < 4 && dateDiff1 > 0) {
                    monthly1 = (int) (intAmount811 * intPercent811 * 0.01 * dateDiff1);
                } else {
                    monthly1 = 0;
                }
            }

            if (date58.equals("Unavaliable")) {
                dateDiff2 = 0;
            } else {
                Date currentDate = sdf.parse(strcurrentDate);
                Date date = sdf.parse(date58);

                long currentLong = currentDate.getTime();
                long long1 = date.getTime();

                int currentYear = currentDate.getYear();
                int year = date.getYear();

                if (currentYear == year) {
                    dateDiff2 = Calendar.getInstance().get(Calendar.MONTH) - date.getMonth();
                } else {
                    Period period = new Period(long1, currentLong, PeriodType.yearMonthDay());
                    dateDiff2 = period.getMonths();
                }

                if (dateDiff2 >= 4) {
                    monthly2 = (int) (intAmount58 * intPercent58 * 0.01 * 4);
                } else if (dateDiff2 < 4 && dateDiff2 > 0) {
                    monthly2 = (int) (intAmount58 * intPercent58 * 0.01 * dateDiff2);
                } else {
                    monthly2 = 0;
                }
            }

            if (date456.equals("Unavaliable")) {
                dateDiff3 = 0;
            } else {
                Date currentDate = sdf.parse(strcurrentDate);
                Date date = sdf.parse(date456);

                long currentLong = currentDate.getTime();
                long long1 = date.getTime();

                int currentYear = currentDate.getYear();
                int year = date.getYear();

                if (currentYear == year) {
                    dateDiff3 = Calendar.getInstance().get(Calendar.MONTH) - date.getMonth();
                } else {
                    Period period = new Period(long1, currentLong, PeriodType.yearMonthDay());
                    dateDiff3 = period.getMonths();
                }

                if (dateDiff3 >= 4) {
                    monthly3 = (int) (intAmount456 * intPercent456 * 0.01 * 4);
                } else if (dateDiff3 < 4 && dateDiff3 > 0) {
                    monthly3 = (int) (intAmount456 * intPercent456 * 0.01 * dateDiff3);
                } else {
                    monthly3 = 0;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        totalProfit = preProfit + monthly1 + monthly2 + monthly3;

        tvTotalProfit.setText(nf.format(totalProfit) + " Ks");
    }

    private void DeleteSubCollection() {
        FirebaseFirestore.getInstance().collection("Investors/" + ID + "/First Date").get()
                .addOnCompleteListener(InvestorDetailActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + ID + "/First Date").document(qs.getId()).delete();
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Investors/" + ID + "/Second Date").get()
                .addOnCompleteListener(InvestorDetailActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + ID + "/Second Date").document(qs.getId()).delete();
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Investors/" + ID + "/Third Date").get()
                .addOnCompleteListener(InvestorDetailActivity.this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + ID + "/Third Date").document(qs.getId()).delete();
                        }
                    }
                });
    }

    private void DeleteCollection() {
        collRef.document(ID).delete();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
        FetchingLatestData();
    }

    private void FetchingLatestData() {
        collRef.document(ID).collection("First Date").orderBy("currentTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(InvestorDetailActivity.this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            ExpiredDate item = ds.toObject(ExpiredDate.class);
                            firstData.add(item);
                        }

                        if (firstData.size() > 0){
                            strAmount811Cash = firstData.get(0).getAmountCash();
                            strAmount811Banking = firstData.get(0).getAmountBanking();
                            strPercent811 = firstData.get(0).getPercent();
                            strDate811 = firstData.get(0).getDate();
                            ImgOne = firstData.get(0).getImageUrl();
                        }else{
                            strAmount811Cash = "";
                            strAmount811Banking = "";
                            strPercent811 = "";
                            strDate811 = "";
                            ImgOne = "";
                        }

                    }
                });

        collRef.document(ID).collection("Second Date").orderBy("currentTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(InvestorDetailActivity.this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            ExpiredDate item = ds.toObject(ExpiredDate.class);
                            secondData.add(item);
                        }

                        if (secondData.size() > 0){
                            strAmount58Cash = secondData.get(0).getAmountCash();
                            strAmount58Banking = secondData.get(0).getAmountBanking();
                            strPercent58 = secondData.get(0).getPercent();
                            strDate58 = secondData.get(0).getDate();
                            ImgTwo = secondData.get(0).getImageUrl();
                        }else{
                            strAmount58Cash = "";
                            strAmount58Banking = "";
                            strPercent58 = "";
                            strDate58 = "";
                            ImgTwo = "";
                        }

                    }
                });

        collRef.document(ID).collection("Third Date").orderBy("currentTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(InvestorDetailActivity.this, new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            ExpiredDate item = ds.toObject(ExpiredDate.class);
                            thirdData.add(item);
                        }

                        if (thirdData.size() > 0){
                            strAmount456Cash = thirdData.get(0).getAmountCash();
                            strAmount456Banking = thirdData.get(0).getAmountBanking();
                            strPercent456 = thirdData.get(0).getPercent();
                            strDate456 = thirdData.get(0).getDate();
                            ImgThree = thirdData.get(0).getImageUrl();
                        }else{
                            strAmount456Cash = "";
                            strAmount456Banking = "";
                            strPercent456 = "";
                            strDate456 = "";
                            ImgThree = "";
                        }

                    }
                });
    }

    private void RetrieveData() {
        collRef.document(ID).get().addOnSuccessListener(InvestorDetailActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("name");
                companyID = documentSnapshot.getString("companyID");
                phone = documentSnapshot.getString("phone");
                nrc = documentSnapshot.getString("nrc");
                address = documentSnapshot.getString("address");
                TotalProfit = documentSnapshot.getString("preProfit");
                strNRCImgUrl = documentSnapshot.getString("nrcImgUrl");

                if (documentSnapshot.getString("amount811Cash").equals("0") && documentSnapshot.getString("amount811Banking").equals("0")
                        && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                    date811 = "Unavaliable";

                    intAmount811 = 0;
                    intPercent811 = 0;
                } else {
                    date811 = documentSnapshot.getString("date811");

                    intAmount811 = Integer.parseInt(documentSnapshot.getString("amount811Cash")) + Integer.parseInt(documentSnapshot.getString("amount811Banking"));
                    intPercent811 = Integer.parseInt(documentSnapshot.getString("percent811"));
                }

                if (documentSnapshot.getString("amount58Cash").equals("0") && documentSnapshot.getString("amount58Banking").equals("0")
                        && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                    date58 = "Unavaliable";

                    intAmount58 = 0;
                    intPercent58 = 0;
                } else {
                    date58 = documentSnapshot.getString("date58");

                    intAmount58 = Integer.parseInt(documentSnapshot.getString("amount58Cash")) + Integer.parseInt(documentSnapshot.getString("amount58Banking"));
                    intPercent58 = Integer.parseInt(documentSnapshot.getString("percent58"));
                }

                if (documentSnapshot.getString("amount456Cash").equals("0") && documentSnapshot.getString("amount456Banking").equals("0")
                        && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                    date456 = "Unavaliable";

                    intAmount456 = 0;
                    intPercent456 = 0;
                } else {
                    date456 = documentSnapshot.getString("date456");

                    intAmount456 = Integer.parseInt(documentSnapshot.getString("amount456Cash")) + Integer.parseInt(documentSnapshot.getString("amount456Banking"));
                    intPercent456 = Integer.parseInt(documentSnapshot.getString("percent456"));
                }

                intCashBonus = Integer.parseInt(documentSnapshot.getString("cashBonus"));
                preProfit = Integer.parseInt(documentSnapshot.getString("preProfit"));
            }
        });
    }
}