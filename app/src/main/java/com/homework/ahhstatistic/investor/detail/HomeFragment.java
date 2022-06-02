package com.homework.ahhstatistic.investor.detail;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.investor.UpdateInvestorActivity;
import com.homework.ahhstatistic.model.ExpiredDate;
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

public class HomeFragment extends Fragment {
    private String bundlePass;
    private RelativeLayout RLToolbar;
    private NestedScrollView NSV;

    private TextView name, companyID, phone, nrc, address,
            amount811, percent811, date811,
            amount58, percent58, date58,
            amount456, percent456, date456,
            cashBonus;
    private int preProfit, totalProfit;
    private ImageView imageView1, imageView2, imageView3, zoomPic, downImg, backImg, clrImg;
    private Uri imgUri1, imgUri2, imgUri3;
    private ProgressBar progressBar;
    private Button btnEdit, btnDelete, totalBtn;
    private String id;
    private CollectionReference collRef;
    public Dialog imageDialog;
    boolean visible = true;

    //Total profit dialog
    private Dialog totalProfitDiaglog;
    private TextView tvTotalProfit;
    private Button btnTotalProfit;

    //Alert Dialog
    private Dialog alertDialog;
    private TextView alert_title, alert_description, alert_tv_1, alert_tv_2;

    private NumberFormat nf = NumberFormat.getInstance();

    int intAmount811, intPercent811, intCashBonus, intAmount58, intPercent58, intAmount456, intPercent456;
    private String strAmount811, strPercent811, strDate811, strAmount58, strPercent58, strDate58, strAmount456, strPercent456, strDate456;
    private String ImgOne, ImgTwo, ImgThree, TotalProfit;
    private List<ExpiredDate> firstData = new ArrayList<>();
    private List<ExpiredDate> secondData = new ArrayList<>();
    private List<ExpiredDate> thirdData = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        collRef = FirebaseFirestore.getInstance().collection("Investors");
        progressBar = view.findViewById(R.id.detail_progress_bar);
        NSV = view.findViewById(R.id.detail_container);

        //Alert Dialog
        alertDialog = new Dialog(getContext());
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
                alert_description.setText("Please wait a few seconds");
                alert_tv_1.setText("");
                alert_tv_2.setText("");

                String strName = name.getText().toString();
                String strCompanyID = companyID.getText().toString();
                String strPhone = phone.getText().toString();
                String strNRC = nrc.getText().toString();
                String strAddress = address.getText().toString();
                String cashBonus = "0";
                String dailyProfit = "0";

