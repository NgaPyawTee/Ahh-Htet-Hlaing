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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.investor.InvestorUpdateActivity;

import java.text.NumberFormat;

public class HomeFragment extends Fragment {
    private String bundlePass;
    private RelativeLayout RLToolbar;
    private NestedScrollView NSV;

    private TextView name, companyID, phone, nrc, address,
            amount811, percent811, date811, signOne,
            amount58, percent58, date58, signTwo,
            amount456, percent456, date456, signThree,
            cashBonus;
    private String preProfit;
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
    private TextView totalProfit;
    private Button btnTotalProfit;

    //Alert Dialog
    private Dialog alertDialog;
    private TextView alert_title, alert_description, alert_tv_1, alert_tv_2;

    private NumberFormat nf = NumberFormat.getInstance();

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
        alert_description.setText("Are you sure you want to remove this person?");
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
        alert_tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert_title.setText("Removing...");
                alert_description.setText("Please wait a few seconds");
                alert_tv_1.setText("");
                alert_tv_2.setText("");
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
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog.dismiss();
            }
        });

        //Total Profit Dialog
        totalProfitDiaglog = new Dialog(getContext());
        totalProfitDiaglog.setContentView(R.layout.layout_total_profit_dialog);
        totalProfitDiaglog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        totalProfit = totalProfitDiaglog.findViewById(R.id.total_profit);
        btnTotalProfit = totalProfitDiaglog.findViewById(R.id.btn_total_profit);
        btnTotalProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalProfitDiaglog.dismiss();
            }
        });

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
        signOne = view.findViewById(R.id.text_percent_one);

        amount58 = view.findViewById(R.id.detail_plan_58_amount);
        percent58 = view.findViewById(R.id.detail_plan_58_percent);
        date58 = view.findViewById(R.id.detail_date_58);
        signTwo = view.findViewById(R.id.text_percent_two);

        amount456 = view.findViewById(R.id.detail_plan_456_amount);
        percent456 = view.findViewById(R.id.detail_plan_456_percent);
        date456 = view.findViewById(R.id.detail_date_456);
        signThree = view.findViewById(R.id.text_percent_three);

        cashBonus = view.findViewById(R.id.cash_bonus);

        totalBtn = view.findViewById(R.id.detail_total_btn);
        btnEdit = view.findViewById(R.id.detail_edit_btn);
        btnDelete = view.findViewById(R.id.detail_delete_btn);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri1 != null) {
                    OpenImageDialog1();
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri2 != null) {
                    OpenImageDialog2();
                }
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri3 != null) {
                    OpenImageDialog3();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InvestorUpdateActivity.class);
                intent.putExtra(InvestorUpdateActivity.ID_PASS, id);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        totalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalProfitDiaglog.show();
                int i = 50000;
                totalProfit.setText(nf.format(i) + " Ks");
            }
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/1st Contract.png");
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/2nd Contract.png");
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/3rd Contract.png");
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

        FirebaseFirestore.getInstance().collection("Investors/" + id + "/Final Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()) {
                            FirebaseFirestore.getInstance().collection("Investors/" + id + "/Final Date").document(qs.getId()).delete();
                        }
                    }
                });
    }

    private void DeleteCollection() {
        collRef.document(id).delete();
    }

    @Override
    public void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.document(bundlePass)
                .get().addOnSuccessListener(getActivity(), new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                NSV.setVisibility(View.VISIBLE);

                id = documentSnapshot.getId();

                name.setText(documentSnapshot.getString("name"));
                companyID.setText(documentSnapshot.getString("companyID"));
                phone.setText(documentSnapshot.getString("phone"));
                nrc.setText(documentSnapshot.getString("nrc"));
                address.setText(documentSnapshot.getString("address"));

                if (documentSnapshot.getString("amount811").equals("0") && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                    amount811.setText("Unavaliable");
                    percent811.setText("Unavaliable");
                    signOne.setVisibility(View.INVISIBLE);
                    date811.setText("Unavaliable");
                } else {
                    amount811.setText(nf.format(Integer.valueOf(documentSnapshot.getString("amount811"))));
                    percent811.setText(documentSnapshot.getString("percent811"));
                    date811.setText(documentSnapshot.getString("date811"));
                }

                if (documentSnapshot.getString("amount58").equals("0") && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                    amount58.setText("Unavaliable");
                    percent58.setText("Unavaliable");
                    signTwo.setVisibility(View.INVISIBLE);
                    date58.setText("Unavaliable");
                } else {
                    amount58.setText(nf.format(Integer.valueOf(documentSnapshot.getString("amount58"))));
                    percent58.setText(documentSnapshot.getString("percent58"));
                    date58.setText(documentSnapshot.getString("date58"));
                }

                if (documentSnapshot.getString("amount456").equals("0") && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                    amount456.setText("Unavaliable");
                    percent456.setText("Unavaliable");
                    signThree.setVisibility(View.INVISIBLE);
                    date456.setText("Unavaliable");
                } else {
                    amount456.setText(nf.format(Integer.valueOf(documentSnapshot.getString("amount456"))));
                    percent456.setText(documentSnapshot.getString("percent456"));
                    date456.setText(documentSnapshot.getString("date456"));
                }

                cashBonus.setText(documentSnapshot.getString("cashAmount"));

                if (!documentSnapshot.getString("imgUrlOne").isEmpty()) {
                    imgUri1 = Uri.parse(documentSnapshot.getString("imgUrlOne"));
                    imageView1.setBackgroundResource(android.R.color.transparent);
                    Glide.with(getContext()).load(imgUri1).into(imageView1);
                }

                if (!documentSnapshot.getString("imgUrlTwo").isEmpty()) {
                    imgUri2 = Uri.parse(documentSnapshot.getString("imgUrlTwo"));
                    imageView2.setBackgroundResource(android.R.color.transparent);
                    Glide.with(getContext()).load(imgUri2).into(imageView2);
                }

                if (!documentSnapshot.getString("imgUrlThree").isEmpty()) {
                    imgUri3 = Uri.parse(documentSnapshot.getString("imgUrlThree"));
                    imageView3.setBackgroundResource(android.R.color.transparent);
                    Glide.with(getContext()).load(imgUri3).into(imageView3);
                }
            }
        });
    }
}