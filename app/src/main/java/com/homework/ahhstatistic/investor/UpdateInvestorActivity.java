package com.homework.ahhstatistic.investor;

import static com.homework.ahhstatistic.R.layout.layout_alert_dialog;
import static com.homework.ahhstatistic.R.layout.layout_progress_dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.ExpiredDate;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateInvestorActivity extends AppCompatActivity {
    public static final String ID_PASS = "IntentPass";
    String intentPass, id;

    private TextInputEditText name, companyID, phone, nrc, address, cashBonus, preProfit;
    private ImageView exImgView1, exImgView2, exImgView3, OnImgView1, OnImgView2, OnImgView3,
            zoomPic, downImg, backImg, clrImg;
    private TextView date811, date58, date456;
    private EditText amount811, percent811, amount58, percent58, amount456, percent456;
    private Button btnEx1st, btnOn1st, btnEx2nd, btnOn2nd, btnEx3rd, btnOn3rd, btnUpdate;
    private RelativeLayout exRL1, onRL1, exRL2, onRL2, exRL3, onRL3;
    private ProgressBar progressBar;
    private NestedScrollView NSV;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private RelativeLayout RLToolbar;
    private CollectionReference collRef;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatePickerDialog.OnDateSetListener listener1, listener2, listener3;
    private Uri exImgUri1, onImgUri1, exImgUri2, onImgUri2, exImgUri3, onImgUri3;
    int intAmount811, intPercent811, intCashBonus, intAmount58, intPercent58, intAmount456, intPercent456;

    //Alert Dialog
    private Dialog alertDialog;
    private TextView alert_title, alert_description, alert_tv_1, alert_tv_2;

    //Image Dialog & alertDialog & ProgressDialog
    public Dialog imageDialog, progressDialog;
    private TextView tv;
    private boolean visible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_update);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        Intent intent = getIntent();
        intentPass = intent.getStringExtra(ID_PASS);

        NSV = findViewById(R.id.update_container);
        progressBar = findViewById(R.id.update_progress_bar);

        toolbar = findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update an investor");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageDialog();
        AlertDialog();
        progressDialog = new Dialog(this);

        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Investors");

        name = findViewById(R.id.edit_name);
        companyID = findViewById(R.id.edit_companyID);
        phone = findViewById(R.id.edit_phone);
        nrc = findViewById(R.id.edit_nrc);
        address = findViewById(R.id.edit_address);

        amount811 = findViewById(R.id.edit_811_amount);
        percent811 = findViewById(R.id.edit_811_percent);
        date811 = findViewById(R.id.edit_811_date);

        amount58 = findViewById(R.id.edit_58_amount);
        percent58 = findViewById(R.id.edit_58_percent);
        date58 = findViewById(R.id.edit_58_date);

        cashBonus = findViewById(R.id.edit_cash_bonus);
        preProfit = findViewById(R.id.edit_pre_profit);

        btnUpdate = findViewById(R.id.btn_update_investor);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePersonalInfo();
            }
        });

        amount456 = findViewById(R.id.edit_456_amount);
        percent456 = findViewById(R.id.edit_456_percent);
        date456 = findViewById(R.id.edit_456_date);

        exImgView1 = findViewById(R.id.edit_expired_img_1);
        exImgView2 = findViewById(R.id.edit_expired_img_2);
        exImgView3 = findViewById(R.id.edit_expired_img_3);
        OnImgView1 = findViewById(R.id.edit_new_img_1);
        OnImgView2 = findViewById(R.id.edit_new_img_2);
        OnImgView3 = findViewById(R.id.edit_new_img_3);

        btnEx1st = findViewById(R.id.save_expired_1st);
        btnOn1st = findViewById(R.id.upload_new_1st);
        btnEx2nd = findViewById(R.id.save_expired_2nd);
        btnOn2nd = findViewById(R.id.upload_new_2nd);
        btnEx3rd = findViewById(R.id.save_expired_3rd);
        btnOn3rd = findViewById(R.id.upload_new_3rd);

        exRL1 = findViewById(R.id.edit_expired_rl_1);
        exRL2 = findViewById(R.id.edit_expired_rl_2);
        exRL3 = findViewById(R.id.edit_expired_rl_3);
        onRL1 = findViewById(R.id.edit_new_rl_1);
        onRL2 = findViewById(R.id.edit_new_rl_2);
        onRL3 = findViewById(R.id.edit_new_rl_3);

        exImgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exImgUri1 == null) {
                    OpenFileChooser1();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(exImgUri1).into(zoomPic);
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

                    clrImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    exImgUri1 = null;
                                    exImgView1.setImageURI(null);
                                    exImgView1.setBackgroundResource(R.drawable.stroke_bg_cyan);
                                    exRL1.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        exImgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exImgUri2 == null) {
                    OpenFileChooser2();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(exImgUri2).into(zoomPic);
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

                    clrImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    exImgUri2 = null;
                                    exImgView2.setImageURI(null);
                                    exImgView2.setBackgroundResource(R.drawable.stroke_bg_blue);
                                    exRL2.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        exImgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exImgUri3 == null) {
                    OpenFileChooser3();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(exImgUri3).into(zoomPic);
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

                    clrImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    exImgUri3 = null;
                                    exImgView3.setImageURI(null);
                                    exImgView3.setBackgroundResource(R.drawable.stroke_bg_pale_blue);
                                    exRL3.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        OnImgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onImgUri1 == null) {
                    OpenFileChooser4();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(onImgUri1).into(zoomPic);
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

                    clrImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onImgUri1 = null;
                                    OnImgView1.setImageURI(null);
                                    OnImgView1.setBackgroundResource(R.drawable.stroke_bg_cyan);
                                    onRL1.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        OnImgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onImgUri2 == null) {
                    OpenFileChooser5();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(onImgUri2).into(zoomPic);
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

                    clrImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onImgUri2 = null;
                                    OnImgView2.setImageURI(null);
                                    OnImgView2.setBackgroundResource(R.drawable.stroke_bg_blue);
                                    onRL2.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        OnImgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onImgUri3 == null) {
                    OpenFileChooser6();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(onImgUri3).into(zoomPic);
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

                    clrImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onImgUri3 = null;
                                    OnImgView3.setImageURI(null);
                                    OnImgView3.setBackgroundResource(R.drawable.stroke_bg_pale_blue);
                                    onRL3.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });

        date811.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDatePicker1();
            }
        });
        date58.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDatePicker2();
            }
        });
        date456.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDatePicker3();
            }
        });
        listener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String s = day + "/" + month + "/" + year;
                date811.setText(s);
            }
        };
        listener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String s = day + "/" + month + "/" + year;
                date58.setText(s);
            }
        };
        listener3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String s = day + "/" + month + "/" + year;
                date456.setText(s);
            }
        };

        btnEx1st = findViewById(R.id.save_expired_1st);
        btnEx2nd = findViewById(R.id.save_expired_2nd);
        btnEx3rd = findViewById(R.id.save_expired_3rd);
        btnOn1st = findViewById(R.id.upload_new_1st);
        btnOn2nd = findViewById(R.id.upload_new_2nd);
        btnOn3rd = findViewById(R.id.upload_new_3rd);

        btnEx1st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount811 != 0){
                    savedEx1st();
                }else{
                    Toast.makeText(UpdateInvestorActivity.this, "No data to save as expired", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEx2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount58 != 0){
                    savedEx2nd();
                }else{
                    Toast.makeText(UpdateInvestorActivity.this, "No data to save as expired", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEx3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount456 != 0){
                    savedEx3rd();
                }else{
                    Toast.makeText(UpdateInvestorActivity.this, "No data to save as expired", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnOn1st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount811 != 0) {
                    alert_title.setText("Warning");
                    alert_description.setText("Please save this contract as an expired one first.");
                    alert_tv_1.setVisibility(View.INVISIBLE);
                    alert_tv_2.setText("OK");
                    alert_tv_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    UploadNew1st();
                }
            }
        });
        btnOn2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount58 != 0) {
                    alert_title.setText("Warning");
                    alert_description.setText("Please save this contract as an expired one first.");
                    alert_tv_1.setVisibility(View.INVISIBLE);
                    alert_tv_2.setText("OK");
                    alert_tv_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    UploadNew2nd();
                }
            }
        });
        btnOn3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount456 != 0) {
                    alert_title.setText("Warning");
                    alert_description.setText("Please save this contract as an expired one first.");
                    alert_tv_1.setVisibility(View.INVISIBLE);
                    alert_tv_2.setText("OK");
                    alert_tv_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    UploadNew3rd();
                }
            }
        });

    }

    private void savedEx1st() {
        String amount = amount811.getText().toString();
        String percent = percent811.getText().toString();
        String date = date811.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = "";

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri1 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and add a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            CalculateProfit1();

            collRef.document(id).collection("First Date").add(new ExpiredDate(amount, percent, date, currentTime, imageUrl))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Expired Date/First Date/"
                                    + System.currentTimeMillis() + "." + getFileExtension(exImgUri1));
                            fileRef.putFile(exImgUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlOne"));
                                                    deleRef.delete();
                                                    collRef.document(id).update("amount811", "0");
                                                    collRef.document(id).update("percent811", "");
                                                    collRef.document(id).update("date811", "");
                                                    collRef.document(id).update("imgUrlOne", "");
                                                    collRef.document(id).update("preProfit", preProfit.getText().toString());
                                                    collRef.document(id).collection("First Date").document(documentReference.getId())
                                                            .update("imageUrl", uri.toString());

                                                    Toast.makeText(UpdateInvestorActivity.this, "Saved pre-date", Toast.LENGTH_SHORT).show();
                                                    btnEx1st.setText("Saved");
                                                    btnEx1st.setClickable(false);

                                                    amount811.setText(null);
                                                    amount811.setHint("Unavaliable");
                                                    percent811.setText(null);
                                                    percent811.setHint("Unavaliable");
                                                    date811.setText(null);
                                                    date811.setHint("Unavaliable");
                                                    progressDialog.dismiss();

                                                    intAmount811 = 0;
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
    }
    private void savedEx2nd() {
        String amount = amount58.getText().toString();
        String percent = percent58.getText().toString();
        String date = date58.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = "";

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri2 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and add a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            CalculateProfit2();

            collRef.document(id).collection("Second Date").add(new ExpiredDate(amount, percent, date, currentTime, imageUrl))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Expired Date/Second Date/"
                                    + System.currentTimeMillis() + "." + getFileExtension(exImgUri2));
                            fileRef.putFile(exImgUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlTwo"));
                                                    deleRef.delete();
                                                    collRef.document(id).update("amount58", "0");
                                                    collRef.document(id).update("percent58", "");
                                                    collRef.document(id).update("date58", "");
                                                    collRef.document(id).update("imgUrlTwo", "");
                                                    collRef.document(id).update("preProfit", preProfit.getText().toString());
                                                    collRef.document(id).collection("Second Date").document(documentReference.getId())
                                                            .update("imageUrl", uri.toString());

                                                    Toast.makeText(UpdateInvestorActivity.this, "Saved pre-date", Toast.LENGTH_SHORT).show();
                                                    btnEx2nd.setText("Saved");
                                                    btnEx2nd.setClickable(false);

                                                    amount58.setText(null);
                                                    amount58.setHint("Unavaliable");
                                                    percent58.setText(null);
                                                    percent58.setHint("Unavaliable");
                                                    date58.setText(null);
                                                    date58.setHint("Unavaliable");
                                                    progressDialog.dismiss();

                                                    intAmount58 = 0;
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
    }
    private void savedEx3rd() {
        String amount = amount456.getText().toString();
        String percent = percent456.getText().toString();
        String date = date456.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = "";

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri3 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and add a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            CalculateProfit3();

            collRef.document(id).collection("Third Date").add(new ExpiredDate(amount, percent, date, currentTime, imageUrl))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Expired Date/Third Date/"
                                    + System.currentTimeMillis() + "." + getFileExtension(exImgUri3));
                            fileRef.putFile(exImgUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlThree"));
                                                    deleRef.delete();
                                                    collRef.document(id).update("amount456", "0");
                                                    collRef.document(id).update("percent456", "");
                                                    collRef.document(id).update("date456", "");
                                                    collRef.document(id).update("imgUrlThree", "");
                                                    collRef.document(id).update("preProfit", preProfit.getText().toString());
                                                    collRef.document(id).collection("Third Date").document(documentReference.getId())
                                                            .update("imageUrl", uri.toString());

                                                    Toast.makeText(UpdateInvestorActivity.this, "Saved pre-date", Toast.LENGTH_SHORT).show();
                                                    btnEx3rd.setText("Saved");
                                                    btnEx3rd.setClickable(false);

                                                    amount456.setText(null);
                                                    amount456.setHint("Unavaliable");
                                                    percent456.setText(null);
                                                    percent456.setHint("Unavaliable");
                                                    date456.setText(null);
                                                    date456.setHint("Unavaliable");
                                                    progressDialog.dismiss();

                                                    intAmount456 = 0;
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
    }
    private void UploadNew1st() {
        String amount = amount811.getText().toString();
        String percent = percent811.getText().toString();
        String date = date811.getText().toString();

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty() | onImgUri1 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and add a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }else{
            progressDialog.setContentView(R.layout.layout_progress_dialog_3);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_3);
            tv.setText("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Ongoing Date/" + "First Contract"
                    + "." + getFileExtension(onImgUri1));
            fileRef.putFile(onImgUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("amount811", amount811.getText().toString());
                            collRef.document(id).update("percent811", percent811.getText().toString());
                            collRef.document(id).update("date811", date811.getText().toString());
                            collRef.document(id).update("imgUrlOne", uri.toString());

                            Toast.makeText(UpdateInvestorActivity.this, "Saved new contract", Toast.LENGTH_SHORT).show();
                            btnOn1st.setText("Saved");
                            btnOn1st.setClickable(false);
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }
    private void UploadNew2nd() {
        String amount = amount58.getText().toString();
        String percent = percent58.getText().toString();
        String date = date58.getText().toString();

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty() | onImgUri2 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and add a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }else{
            progressDialog.setContentView(R.layout.layout_progress_dialog_3);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_3);
            tv.setText("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Ongoing Date/" + "Second Contract"
                    + "." + getFileExtension(onImgUri2));
            fileRef.putFile(onImgUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("amount58", amount58.getText().toString());
                            collRef.document(id).update("percent58", percent58.getText().toString());
                            collRef.document(id).update("date58", date58.getText().toString());
                            collRef.document(id).update("imgUrlTwo", uri.toString());

                            Toast.makeText(UpdateInvestorActivity.this, "Saved new contract", Toast.LENGTH_SHORT).show();
                            btnOn2nd.setText("Saved");
                            btnOn2nd.setClickable(false);
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }
    private void UploadNew3rd() {
        String amount = amount456.getText().toString();
        String percent = percent456.getText().toString();
        String date = date456.getText().toString();

        if (amount.isEmpty() | percent.isEmpty() | date.isEmpty() | onImgUri3 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and add a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }else{
            progressDialog.setContentView(R.layout.layout_progress_dialog_3);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_3);
            tv.setText("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Ongoing Date/" + "Third Contract"
                    + "." + getFileExtension(onImgUri3));
            fileRef.putFile(onImgUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("amount456", amount456.getText().toString());
                            collRef.document(id).update("percent456", percent456.getText().toString());
                            collRef.document(id).update("date456", date456.getText().toString());
                            collRef.document(id).update("imgUrlThree", uri.toString());

                            Toast.makeText(UpdateInvestorActivity.this, "Saved new contract", Toast.LENGTH_SHORT).show();
                            btnOn3rd.setText("Saved");
                            btnOn3rd.setClickable(false);
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    private void CalculateProfit1() {
        progressDialog.setContentView(R.layout.layout_progress_dialog_2);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
        tv.setText("Saving...");
        progressDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff = 0;

        try {
            Calendar c = Calendar.getInstance();
            String strCurrentDate = sdf.format(c.getTime());
            Date currentDate = sdf.parse(strCurrentDate);

            long currentLong = currentDate.getTime();
            long date1 = sdf.parse(date811.getText().toString()).getTime();

            int currentYear = currentDate.getYear();
            Date d = sdf.parse(date811.getText().toString());
            int year = d.getYear();

            if (currentYear == year){
                dateDiff = c.get(Calendar.MONTH) - d.getMonth();
            }else{
                Period period = new Period(date1, currentLong, PeriodType.yearMonthDay());
                dateDiff = period.getMonths();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateDiff >= 4){
            int monthly = (int) (intAmount811 * intPercent811 * 0.01 * 4) + (intCashBonus * 4);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }else{
            int monthly = (int) (intAmount811 * intPercent811 * 0.01 * dateDiff) + (intCashBonus * dateDiff);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }

        progressDialog.setCancelable(false);
    }
    private void CalculateProfit2() {
        progressDialog.setContentView(R.layout.layout_progress_dialog_2);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
        tv.setText("Saving...");
        progressDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff = 0;

        try {
            Calendar c = Calendar.getInstance();
            String strCurrentDate = sdf.format(c.getTime());
            Date currentDate = sdf.parse(strCurrentDate);

            long currentLong = currentDate.getTime();
            long date1 = sdf.parse(date58.getText().toString()).getTime();

            int currentYear = currentDate.getYear();
            Date d = sdf.parse(date58.getText().toString());
            int year = d.getYear();

            if (currentYear == year){
                dateDiff = c.get(Calendar.MONTH) - d.getMonth();
            }else{
                Period period = new Period(date1, currentLong, PeriodType.yearMonthDay());
                dateDiff = period.getMonths();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateDiff >= 4){
            int monthly = (int) (intAmount58 * intPercent58 * 0.01 * 4);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }else{
            int monthly = (int) (intAmount58 * intPercent58 * 0.01 * dateDiff);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }

        progressDialog.setCancelable(false);
    }
    private void CalculateProfit3() {
        progressDialog.setContentView(R.layout.layout_progress_dialog_2);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
        tv.setText("Saving...");
        progressDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int dateDiff = 0;

        try {
            Calendar c = Calendar.getInstance();
            String strCurrentDate = sdf.format(c.getTime());
            Date currentDate = sdf.parse(strCurrentDate);

            long currentLong = currentDate.getTime();
            long date1 = sdf.parse(date456.getText().toString()).getTime();

            int currentYear = currentDate.getYear();
            Date d = sdf.parse(date456.getText().toString());
            int year = d.getYear();

            if (currentYear == year){
                dateDiff = c.get(Calendar.MONTH) - d.getMonth();
            }else{
                Period period = new Period(date1, currentLong, PeriodType.yearMonthDay());
                dateDiff = period.getMonths();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateDiff >= 4){
            int monthly = (int) (intAmount456 * intPercent456 * 0.01 * 4);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }else{
            int monthly = (int) (intAmount456 * intPercent456 * 0.01 * dateDiff);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }


        progressDialog.setCancelable(false);
    }

    private void UpdatePersonalInfo() {
        String strName = name.getText().toString();
        String strCompanyID = companyID.getText().toString();
        String strPhone = phone.getText().toString();
        String strNRC = nrc.getText().toString();
        String strAddress = address.getText().toString();
        String strCashBonus = cashBonus.getText().toString();
        String strPreprofit = preProfit.getText().toString();

        if (strName.isEmpty() | strCompanyID.isEmpty() | strPhone.isEmpty() | strNRC.isEmpty() | strAddress.isEmpty() | strCashBonus.isEmpty() | strPreprofit.isEmpty()) {
            alertDialog.show();
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields again.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        } else {
            progressDialog.setContentView(layout_progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv);
            tv.setText("Updating...");
            progressDialog.show();

            collRef.document(id).update("name", name.getText().toString());
            collRef.document(id).update("companyID", companyID.getText().toString());
            collRef.document(id).update("phone", phone.getText().toString());
            collRef.document(id).update("nrc", nrc.getText().toString());
            collRef.document(id).update("address", address.getText().toString());
            collRef.document(id).update("cashBonus", cashBonus.getText().toString());
            collRef.document(id).update("preProfit", preProfit.getText().toString());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateInvestorActivity.this, "Personal information updated", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    }

    private void AlertDialog() {
        alertDialog = new Dialog(this);
        alertDialog.setContentView(layout_alert_dialog);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alert_title = alertDialog.findViewById(R.id.alert_dialog_title);
        alert_description = alertDialog.findViewById(R.id.alert_dialog_description);
        alert_tv_1 = alertDialog.findViewById(R.id.alert_dialog_tv_1);
        alert_tv_2 = alertDialog.findViewById(R.id.alert_dialog_tv_2);
    }
    private void ImageDialog() {
        imageDialog = new Dialog(this);
        imageDialog.setContentView(R.layout.layout_image_dialog);
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageDialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        zoomPic = imageDialog.findViewById(R.id.zoom_img);
        RLToolbar = imageDialog.findViewById(R.id.dialog_toolbar);
        clrImg = imageDialog.findViewById(R.id.clear_img);
        downImg = imageDialog.findViewById(R.id.down_img);
        downImg.setVisibility(View.INVISIBLE);
        backImg = imageDialog.findViewById(R.id.back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog.dismiss();
            }
        });
    }

    private void OpenDatePicker1() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, listener1, year, month, day);
        dialog.show();
    }
    private void OpenDatePicker2() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, listener2, year, month, day);
        dialog.show();
    }
    private void OpenDatePicker3() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, listener3, year, month, day);
        dialog.show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    ;


    private void OpenFileChooser1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private void OpenFileChooser2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }
    private void OpenFileChooser3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 3);
    }
    private void OpenFileChooser4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 4);
    }
    private void OpenFileChooser5() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }
    private void OpenFileChooser6() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 6);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            exImgUri1 = data.getData();
            exImgView1.setBackgroundResource(android.R.color.transparent);
            exImgView1.setImageURI(exImgUri1);
            exRL1.setBackgroundResource(android.R.color.transparent);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            exImgUri2 = data.getData();
            exImgView2.setBackgroundResource(android.R.color.transparent);
            exImgView2.setImageURI(exImgUri2);
            exRL2.setBackgroundResource(android.R.color.transparent);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            exImgUri3 = data.getData();
            exImgView3.setBackgroundResource(android.R.color.transparent);
            exImgView3.setImageURI(exImgUri3);
            exRL3.setBackgroundResource(android.R.color.transparent);
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            onImgUri1 = data.getData();
            OnImgView1.setBackgroundResource(android.R.color.transparent);
            OnImgView1.setImageURI(onImgUri1);
            onRL1.setBackgroundResource(android.R.color.transparent);
        } else if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            onImgUri2 = data.getData();
            OnImgView2.setBackgroundResource(android.R.color.transparent);
            OnImgView2.setImageURI(onImgUri2);
            onRL2.setBackgroundResource(android.R.color.transparent);
        } else if (requestCode == 6 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            onImgUri3 = data.getData();
            OnImgView3.setBackgroundResource(android.R.color.transparent);
            OnImgView3.setImageURI(onImgUri3);
            onRL3.setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.document(intentPass)
                .get().addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id = documentSnapshot.getId();

                name.setText(documentSnapshot.getString("name"));
                companyID.setText(documentSnapshot.getString("companyID"));
                phone.setText(documentSnapshot.getString("phone"));
                nrc.setText(documentSnapshot.getString("nrc"));
                address.setText(documentSnapshot.getString("address"));

                if (documentSnapshot.getString("amount811").equals("0") && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                    amount811.setHint("Unavaliable");
                    percent811.setHint("Unavaliable");
                    date811.setHint("Unavaliable");

                    intAmount811 = 0;
                    intPercent811 = 0;
                } else {
                    amount811.setText(documentSnapshot.getString("amount811"));
                    percent811.setText(documentSnapshot.getString("percent811"));
                    date811.setText(documentSnapshot.getString("date811"));

                    intAmount811 = Integer.parseInt(documentSnapshot.getString("amount811"));
                    intPercent811 = Integer.parseInt(documentSnapshot.getString("percent811"));
                }

                if (documentSnapshot.getString("amount58").equals("0") && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                    amount58.setHint("Unavaliable");
                    percent58.setHint("Unavaliable");
                    date58.setHint("Unavaliable");

                    intAmount58 = 0;
                    intPercent58 = 0;
                } else {
                    amount58.setText(documentSnapshot.getString("amount58"));
                    percent58.setText(documentSnapshot.getString("percent58"));
                    date58.setText(documentSnapshot.getString("date58"));

                    intAmount58 = Integer.parseInt(documentSnapshot.getString("amount58"));
                    intPercent58 = Integer.parseInt(documentSnapshot.getString("percent58"));
                }

                if (documentSnapshot.getString("amount456").equals("0") && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                    amount456.setHint("Unavaliable");
                    percent456.setHint("Unavaliable");
                    date456.setHint("Unavaliable");

                    intAmount456 = 0;
                    intPercent456 = 0;
                } else {
                    amount456.setText(documentSnapshot.getString("amount456"));
                    percent456.setText(documentSnapshot.getString("percent456"));
                    date456.setText(documentSnapshot.getString("date456"));

                    intAmount456 = Integer.parseInt(documentSnapshot.getString("amount456"));
                    intPercent456 = Integer.parseInt(documentSnapshot.getString("percent456"));
                }

                cashBonus.setText(documentSnapshot.getString("cashBonus"));
                intCashBonus = Integer.parseInt(documentSnapshot.getString("cashBonus"));
                preProfit.setText(documentSnapshot.getString("preProfit"));

                progressBar.setVisibility(View.GONE);
                NSV.setVisibility(View.VISIBLE);
            }
        });
    }

}