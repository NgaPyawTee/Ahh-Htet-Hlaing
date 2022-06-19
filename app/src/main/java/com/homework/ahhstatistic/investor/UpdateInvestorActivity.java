package com.homework.ahhstatistic.investor;

import static com.homework.ahhstatistic.R.layout.layout_alert_dialog;
import static com.homework.ahhstatistic.R.layout.layout_image_dialog;
import static com.homework.ahhstatistic.R.layout.layout_progress_dialog;
import static com.homework.ahhstatistic.R.layout.layout_recycle_bin_dialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.tasks.OnSuccessListener;
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

    private TextInputEditText name, companyID, phone, nrc, address, preProfit;
    private ImageView exImgView1, exImgView2, exImgView3, nrcImgView, zoomPic, downImg, backImg, clrImg;
    private TextView date811, date58, date456;
    private EditText amount811Cash, amount811Banking, percent811, amount58Cash, amount58Banking, percent58, amount456Cash, amount456Banking, percent456;
    private Button btnEx1st, btnOn1st, btnEx2nd, btnOn2nd, btnEx3rd, btnOn3rd, btnUpdate;
    private RelativeLayout exRL1, exRL2, exRL3;
    private ProgressBar progressBar;
    private NestedScrollView NSV;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private RelativeLayout RLToolbar;
    private CollectionReference collRef;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatePickerDialog.OnDateSetListener listener1, listener2, listener3;
    private Uri exImgUri1, exImgUri2, exImgUri3, nrcImgUri;
    int intAmount811, intPercent811, intAmount58, intPercent58, intAmount456, intPercent456;

    //Alert Dialog
    private Dialog alertDialog;
    private TextView alert_title, alert_description, alert_tv_1, alert_tv_2;

    //Image Dialog & alertDialog & ProgressDialog
    private Dialog imageDialog, progressDialog;
    private TextView tv;
    private boolean visible = true;

    //Recycle bin Dialog
    private Dialog recycleBinDialog;
    private TextView rbTv1, rbTv2, rbYes, rbNo, rbCancel;

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
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageDialog();
        AlertDialog();
        RecycleBinDialog();
        progressDialog = new Dialog(this);

        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Investors");

        name = findViewById(R.id.edit_name);
        companyID = findViewById(R.id.edit_companyID);
        phone = findViewById(R.id.edit_phone);
        nrc = findViewById(R.id.edit_nrc);
        address = findViewById(R.id.edit_address);

        amount811Cash = findViewById(R.id.edit_811_amount_cash);
        amount811Banking = findViewById(R.id.edit_811_amount_banking);
        percent811 = findViewById(R.id.edit_811_percent);
        date811 = findViewById(R.id.edit_811_date);

        amount58Cash = findViewById(R.id.edit_58_amount_cash);
        amount58Banking = findViewById(R.id.edit_58_amount_banking);
        percent58 = findViewById(R.id.edit_58_percent);
        date58 = findViewById(R.id.edit_58_date);

        amount456Cash = findViewById(R.id.edit_456_amount_cash);
        amount456Banking = findViewById(R.id.edit_456_amount_banking);
        percent456 = findViewById(R.id.edit_456_percent);
        date456 = findViewById(R.id.edit_456_date);

        preProfit = findViewById(R.id.edit_pre_profit);

        btnUpdate = findViewById(R.id.btn_update_investor);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePersonalInfo();
            }
        });

        exImgView1 = findViewById(R.id.edit_expired_img_1);
        exImgView2 = findViewById(R.id.edit_expired_img_2);
        exImgView3 = findViewById(R.id.edit_expired_img_3);
        nrcImgView = findViewById(R.id.edit_nrc_img);

        btnEx1st = findViewById(R.id.save_expired_1st);
        btnOn1st = findViewById(R.id.upload_new_1st);
        btnEx2nd = findViewById(R.id.save_expired_2nd);
        btnOn2nd = findViewById(R.id.upload_new_2nd);
        btnEx3rd = findViewById(R.id.save_expired_3rd);
        btnOn3rd = findViewById(R.id.upload_new_3rd);

        exRL1 = findViewById(R.id.edit_expired_rl_1);
        exRL2 = findViewById(R.id.edit_expired_rl_2);
        exRL3 = findViewById(R.id.edit_expired_rl_3);

        exImgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RLToolbar.setVisibility(View.INVISIBLE);
                visible = true;
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
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView1);
                                    exImgView1.setBackgroundResource(R.drawable.stroke_bg_cyan);
                                    exRL1.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2500);
                        }
                    });
                }
            }
        });
        exImgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RLToolbar.setVisibility(View.INVISIBLE);
                visible = true;
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
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView2);
                                    exImgView2.setBackgroundResource(R.drawable.stroke_bg_blue);
                                    exRL2.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2500);
                        }
                    });
                }
            }
        });
        exImgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RLToolbar.setVisibility(View.INVISIBLE);
                visible = true;
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
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView3);
                                    exImgView3.setBackgroundResource(R.drawable.stroke_bg_pale_blue);
                                    exRL3.setBackgroundResource(R.color.white);
                                    imageDialog.dismiss();
                                }
                            }, 2500);
                        }
                    });
                }
            }
        });
        nrcImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RLToolbar.setVisibility(View.INVISIBLE);
                visible = true;
                if (nrcImgUri == null) {
                    OpenFileChooser4();
                } else {
                    Glide.with(UpdateInvestorActivity.this).load(nrcImgUri).into(zoomPic);
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
                            collRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (!documentSnapshot.getString("nrcImgUrl").equals("")) {
                                        Toast.makeText(UpdateInvestorActivity.this, "Deleting image...", Toast.LENGTH_SHORT).show();
                                        StorageReference stoRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("nrcImgUrl"));
                                        stoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                collRef.document(id).update("nrcImgUrl", "").addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                nrcImgUri = null;
                                                                nrcImgView.setImageURI(null);
                                                                Glide.with(UpdateInvestorActivity.this).clear(nrcImgView);
                                                                imageDialog.dismiss();
                                                            }
                                                        }, 1000);
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(UpdateInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                nrcImgUri = null;
                                                nrcImgView.setImageURI(null);
                                                Glide.with(UpdateInvestorActivity.this).clear(nrcImgView);
                                                imageDialog.dismiss();
                                            }
                                        }, 2500);
                                    }
                                }
                            });

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
                if (intAmount811 != 0) {
                    rbTv1.setText("Delete 1st Contract");
                    rbTv2.setText("Do you want to move this contract to the recycle bin?");
                    recycleBinDialog.show();

                    rbCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recycleBinDialog.dismiss();
                        }
                    });

                    rbNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeletePermanently1();
                        }
                    });

                    rbYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            savedEx1st();
                        }
                    });
                } else {
                    Toast.makeText(UpdateInvestorActivity.this, "No contract exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEx2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount58 != 0) {
                    rbTv1.setText("Delete 2nd Contract");
                    rbTv2.setText("Do you want to move this contract to the recycle bin?");
                    recycleBinDialog.show();

                    rbCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recycleBinDialog.dismiss();
                        }
                    });

                    rbNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeletePermanently2();
                        }
                    });

                    rbYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            savedEx2nd();
                        }
                    });
                } else {
                    Toast.makeText(UpdateInvestorActivity.this, "No contract exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnEx3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount456 != 0) {
                    rbTv1.setText("Delete 3rd Contract");
                    rbTv2.setText("Do you want to move this contract to the recycle bin?");
                    recycleBinDialog.show();

                    rbCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recycleBinDialog.dismiss();
                        }
                    });

                    rbNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeletePermanently3();
                        }
                    });

                    rbYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            savedEx3rd();
                        }
                    });
                } else {
                    Toast.makeText(UpdateInvestorActivity.this, "No contract exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnOn1st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intAmount811 != 0) {
                    alert_title.setText("Warning");
                    alert_description.setText("Delete current contract first.");
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
                    alert_description.setText("Delete current contract first.");
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
                    alert_description.setText("Delete current contract first.");
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

    private void DeletePermanently1() {
        String amountCash = amount811Cash.getText().toString();
        String amountBanking = amount811Banking.getText().toString();
        String percent = percent811.getText().toString();
        String date = date811.getText().toString();

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri1 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            recycleBinDialog.dismiss();
            alertDialog.show();
        } else {
            recycleBinDialog.dismiss();
            progressDialog.setContentView(R.layout.layout_progress_dialog_2);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
            tv.setText("Deleting...");
            progressDialog.show();
            collRef.document(id).get().addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlOne"));
                    deleRef.delete().addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            collRef.document(id).update("amount811Cash", "0");
                            collRef.document(id).update("amount811Banking", "0");
                            collRef.document(id).update("percent811", "");
                            collRef.document(id).update("date811", "");
                            collRef.document(id).update("imgUrlOne", "");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateInvestorActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    btnEx1st.setText("Deleted");
                                    btnEx1st.setClickable(false);

                                    amount811Cash.setText(null);
                                    amount811Cash.setHint("Unavaliable");
                                    amount811Banking.setText(null);
                                    amount811Banking.setHint("Unavaliable");
                                    percent811.setText(null);
                                    percent811.setHint("Unavaliable");
                                    date811.setText(null);
                                    date811.setHint("Unavaliable");

                                    exImgUri1 = null;
                                    exImgView1.setBackgroundResource(R.drawable.stroke_bg_cyan);
                                    exRL1.setBackgroundResource(R.color.white);
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView1);

                                    intAmount811 = 0;
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    });
                }
            });
        }
    }

    private void DeletePermanently2() {
        String amountCash = amount58Cash.getText().toString();
        String amountBanking = amount58Banking.getText().toString();
        String percent = percent58.getText().toString();
        String date = date58.getText().toString();

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri2 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            recycleBinDialog.dismiss();
            alertDialog.show();
        } else {
            recycleBinDialog.dismiss();
            progressDialog.setContentView(R.layout.layout_progress_dialog_2);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
            tv.setText("Deleting...");
            progressDialog.show();
            collRef.document(id).get().addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlTwo"));
                    deleRef.delete().addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            collRef.document(id).update("amount58Cash", "0");
                            collRef.document(id).update("amount58Banking", "0");
                            collRef.document(id).update("percent58", "");
                            collRef.document(id).update("date58", "");
                            collRef.document(id).update("imgUrlTwo", "");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateInvestorActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    btnEx2nd.setText("Deleted");
                                    btnEx2nd.setClickable(false);

                                    amount58Cash.setText(null);
                                    amount58Cash.setHint("Unavaliable");
                                    amount58Banking.setText(null);
                                    amount58Banking.setHint("Unavaliable");
                                    percent58.setText(null);
                                    percent58.setHint("Unavaliable");
                                    date58.setText(null);
                                    date58.setHint("Unavaliable");

                                    exImgUri2 = null;
                                    exImgView2.setBackgroundResource(R.drawable.stroke_bg_blue);
                                    exRL2.setBackgroundResource(R.color.white);
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView2);

                                    intAmount58 = 0;
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    });
                }
            });
        }
    }

    private void DeletePermanently3() {
        String amountCash = amount456Cash.getText().toString();
        String amountBanking = amount456Banking.getText().toString();
        String percent = percent456.getText().toString();
        String date = date456.getText().toString();

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri3 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            recycleBinDialog.dismiss();
            alertDialog.show();
        } else {
            recycleBinDialog.dismiss();
            progressDialog.setContentView(R.layout.layout_progress_dialog_2);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
            tv.setText("Deleting...");
            progressDialog.show();
            collRef.document(id).get().addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    StorageReference deleRef = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imgUrlOne"));
                    deleRef.delete().addOnSuccessListener(UpdateInvestorActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            collRef.document(id).update("amount456Cash", "0");
                            collRef.document(id).update("amount456Banking", "0");
                            collRef.document(id).update("percent456", "");
                            collRef.document(id).update("date456", "");
                            collRef.document(id).update("imgUrlThree", "");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateInvestorActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    btnEx3rd.setText("Deleted");
                                    btnEx3rd.setClickable(false);

                                    amount456Cash.setText(null);
                                    amount456Cash.setHint("Unavaliable");
                                    amount456Banking.setText(null);
                                    amount456Banking.setHint("Unavaliable");
                                    percent456.setText(null);
                                    percent456.setHint("Unavaliable");
                                    date456.setText(null);
                                    date456.setHint("Unavaliable");

                                    exImgUri3 = null;
                                    exImgView3.setBackgroundResource(R.drawable.stroke_bg_pale_blue);
                                    exRL3.setBackgroundResource(R.color.white);
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView3);

                                    intAmount456 = 0;
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    });
                }
            });
        }
    }

    private void savedEx1st() {
        String amountCash = amount811Cash.getText().toString();
        String amountBanking = amount811Banking.getText().toString();
        String percent = percent811.getText().toString();
        String date = date811.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = String.valueOf(exImgUri1);
        String color = "Cyan";

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri1 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            recycleBinDialog.dismiss();
            alertDialog.show();
        } else {
            recycleBinDialog.dismiss();
            CalculateProfit1();

            collRef.document(id).collection("First Date").add(new ExpiredDate(amountCash, amountBanking, percent, date, currentTime, imageUrl, color))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            collRef.document(id).update("amount811Cash", "0");
                            collRef.document(id).update("amount811Banking", "0");
                            collRef.document(id).update("percent811", "");
                            collRef.document(id).update("date811", "");
                            collRef.document(id).update("imgUrlOne", "");
                            collRef.document(id).update("preProfit", preProfit.getText().toString());

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateInvestorActivity.this, "Moved to recycle bin", Toast.LENGTH_SHORT).show();
                                    btnEx1st.setText("Moved");
                                    btnEx1st.setClickable(false);

                                    amount811Cash.setText(null);
                                    amount811Cash.setHint("Unavaliable");
                                    amount811Banking.setText(null);
                                    amount811Banking.setHint("Unavaliable");
                                    percent811.setText(null);
                                    percent811.setHint("Unavaliable");
                                    date811.setText(null);
                                    date811.setHint("Unavaliable");

                                    exImgUri1 = null;
                                    exImgView1.setBackgroundResource(R.drawable.stroke_bg_cyan);
                                    exRL1.setBackgroundResource(R.color.white);
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView1);

                                    intAmount811 = 0;
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    });
        }
    }

    private void savedEx2nd() {
        String amountCash = amount58Cash.getText().toString();
        String amountBanking = amount58Banking.getText().toString();
        String percent = percent58.getText().toString();
        String date = date58.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = String.valueOf(exImgUri2);
        String color = "Blue";

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri2 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            recycleBinDialog.dismiss();
            alertDialog.show();
        } else {
            recycleBinDialog.dismiss();
            CalculateProfit2();

            collRef.document(id).collection("Second Date").add(new ExpiredDate(amountCash, amountBanking, percent, date, currentTime, imageUrl, color))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            collRef.document(id).update("amount58Cash", "0");
                            collRef.document(id).update("amount58Banking", "0");
                            collRef.document(id).update("percent58", "");
                            collRef.document(id).update("date58", "");
                            collRef.document(id).update("imgUrlTwo", "");
                            collRef.document(id).update("preProfit", preProfit.getText().toString());

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateInvestorActivity.this, "Moved to recycle bin", Toast.LENGTH_SHORT).show();
                                    btnEx2nd.setText("Moved");
                                    btnEx2nd.setClickable(false);

                                    amount58Cash.setText(null);
                                    amount58Cash.setHint("Unavaliable");
                                    amount58Banking.setText(null);
                                    amount58Banking.setHint("Unavaliable");
                                    percent58.setText(null);
                                    percent58.setHint("Unavaliable");
                                    date58.setText(null);
                                    date58.setHint("Unavaliable");

                                    exImgUri2 = null;
                                    exImgView2.setBackgroundResource(R.drawable.stroke_bg_blue);
                                    exRL2.setBackgroundResource(R.color.white);
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView2);

                                    intAmount58 = 0;
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    });
        }
    }

    private void savedEx3rd() {
        String amountCash = amount456Cash.getText().toString();
        String amountBanking = amount456Banking.getText().toString();
        String percent = percent456.getText().toString();
        String date = date456.getText().toString();
        String currentTime = String.valueOf(System.currentTimeMillis());
        String imageUrl = String.valueOf(exImgUri3);
        String color = "Pale Blue";

        if (amountCash.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri3 == null) {
            alert_title.setText("Insufficient Data");
            alert_description.setText("Please check the fields and a contract photo.");
            alert_tv_1.setVisibility(View.INVISIBLE);
            alert_tv_2.setText("OK");
            alert_tv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            recycleBinDialog.dismiss();
            alertDialog.show();
        } else {
            recycleBinDialog.dismiss();
            CalculateProfit3();

            collRef.document(id).collection("Third Date").add(new ExpiredDate(amountCash, amountBanking, percent, date, currentTime, imageUrl, color))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            collRef.document(id).update("amount456Cash", "0");
                            collRef.document(id).update("amount456Banking", "0");
                            collRef.document(id).update("percent456", "");
                            collRef.document(id).update("date456", "");
                            collRef.document(id).update("imgUrlThree", "");
                            collRef.document(id).update("preProfit", preProfit.getText().toString());

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateInvestorActivity.this, "Moved to recycle bin", Toast.LENGTH_SHORT).show();
                                    btnEx3rd.setText("Moved");
                                    btnEx3rd.setClickable(false);

                                    amount456Cash.setText(null);
                                    amount456Cash.setHint("Unavaliable");
                                    amount456Banking.setText(null);
                                    amount456Banking.setHint("Unavaliable");
                                    percent456.setText(null);
                                    percent456.setHint("Unavaliable");
                                    date456.setText(null);
                                    date456.setHint("Unavaliable");

                                    exImgUri3 = null;
                                    exImgView3.setBackgroundResource(R.drawable.stroke_bg_pale_blue);
                                    exRL3.setBackgroundResource(R.color.white);
                                    Glide.with(UpdateInvestorActivity.this).clear(exImgView3);

                                    intAmount456 = 0;
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                        }
                    });
        }
    }

    private void UploadNew1st() {
        String amountCash = amount811Cash.getText().toString();
        String amountBanking = amount811Banking.getText().toString();
        String percent = percent811.getText().toString();
        String date = date811.getText().toString();

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri1 == null) {
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
            progressDialog.setContentView(R.layout.layout_progress_dialog_3);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_3);
            tv.setText("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageReference.child(name.getText().toString() + "/First Contract/" + System.currentTimeMillis()
                    + "." + getFileExtension(exImgUri1));
            fileRef.putFile(exImgUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("amount811Cash", amount811Cash.getText().toString());
                            collRef.document(id).update("amount811Banking", amount811Banking.getText().toString());
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
        String amountCash = amount58Cash.getText().toString();
        String amountBanking = amount58Banking.getText().toString();
        String percent = percent58.getText().toString();
        String date = date58.getText().toString();

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri2 == null) {
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
            progressDialog.setContentView(R.layout.layout_progress_dialog_3);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_3);
            tv.setText("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Second Contract/" + System.currentTimeMillis()
                    + "." + getFileExtension(exImgUri2));
            fileRef.putFile(exImgUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("amount58Cash", amount58Cash.getText().toString());
                            collRef.document(id).update("amount58Banking", amount58Banking.getText().toString());
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
        String amountCash = amount456Cash.getText().toString();
        String amountBanking = amount456Banking.getText().toString();
        String percent = percent456.getText().toString();
        String date = date456.getText().toString();

        if (amountCash.isEmpty() | amountBanking.isEmpty() | percent.isEmpty() | date.isEmpty() | exImgUri3 == null) {
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
            progressDialog.setContentView(R.layout.layout_progress_dialog_3);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            tv = progressDialog.findViewById(R.id.progress_dialog_tv_3);
            tv.setText("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference fileRef = storageReference.child(name.getText().toString() + "/Third Contract/" + System.currentTimeMillis()
                    + "." + getFileExtension(exImgUri3));
            fileRef.putFile(exImgUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            collRef.document(id).update("amount456Cash", amount456Cash.getText().toString());
                            collRef.document(id).update("amount456Banking", amount456Banking.getText().toString());
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
        tv.setText("Moving...");
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

            if (currentYear == year) {
                dateDiff = c.get(Calendar.MONTH) - d.getMonth();
            } else {
                Period period = new Period(date1, currentLong, PeriodType.yearMonthDay());
                dateDiff = period.getMonths();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateDiff >= 4) {
            int monthly = (int) (intAmount811 * intPercent811 * 0.01 * 4);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        } else if (dateDiff < 4 && dateDiff > 0) {
            int monthly = (int) (intAmount811 * intPercent811 * 0.01 * dateDiff);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        } else {
            int monthly = 0;
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }

        progressDialog.setCancelable(false);
    }

    private void CalculateProfit2() {
        progressDialog.setContentView(R.layout.layout_progress_dialog_2);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
        tv.setText("Moving...");
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

            if (currentYear == year) {
                dateDiff = c.get(Calendar.MONTH) - d.getMonth();
            } else {
                Period period = new Period(date1, currentLong, PeriodType.yearMonthDay());
                dateDiff = period.getMonths();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateDiff >= 4) {
            int monthly = (int) (intAmount58 * intPercent58 * 0.01 * 4);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        } else if (dateDiff < 4 && dateDiff > 0) {
            int monthly = (int) (intAmount58 * intPercent58 * 0.01 * dateDiff);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        } else {
            int monthly = 0;
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        }

        progressDialog.setCancelable(false);
    }

    private void CalculateProfit3() {
        progressDialog.setContentView(R.layout.layout_progress_dialog_2);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = progressDialog.findViewById(R.id.progress_dialog_tv_2);
        tv.setText("Moving...");
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

            if (currentYear == year) {
                dateDiff = c.get(Calendar.MONTH) - d.getMonth();
            } else {
                Period period = new Period(date1, currentLong, PeriodType.yearMonthDay());
                dateDiff = period.getMonths();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateDiff >= 4) {
            int monthly = (int) (intAmount456 * intPercent456 * 0.01 * 4);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        } else if (dateDiff < 4 && dateDiff > 0) {
            int monthly = (int) (intAmount456 * intPercent456 * 0.01 * dateDiff);
            int profit = Integer.parseInt(preProfit.getText().toString()) + monthly;
            preProfit.setText(String.valueOf(profit));
        } else {
            int monthly = 0;
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
        String strPreprofit = preProfit.getText().toString();

        if (strName.isEmpty() | strCompanyID.isEmpty() | strPhone.isEmpty() | strNRC.isEmpty() | strAddress.isEmpty() | strPreprofit.isEmpty()) {
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

            if (nrcImgUri != null) {
                StorageReference fileRef = storageReference.child(strName + "/NRC"
                        + "." + getFileExtension(nrcImgUri));
                fileRef.putFile(nrcImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                collRef.document(id).update("nrcImgUrl", uri.toString());
                            }
                        });
                    }
                });
            }

            collRef.document(id).update("name", name.getText().toString());
            collRef.document(id).update("companyID", companyID.getText().toString());
            collRef.document(id).update("phone", phone.getText().toString());
            collRef.document(id).update("nrc", nrc.getText().toString());
            collRef.document(id).update("address", address.getText().toString());
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

    private void RecycleBinDialog() {
        recycleBinDialog = new Dialog(this);
        recycleBinDialog.setContentView(layout_recycle_bin_dialog);
        recycleBinDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recycleBinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rbTv1 = recycleBinDialog.findViewById(R.id.rb_dialog_title);
        rbTv2 = recycleBinDialog.findViewById(R.id.rb_dialog_description);
        rbYes = recycleBinDialog.findViewById(R.id.rb_dialog_yes);
        rbNo = recycleBinDialog.findViewById(R.id.rb_dialog_no);
        rbCancel = recycleBinDialog.findViewById(R.id.rb_dialog_cancel);
    }

    private void ImageDialog() {
        imageDialog = new Dialog(this, R.style.FullScreenDialog);
        View v = getLayoutInflater().inflate(layout_image_dialog,null);
        imageDialog.setContentView(v);
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

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, listener1, year, month, day);
        dialog.show();
    }

    private void OpenDatePicker2() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, listener2, year, month, day);
        dialog.show();
    }

    private void OpenDatePicker3() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, listener3, year, month, day);
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
            nrcImgUri = data.getData();
            nrcImgView.setBackgroundResource(android.R.color.transparent);
            nrcImgView.setImageURI(nrcImgUri);
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

                if (!documentSnapshot.getString("nrcImgUrl").equals("")) {
                    nrcImgUri = Uri.parse(documentSnapshot.getString("nrcImgUrl"));
                    Glide.with(UpdateInvestorActivity.this).load(nrcImgUri).into(nrcImgView);
                }

                if (documentSnapshot.getString("amount811Cash").equals("0") && documentSnapshot.getString("amount811Banking").equals("0")
                        && documentSnapshot.getString("percent811").equals("") && documentSnapshot.getString("date811").equals("")) {
                    amount811Cash.setHint("Unavaliable");
                    amount811Banking.setHint("Unavaliable");
                    percent811.setHint("Unavaliable");
                    date811.setHint("Unavaliable");

                    intAmount811 = 0;
                    intPercent811 = 0;
                } else {
                    amount811Cash.setText(documentSnapshot.getString("amount811Cash"));
                    amount811Banking.setText(documentSnapshot.getString("amount811Banking"));
                    percent811.setText(documentSnapshot.getString("percent811"));
                    date811.setText(documentSnapshot.getString("date811"));

                    intAmount811 = Integer.parseInt(documentSnapshot.getString("amount811Cash")) + Integer.parseInt(documentSnapshot.getString("amount811Banking"));
                    intPercent811 = Integer.parseInt(documentSnapshot.getString("percent811"));

                    exImgUri1 = Uri.parse(documentSnapshot.getString("imgUrlOne"));
                    Glide.with(UpdateInvestorActivity.this).load(exImgUri1).into(exImgView1);
                    exImgView1.setBackgroundResource(android.R.color.transparent);
                    exRL1.setBackgroundResource(android.R.color.transparent);
                }

                if (documentSnapshot.getString("amount58Cash").equals("0") && documentSnapshot.getString("amount58Banking").equals("0")
                        && documentSnapshot.getString("percent58").equals("") && documentSnapshot.getString("date58").equals("")) {
                    amount58Cash.setHint("Unavaliable");
                    amount58Banking.setHint("Unavaliable");
                    percent58.setHint("Unavaliable");
                    date58.setHint("Unavaliable");

                    intAmount58 = 0;
                    intPercent58 = 0;
                } else {
                    amount58Cash.setText(documentSnapshot.getString("amount58Cash"));
                    amount58Banking.setText(documentSnapshot.getString("amount58Banking"));
                    percent58.setText(documentSnapshot.getString("percent58"));
                    date58.setText(documentSnapshot.getString("date58"));

                    intAmount58 = Integer.parseInt(documentSnapshot.getString("amount58Cash")) + Integer.parseInt(documentSnapshot.getString("amount58Banking"));
                    intPercent58 = Integer.parseInt(documentSnapshot.getString("percent58"));

                    exImgUri2 = Uri.parse(documentSnapshot.getString("imgUrlTwo"));
                    Glide.with(UpdateInvestorActivity.this).load(exImgUri2).into(exImgView2);
                    exImgView2.setBackgroundResource(android.R.color.transparent);
                    exRL2.setBackgroundResource(android.R.color.transparent);
                }

                if (documentSnapshot.getString("amount456Cash").equals("0") && documentSnapshot.getString("amount456Banking").equals("0")
                        && documentSnapshot.getString("percent456").equals("") && documentSnapshot.getString("date456").equals("")) {
                    amount456Cash.setHint("Unavaliable");
                    amount456Banking.setHint("Unavaliable");
                    percent456.setHint("Unavaliable");
                    date456.setHint("Unavaliable");

                    intAmount456 = 0;
                    intPercent456 = 0;
                } else {
                    amount456Cash.setText(documentSnapshot.getString("amount456Cash"));
                    amount456Banking.setText(documentSnapshot.getString("amount456Banking"));
                    percent456.setText(documentSnapshot.getString("percent456"));
                    date456.setText(documentSnapshot.getString("date456"));

                    intAmount456 = Integer.parseInt(documentSnapshot.getString("amount456Cash")) + Integer.parseInt(documentSnapshot.getString("amount456Banking"));
                    intPercent456 = Integer.parseInt(documentSnapshot.getString("percent456"));

                    exImgUri3 = Uri.parse(documentSnapshot.getString("imgUrlThree"));
                    Glide.with(UpdateInvestorActivity.this).load(exImgUri3).into(exImgView3);
                    exImgView3.setBackgroundResource(android.R.color.transparent);
                    exRL3.setBackgroundResource(android.R.color.transparent);
                }

                preProfit.setText(documentSnapshot.getString("preProfit"));

                progressBar.setVisibility(View.GONE);
                NSV.setVisibility(View.VISIBLE);
            }
        });
    }
}