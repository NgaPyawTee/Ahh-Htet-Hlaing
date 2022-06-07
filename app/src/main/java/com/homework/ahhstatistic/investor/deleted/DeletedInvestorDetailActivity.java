package com.homework.ahhstatistic.investor.deleted;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homework.ahhstatistic.R;

import java.text.NumberFormat;

public class DeletedInvestorDetailActivity extends AppCompatActivity {
    public static final String ID_PASS = "ID_PASS";
    public static String id;

    private RelativeLayout RLToolbar, rl;
    private NestedScrollView NSV;

    private TextView name, companyID, phone, nrc, address,
            amount811, percent811, date811,
            amount58, percent58, date58,
            amount456, percent456, date456,
            totalProfit;
    private ImageView imageView1, imageView2, imageView3, zoomPic, downImg, backImg, clrImg;
    private Uri imgUri1, imgUri2, imgUri3;
    private ProgressBar progressBar;
    private CollectionReference collRef;
    public Dialog imageDialog;
    boolean visible = true;

    private NumberFormat nf = NumberFormat.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_investor_detail);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));

        Intent intent = getIntent();
        id = intent.getStringExtra(ID_PASS);

        collRef = FirebaseFirestore.getInstance().collection("Deleted Investors");
        progressBar = findViewById(R.id.deleted_detail_progress_bar);
        NSV = findViewById(R.id.deleted_detail_container);

        rl = findViewById(R.id.deleted_detail_rl);

        //Image Dialog
        imageDialog = new Dialog(this);
        imageDialog.setContentView(R.layout.layout_image_dialog);
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageDialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        zoomPic = imageDialog.findViewById(R.id.zoom_img);
        RLToolbar = imageDialog.findViewById(R.id.dialog_toolbar);
        clrImg = imageDialog.findViewById(R.id.clear_img);
        clrImg.setVisibility(View.INVISIBLE);
        downImg = imageDialog.findViewById(R.id.down_img);
        downImg.setVisibility(View.INVISIBLE);
        backImg = imageDialog.findViewById(R.id.back_img);
        backImg.setOnClickListener(view12 -> imageDialog.dismiss());

        name = findViewById(R.id.deleted_detail_name);
        companyID = findViewById(R.id.deleted_detail_company_id);
        phone = findViewById(R.id.deleted_detail_phone);
        nrc = findViewById(R.id.deleted_detail_nrc);
        address = findViewById(R.id.deleted_detail_address);

        imageView1 = findViewById(R.id.deleted_detail_img_one);
        imageView2 = findViewById(R.id.deleted_detail_img_two);
        imageView3 = findViewById(R.id.deleted_detail_img_three);

        amount811 = findViewById(R.id.deleted_detail_plan_811_amount);
        percent811 = findViewById(R.id.deleted_detail_plan_811_percent);
        date811 = findViewById(R.id.deleted_detail_date_811);

        amount58 = findViewById(R.id.deleted_detail_plan_58_amount);
        percent58 = findViewById(R.id.deleted_detail_plan_58_percent);
        date58 = findViewById(R.id.deleted_detail_date_58);

        amount456 = findViewById(R.id.deleted_detail_plan_456_amount);
        percent456 = findViewById(R.id.deleted_detail_plan_456_percent);
        date456 = findViewById(R.id.deleted_detail_date_456);

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

        totalProfit = findViewById(R.id.deleted_detail_total_profit);
    }

    private void OpenImageDialog1() {
        Glide.with(this).load(imgUri1).into(zoomPic);
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

    }

    private void OpenImageDialog2() {
        Glide.with(this).load(imgUri2).into(zoomPic);
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

    }

    private void OpenImageDialog3() {
        Glide.with(this).load(imgUri3).into(zoomPic);
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.document(id).get().addOnSuccessListener(this, documentSnapshot -> {
            name.setText(documentSnapshot.getString("name"));
            companyID.setText(documentSnapshot.getString("companyID"));
            phone.setText(documentSnapshot.getString("phone"));
            nrc.setText(documentSnapshot.getString("nrc"));
            address.setText(documentSnapshot.getString("address"));

            if (documentSnapshot.getString("amount811").equals("0") && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                amount811.setText("Unavaliable");
                percent811.setText("Unavaliable");
                date811.setText("Unavaliable");
            } else {
                amount811.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount811"))) + " Ks");
                percent811.setText(documentSnapshot.getString("percent811") + "%");
                date811.setText(documentSnapshot.getString("date811"));
            }

            if (documentSnapshot.getString("amount58").equals("0") && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                amount58.setText("Unavaliable");
                percent58.setText("Unavaliable");
                date58.setText("Unavaliable");
            } else {
                amount58.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount58"))) + " Ks");
                percent58.setText(documentSnapshot.getString("percent58") + "%");
                date58.setText(documentSnapshot.getString("date58"));
            }

            if (documentSnapshot.getString("amount456").equals("0") && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                amount456.setText("Unavaliable");
                percent456.setText("Unavaliable");
                date456.setText("Unavaliable");
            } else {
                amount456.setText(nf.format(Integer.parseInt(documentSnapshot.getString("amount456"))) + " Ks");
                percent456.setText(documentSnapshot.getString("percent456") + "%");
                date456.setText(documentSnapshot.getString("date456"));
            }

            if (!documentSnapshot.getString("imgUrlOne").isEmpty()) {
                imgUri1 = Uri.parse(documentSnapshot.getString("imgUrlOne"));
                imageView1.setBackgroundResource(android.R.color.transparent);
                Glide.with(this).load(imgUri1).into(imageView1);
            }

            if (!documentSnapshot.getString("imgUrlTwo").isEmpty()) {
                imgUri2 = Uri.parse(documentSnapshot.getString("imgUrlTwo"));
                imageView2.setBackgroundResource(android.R.color.transparent);
                Glide.with(this).load(imgUri2).into(imageView2);
            }

            if (!documentSnapshot.getString("imgUrlThree").isEmpty()) {
                imgUri3 = Uri.parse(documentSnapshot.getString("imgUrlThree"));
                imageView3.setBackgroundResource(android.R.color.transparent);
                Glide.with(this).load(imgUri3).into(imageView3);
            }

            totalProfit.setText(nf.format(Integer.parseInt(documentSnapshot.getString("preProfit"))) + " Ks");

            progressBar.setVisibility(View.GONE);
            NSV.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
        });
    }
}