                FirebaseFirestore.getInstance().collection("Deleted Investors")
                        .add(new Investor(strName, strCompanyID, strPhone, strNRC, strAddress,
                                strAmount811, strPercent811, strDate811,
                                strAmount58, strPercent58, strDate58,
                                strAmount456, strPercent456, strDate456,
                                cashBonus, dailyProfit, ImgOne, ImgTwo, ImgThree, TotalProfit))
                        .addOnSuccessListener(getActivity(), documentReference -> {
                            DeleteSubCollection();
                            DeleteCollection();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.dismiss();
                                    Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }
                            }, 3000);
                        });
            } else {
                Toast.makeText(getActivity(), "You need to delete all contracts", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }

        });


        //Image Dialog
        imageDialog = new Dialog(getContext());
        imageDialog.setContentView(R.layout.layout_image_dialog);
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageDialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        zoomPic = imageDialog.findViewById(R.id.zoom_img);
        RLToolbar = imageDialog.findViewById(R.id.dialog_toolbar);
        clrImg = imageDialog.findViewById(R.id.clear_img);
        clrImg.setVisibility(View.INVISIBLE);
        downImg = imageDialog.findViewById(R.id.down_img);
        backImg = imageDialog.findViewById(R.id.back_img);
        backImg.setOnClickListener(view12 -> imageDialog.dismiss());

        //Total Profit Dialog
        totalProfitDiaglog = new Dialog(getContext());
        totalProfitDiaglog.setContentView(R.layout.layout_total_profit_dialog);
        totalProfitDiaglog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tvTotalProfit = totalProfitDiaglog.findViewById(R.id.total_profit);
        btnTotalProfit = totalProfitDiaglog.findViewById(R.id.btn_total_profit);
        btnTotalProfit.setOnClickListener(view13 -> totalProfitDiaglog.dismiss());

        name = view.findViewById(R.id.detail_name);
        companyID = view.findViewById(R.id.detail_company_id);
        phone = view.findViewById(R.id.detail_phone);
        nrc = view.findViewById(R.id.detail_nrc);
        address = view.findViewById(R.id.detail_address);

        imageView1 = view.findViewById(R.id.detail_img_one);
        imageView2 = view.findViewById(R.id.detail_img_two);
        imageView3 = view.findViewById(R.id.detail_img_three);

        amount811 = view.findViewById(R.id.detail_plan_811_amount);
        percent811 = view.findViewById(R.id.detail_plan_811_percent);
        date811 = view.findViewById(R.id.detail_date_811);

        amount58 = view.findViewById(R.id.detail_plan_58_amount);
        percent58 = view.findViewById(R.id.detail_plan_58_percent);
        date58 = view.findViewById(R.id.detail_date_58);

        amount456 = view.findViewById(R.id.detail_plan_456_amount);
        percent456 = view.findViewById(R.id.detail_plan_456_percent);
        date456 = view.findViewById(R.id.detail_date_456);

        cashBonus = view.findViewById(R.id.cash_bonus);

        totalBtn = view.findViewById(R.id.detail_total_btn);
        btnEdit = view.findViewById(R.id.detail_edit_btn);
        btnDelete = view.findViewById(R.id.detail_delete_btn);

        imageView1.setOnClickListener(view14 -> {
            if (imgUri1 != null && !amount811.getText().toString().equals("Unavaliable")) {
                OpenImageDialog1();
            }
        });
        imageView2.setOnClickListener(view15 -> {
            if (imgUri2 != null && !amount58.getText().toString().equals("Unavaliable")) {
                OpenImageDialog2();
            }
        });
        imageView3.setOnClickListener(view16 -> {
            if (imgUri3 != null && !amount456.getText().toString().equals("Unavaliable")) {
                OpenImageDialog3();
            }
        });

        btnEdit.setOnClickListener(view17 -> {
            Intent intent = new Intent(getActivity(), UpdateInvestorActivity.class);
            intent.putExtra(UpdateInvestorActivity.ID_PASS, id);
            startActivity(intent);
        });
        btnDelete.setOnClickListener(view18 -> alertDialog.show());
        totalBtn.setOnClickListener(view19 -> {
            CalculateTotalProfit();
            totalProfitDiaglog.show();
        });

        Bundle data = getArguments();
        bundlePass = data.getString(InvestorDetailActivity.BUNDLE_PASS);

        return view;
    }

    private void OpenImageDialog1() {
        Glide.with(getContext()).load(imgUri1).into(zoomPic);
        imageDialog.show();
        zoomPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible) {
                    RLToolbar.setVisibility(View.VISIBLE);
                    visible = false;
                } else {
                    RLToolbar.setVisibility(View.GONE);
                    visible = true;
                }
            }
        });

        downImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                DownloadFirstImage1();
                downImg.setClickable(false);
            }
        });
    }

    private void OpenImageDialog2() {
        Glide.with(getContext()).load(imgUri2).into(zoomPic);
        imageDialog.show();
        zoomPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible) {
                    RLToolbar.setVisibility(View.VISIBLE);
                    visible = false;
                } else {
                    RLToolbar.setVisibility(View.GONE);
                    visible = true;
                }
            }
        });

        downImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                DownloadFirstImage2();
                downImg.setClickable(false);
            }
        });
    }

    private void OpenImageDialog3() {
        Glide.with(getContext()).load(imgUri3).into(zoomPic);
        imageDialog.show();
        zoomPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible) {
                    RLToolbar.setVisibility(View.VISIBLE);
                    visible = false;
                } else {
                    RLToolbar.setVisibility(View.GONE);
                    visible = true;
                }
            }
        });

        downImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                DownloadFirstImage3();
                downImg.setClickable(false);
            }
        });
    }

    private void DownloadFirstImage1() {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(imgUri1);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("1st Contract");
        request.setDescription("Download Completed");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/Ongoing/1st Contract.png");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    private void DownloadFirstImage2() {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(imgUri2);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("2nd Contract");
        request.setDescription("Download Completed");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/Ongoing/2nd Contract.png");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    private void DownloadFirstImage3() {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(imgUri3);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("3rd Contract");
        request.setDescription("Download Completed");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/Ongoing/3rd Contract.png");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    private void DeleteSubCollection() {
        FirebaseFirestore.getInstance().collection("Investors/" + id + "/First Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + id + "/First Date").document(qs.getId()).delete();
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Investors/" + id + "/Second Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + id + "/Second Date").document(qs.getId()).delete();
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Investors/" + id + "/Third Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + id + "/Third Date").document(qs.getId()).delete();
                        }
                    }
                });
    }

    private void DeleteCollection() {
        collRef.document(id).delete();
    }

    private void CalculateTotalProfit() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff1 = 0, dateDiff2 = 0, dateDiff3 = 0;
        int monthly1 = 0, monthly2 = 0, monthly3 = 0;

        Calendar c = Calendar.getInstance();
        String strcurrentDate = sdf.format(c.getTime());

        try {
            if (date811.getText().equals("Unavaliable")) {
                dateDiff1 = 0;
            } else {
                Date currentDate = sdf.parse(strcurrentDate);
                Date date = sdf.parse(date811.getText().toString());

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
                    monthly1 = (int) (intAmount811 * intPercent811 * 0.01 * 4) + (intCashBonus * 4);
                } else if (dateDiff1 < 4 && dateDiff1 > 0) {
                    monthly1 = (int) (intAmount811 * intPercent811 * 0.01 * dateDiff1) + (intCashBonus * dateDiff1);
                } else {
                    monthly1 = 0;
                }
            }

            if (date58.getText().equals("Unavaliable")) {
                dateDiff2 = 0;
            } else {
                Date currentDate = sdf.parse(strcurrentDate);
                Date date = sdf.parse(date58.getText().toString());

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

            if (date456.getText().equals("Unavaliable")) {
                dateDiff3 = 0;
            } else {
                Date currentDate = sdf.parse(strcurrentDate);
                Date date = sdf.parse(date456.getText().toString());

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

    @Override
    public void onStart() {
        super.onStart();
        RetrieveData();
        FetchingLatestData();
    }

    private void FetchingLatestData() {
        collRef.document(bundlePass).collection("First Date").orderBy("currentTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            ExpiredDate item = ds.toObject(ExpiredDate.class);
                            firstData.add(item);
                        }

                        if (firstData.size() > 0){
                            strAmount811 = firstData.get(0).getAmount();
                            strPercent811 = firstData.get(0).getPercent();
                            strDate811 = firstData.get(0).getDate();
                            ImgOne = firstData.get(0).getImageUrl();
                        }else{
                            strAmount811 = "";
                            strPercent811 = "";
                            strDate811 = "";
                            ImgOne = "";
                        }

                    }
                });

        collRef.document(bundlePass).collection("Second Date").orderBy("currentTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            ExpiredDate item = ds.toObject(ExpiredDate.class);
                            secondData.add(item);
                        }

                        if (secondData.size() > 0){
                            strAmount58 = secondData.get(0).getAmount();
                            strPercent58 = secondData.get(0).getPercent();
                            strDate58 = secondData.get(0).getDate();
                            ImgTwo = secondData.get(0).getImageUrl();
                        }else{
                            strAmount58 = "";
                            strPercent58 = "";
                            strDate58 = "";
                            ImgTwo = "";
                        }

                    }
                });

        collRef.document(bundlePass).collection("Third Date").orderBy("currentTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds : queryDocumentSnapshots) {
                            ExpiredDate item = ds.toObject(ExpiredDate.class);
                            thirdData.add(item);
                        }

                        if (thirdData.size() > 0){
                            strAmount456 = thirdData.get(0).getAmount();
                            strPercent456 = thirdData.get(0).getPercent();
                            strDate456 = thirdData.get(0).getDate();
                            ImgThree = thirdData.get(0).getImageUrl();
                        }else{
                            strAmount456 = "";
                            strPercent456 = "";
                            strDate456 = "";
                            ImgThree = "";
                        }

                    }
                });
    }

    private void RetrieveData() {
        collRef.document(bundlePass)
                .get().addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id = documentSnapshot.getId();

                TotalProfit = documentSnapshot.getString("preProfit");

                name.setText(documentSnapshot.getString("name"));
                companyID.setText(documentSnapshot.getString("companyID"));
                phone.setText(documentSnapshot.getString("phone"));
                nrc.setText(documentSnapshot.getString("nrc"));
                address.setText(documentSnapshot.getString("address"));

                if (documentSnapshot.getString("amount811").equals("0") && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                    amount811.setText("Unavaliable");
                    percent811.setText("Unavaliable");
                    date811.setText("Unavaliable");

                    intAmount811 = 0;
                    intPercent811 = 0;
                } else {
                    amount811.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount811"))) + " Ks");
                    percent811.setText(documentSnapshot.getString("percent811") + "%");
                    date811.setText(documentSnapshot.getString("date811"));

                    intAmount811 = Integer.parseInt(documentSnapshot.getString("amount811"));
                    intPercent811 = Integer.parseInt(documentSnapshot.getString("percent811"));
                }

                if (documentSnapshot.getString("amount58").equals("0") && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                    amount58.setText("Unavaliable");
                    percent58.setText("Unavaliable");
                    date58.setText("Unavaliable");

                    intAmount58 = 0;
                    intPercent58 = 0;
                } else {
                    amount58.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount58"))) + " Ks");
                    percent58.setText(documentSnapshot.getString("percent58") + "%");
                    date58.setText(documentSnapshot.getString("date58"));

                    intAmount58 = Integer.parseInt(documentSnapshot.getString("amount58"));
                    intPercent58 = Integer.parseInt(documentSnapshot.getString("percent58"));
                }

                if (documentSnapshot.getString("amount456").equals("0") && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                    amount456.setText("Unavaliable");
                    percent456.setText("Unavaliable");
                    date456.setText("Unavaliable");

                    intAmount456 = 0;
                    intPercent456 = 0;
                } else {
                    amount456.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount456"))) + " Ks");
                    percent456.setText(documentSnapshot.getString("percent456") + "%");
                    date456.setText(documentSnapshot.getString("date456"));

                    intAmount456 = Integer.parseInt(documentSnapshot.getString("amount456"));
                    intPercent456 = Integer.parseInt(documentSnapshot.getString("percent456"));
                }

                cashBonus.setText(nf.format(Integer.parseInt(documentSnapshot.getString("cashBonus"))) + " Ks");
                intCashBonus = Integer.parseInt(documentSnapshot.getString("cashBonus"));
                preProfit = Integer.parseInt(documentSnapshot.getString("preProfit"));

                if (!documentSnapshot.getString("imgUrlOne").isEmpty()) {
                    imgUri1 = Uri.parse(documentSnapshot.getString("imgUrlOne"));
                    imageView1.setBackgroundResource(android.R.color.transparent);
                    Glide.with(getContext()).load(imgUri1).into(imageView1);
                }else{
                    imageView1.setBackground(getResources().getDrawable(R.drawable.stroke_bg_white));
                    Glide.with(getContext()).clear(imageView1);
                }

                if (!documentSnapshot.getString("imgUrlTwo").isEmpty()) {
                    imgUri2 = Uri.parse(documentSnapshot.getString("imgUrlTwo"));
                    imageView2.setBackgroundResource(android.R.color.transparent);
                    Glide.with(getContext()).load(imgUri2).into(imageView2);
                }else{
                    imageView2.setBackground(getResources().getDrawable(R.drawable.stroke_bg_white));
                    Glide.with(getContext()).clear(imageView2);
                }

                if (!documentSnapshot.getString("imgUrlThree").isEmpty()) {
                    imgUri3 = Uri.parse(documentSnapshot.getString("imgUrlThree"));
                    imageView3.setBackgroundResource(android.R.color.transparent);
                    Glide.with(getContext()).load(imgUri3).into(imageView3);
                }else{
                    imageView3.setBackground(getResources().getDrawable(R.drawable.stroke_bg_white));
                    Glide.with(getContext()).clear(imageView3);
                }

                progressBar.setVisibility(View.GONE);
                NSV.setVisibility(View.VISIBLE);
            }
        });
    }


}