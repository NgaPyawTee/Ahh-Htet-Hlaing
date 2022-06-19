package com.homework.ahhstatistic.investor.deleted;

import static com.homework.ahhstatistic.R.layout.layout_image_dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homework.ahhstatistic.R;

import java.text.NumberFormat;

public class DeletedInvestorDetailActivity extends AppCompatActivity {
    public static final String ID_PASS = "ID_PASS";
    public static String id;

    private RelativeLayout RLToolbar;
    private LinearLayout ll;
    private ImageView rl;
    private NestedScrollView NSV;
    private Toolbar toolbar;

    private TextView name, companyID, phone, nrc, address,
            amount811Cash, amount811Banking, percent811, date811,
            amount58Cash, amount58Banking, percent58, date58,
            amount456Cash, amount456Banking,  percent456, date456,
            totalProfit;
    private ImageView imageView1, imageView2, imageView3, zoomPic, downImg, backImg, clrImg;
    private Uri imgUri1, imgUri2, imgUri3, nrcImgUri;
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

        toolbar = findViewById(R.id.deleted_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra(ID_PASS);

        collRef = FirebaseFirestore.getInstance().collection("Deleted Investors");
        progressBar = findViewById(R.id.deleted_detail_progress_bar);
        NSV = findViewById(R.id.deleted_detail_container);

        rl = findViewById(R.id.deleted_detail_rl);

        //Image Dialog
        imageDialog = new Dialog(this, R.style.FullScreenDialog);
        View v = getLayoutInflater().inflate(layout_image_dialog,null);
        imageDialog.setContentView(v);
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

        amount811Cash = findViewById(R.id.deleted_investor_amount_811_cash);
        amount811Banking = findViewById(R.id.deleted_investor_amount_811_banking);
        percent811 = findViewById(R.id.deleted_detail_plan_811_percent);
        date811 = findViewById(R.id.deleted_detail_date_811);

        amount58Cash = findViewById(R.id.deleted_investor_amount_58_cash);
        amount58Banking = findViewById(R.id.deleted_investor_amount_58_banking);
        percent58 = findViewById(R.id.deleted_detail_plan_58_percent);
        date58 = findViewById(R.id.deleted_detail_date_58);

        amount456Cash = findViewById(R.id.deleted_investor_amount_456_cash);
        amount456Banking = findViewById(R.id.deleted_investor_amount_456_banking);
        percent456 = findViewById(R.id.deleted_detail_plan_456_percent);
        date456 = findViewById(R.id.deleted_detail_date_456);

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

        totalProfit = findViewById(R.id.deleted_detail_total_profit);

        ll = findViewById(R.id.show_deleted_nrc);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collRef.document(id).get().addOnSuccessListener(DeletedInvestorDetailActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (nrcImgUri != null){
                            OpenImageDialog4();
                        }
                    }
                });
            }
        });
    }

    private void OpenImageDialog1() {
        RLToolbar.setVisibility(View.INVISIBLE);
        visible = true;
        Glide.with(this).load(imgUri1).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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
        RLToolbar.setVisibility(View.INVISIBLE);
        visible = true;
        Glide.with(this).load(imgUri2).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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
        RLToolbar.setVisibility(View.INVISIBLE);
        visible = true;
        Glide.with(this).load(imgUri3).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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

    private void OpenImageDialog4() {
        RLToolbar.setVisibility(View.INVISIBLE);
        visible = true;
        Glide.with(this).load(nrcImgUri).placeholder(R.drawable.rotate_progress).diskCacheStrategy(DiskCacheStrategy.NONE).into(zoomPic);
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

            if (documentSnapshot.getString("amount811Cash").equals("0") && documentSnapshot.getString("amount811Banking").equals("0") &&
                    documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
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

            if (documentSnapshot.getString("amount58Cash").equals("0") && documentSnapshot.getString("amount58Banking").equals("0") &&
                    documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
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

            if (documentSnapshot.getString("amount456Cash").equals("0") && documentSnapshot.getString("amount456Banking").equals("0") &&
                    documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
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

            if (!documentSnapshot.getString("nrcImgUrl").isEmpty()){
                nrcImgUri = Uri.parse(documentSnapshot.getString("nrcImgUrl"));
            }

            totalProfit.setText(nf.format(Integer.parseInt(documentSnapshot.getString("preProfit"))) + " Ks");

            progressBar.setVisibility(View.GONE);
            NSV.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
        });
    }
}