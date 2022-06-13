package com.homework.ahhstatistic.investor.detail;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homework.ahhstatistic.R;

import java.text.NumberFormat;

public class HomeFragment extends Fragment {
    private String bundlePass;
    private RelativeLayout RLToolbar;
    private NestedScrollView NSV;

    private LinearLayout showNRCImg;
    private TextView name, companyID, phone, nrc, address,
            amount811Cash, amount811Banking,  percent811, date811,
            amount58Cash, amount58Banking, percent58, date58,
            amount456Cash, amount456Banking, percent456, date456;
    private ImageView zoomPic, downImg, backImg, clrImg;
    private Uri imgUri1, imgUri2, imgUri3, nrcImgUri;
    private ProgressBar progressBar;
    private CollectionReference collRef;
    public Dialog imageDialog;
    boolean visible = true;
    private RelativeLayout imageView1, imageView2, imageView3;

    private NumberFormat nf = NumberFormat.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        collRef = FirebaseFirestore.getInstance().collection("Investors");
        progressBar = view.findViewById(R.id.detail_progress_bar);
        NSV = view.findViewById(R.id.detail_container);

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

        name = view.findViewById(R.id.detail_name);
        companyID = view.findViewById(R.id.detail_company_id);
        phone = view.findViewById(R.id.detail_phone);
        nrc = view.findViewById(R.id.detail_nrc);
        address = view.findViewById(R.id.detail_address);

        imageView1 = view.findViewById(R.id.detail_img_one);
        imageView2 = view.findViewById(R.id.detail_img_two);
        imageView3 = view.findViewById(R.id.detail_img_three);
        showNRCImg = view.findViewById(R.id.show_detail_nrc);

        amount811Cash = view.findViewById(R.id.detail_plan_811_amount_cash);
        amount811Banking = view.findViewById(R.id.detail_plan_811_amount_banking);
        percent811 = view.findViewById(R.id.detail_plan_811_percent);
        date811 = view.findViewById(R.id.detail_date_811);

        amount58Cash = view.findViewById(R.id.detail_plan_58_amount_cash);
        amount58Banking = view.findViewById(R.id.detail_plan_58_amount_banking);
        percent58 = view.findViewById(R.id.detail_plan_58_percent);
        date58 = view.findViewById(R.id.detail_date_58);

        amount456Cash = view.findViewById(R.id.detail_plan_456_amount_cash);
        amount456Banking = view.findViewById(R.id.detail_plan_456_amount_banking);
        percent456 = view.findViewById(R.id.detail_plan_456_percent);
        date456 = view.findViewById(R.id.detail_date_456);

        imageView1.setOnClickListener(view14 -> {
            if (imgUri1 != null && !amount811Cash.getText().toString().equals("Unavaliable")) {
                OpenImageDialog1();
            }
        });
        imageView2.setOnClickListener(view15 -> {
            if (imgUri2 != null && !amount58Cash.getText().toString().equals("Unavaliable")) {
                OpenImageDialog2();
            }
        });
        imageView3.setOnClickListener(view16 -> {
            if (imgUri3 != null && !amount456Cash.getText().toString().equals("Unavaliable")) {
                OpenImageDialog3();
            }
        });
        showNRCImg.setOnClickListener(view1 -> {
            collRef.document(bundlePass).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.getString("nrcImgUrl").equals("")){
                        OpenImageDialog4();
                    }
                }
            });
        });

        Bundle data = getArguments();
        bundlePass = data.getString(InvestorDetailActivity.BUNDLE_PASS);

        return view;
    }

    private void OpenImageDialog1() {
        Glide.with(getContext()).load(imgUri1).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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
        Glide.with(getContext()).load(imgUri2).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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
        Glide.with(getContext()).load(imgUri3).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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

    private void OpenImageDialog4() {
        Glide.with(getContext()).load(nrcImgUri).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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
                DownloadFirstImage4();
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

    private void DownloadFirstImage4() {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(nrcImgUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle(name.getText().toString() + " NRC");
        request.setDescription("Download Completed");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Invescog/" + name.getText().toString() + ".png");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
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
                name.setText(documentSnapshot.getString("name"));
                companyID.setText(documentSnapshot.getString("companyID"));
                phone.setText(documentSnapshot.getString("phone"));
                nrc.setText(documentSnapshot.getString("nrc"));
                address.setText(documentSnapshot.getString("address"));

                if (documentSnapshot.getString("amount811Cash").equals("0") && documentSnapshot.getString("amount811Banking").equals("0")
                        && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                    amount811Cash.setText("Unavaliable");
                    amount811Banking.setText("Unavaliable");
                    percent811.setText("Unavaliable");
                    date811.setText("Unavaliable");

                } else {
                    amount811Cash.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount811Cash"))) + " Ks");
                    amount811Banking.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount811Banking"))) + " Ks");
                    percent811.setText(documentSnapshot.getString("percent811") + "%");
                    date811.setText(documentSnapshot.getString("date811"));
                }

                if (documentSnapshot.getString("amount58Cash").equals("0") && documentSnapshot.getString("amount58Banking").equals("0")
                        && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                    amount58Cash.setText("Unavaliable");
                    amount58Banking.setText("Unavaliable");
                    percent58.setText("Unavaliable");
                    date58.setText("Unavaliable");
                } else {
                    amount58Cash.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount58Cash"))) + " Ks");
                    amount58Banking.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount58Banking"))) + " Ks");
                    percent58.setText(documentSnapshot.getString("percent58") + "%");
                    date58.setText(documentSnapshot.getString("date58"));
                }

                if (documentSnapshot.getString("amount456Cash").equals("0") && documentSnapshot.getString("amount456Banking").equals("0")
                        && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                    amount456Cash.setText("Unavaliable");
                    amount456Banking.setText("Unavaliable");
                    percent456.setText("Unavaliable");
                    date456.setText("Unavaliable");
                } else {
                    amount456Cash.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount456Cash"))) + " Ks");
                    amount456Banking.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount456Banking"))) + " Ks");
                    percent456.setText(documentSnapshot.getString("percent456") + "%");
                    date456.setText(documentSnapshot.getString("date456"));
                }


                if (!documentSnapshot.getString("imgUrlOne").isEmpty()) {
                    imgUri1 = Uri.parse(documentSnapshot.getString("imgUrlOne"));
                }

                if (!documentSnapshot.getString("imgUrlTwo").isEmpty()) {
                    imgUri2 = Uri.parse(documentSnapshot.getString("imgUrlTwo"));
                }

                if (!documentSnapshot.getString("imgUrlThree").isEmpty()) {
                    imgUri3 = Uri.parse(documentSnapshot.getString("imgUrlThree"));
                }

                if (!documentSnapshot.getString("nrcImgUrl").isEmpty()) {
                    nrcImgUri = Uri.parse(documentSnapshot.getString("nrcImgUrl"));
                }

                progressBar.setVisibility(View.GONE);
                NSV.setVisibility(View.VISIBLE);
            }
        });
    }
}