package com.homework.ahhstatistic.investor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import static com.homework.ahhstatistic.R.layout.layout_alert_dialog;
import static com.homework.ahhstatistic.R.layout.layout_image_dialog;
import static com.homework.ahhstatistic.R.layout.layout_progress_dialog;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import java.util.Calendar;

public class AddInvestorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText name, companyID, phone, nrc, address, amount811, percent811,
            amount58, percent58, amount456, percent456;
    private TextView date811, date58, date456;
    private Uri imgUri1, imgUri2, imgUri3;
    private ImageView imgView1, imgView2, imgView3;
    private Button btnAdd;
    private DatePickerDialog.OnDateSetListener listener1, listener2, listener3;
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    //Progress Dialog && Alert Dialog
    private Dialog progressDialog, alertDialog;
    private TextView tv, alert_title, alert_description, alert_tv_1, alert_tv_2;

    private String cashBonus = "0";
    private String preProfit = "0";
    private String dailyProfit = "0";

    private String strImgOne = "";
    private String strImgTwo = "";
    private String strImgThree = "";

    //Image dialog
    private Dialog dialog;
    private RelativeLayout RLToolbar;
    private ImageView backImg, downImg, clrImg;
    private PhotoView zoomPic;
    private boolean visible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_add);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Alert Dialog
        alertDialog = new Dialog(this);
        alertDialog.setContentView(layout_alert_dialog);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alert_title = alertDialog.findViewById(R.id.alert_dialog_title);
        alert_title.setText("Insufficient Data");
        alert_description = alertDialog.findViewById(R.id.alert_dialog_description);
        alert_description.setText("Require personal information. Please check the fields again.\n\nContracts can be updated later.");
        alert_tv_1 = alertDialog.findViewById(R.id.alert_dialog_tv_1);
        alert_tv_1.setVisibility(View.INVISIBLE);
        alert_tv_2 = alertDialog.findViewById(R.id.alert_dialog_tv_2);
        alert_tv_2.setText("BACK");
        alert_tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        //Progress Dialog
        progressDialog = new Dialog(this);
        progressDialog.setContentView(layout_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = progressDialog.findViewById(R.id.progress_dialog_tv);
        tv.setText("Adding...");

        //Image Dialog
        dialog = new Dialog(this);
        dialog.setContentView(layout_image_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        zoomPic = dialog.findViewById(R.id.zoom_img);
        RLToolbar = dialog.findViewById(R.id.dialog_toolbar);
        clrImg = dialog.findViewById(R.id.clear_img);
        downImg = dialog.findViewById(R.id.down_img);
        downImg.setVisibility(View.INVISIBLE);
        backImg = dialog.findViewById(R.id.back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        name = findViewById(R.id.add_name);
        companyID = findViewById(R.id.add_companyID);
        phone = findViewById(R.id.add_phone);
        nrc = findViewById(R.id.add_nrc);
        address = findViewById(R.id.add_address);
        amount811 = findViewById(R.id.add_811_amount);
        percent811 = findViewById(R.id.add_811_percent);
        date811 = findViewById(R.id.add_811_date);
        amount58 = findViewById(R.id.add_58_amount);
        percent58 = findViewById(R.id.add_58_percent);
        date58 = findViewById(R.id.add_58_date);
        amount456 = findViewById(R.id.add_456_amount);
        percent456 = findViewById(R.id.add_456_percent);
        date456 = findViewById(R.id.add_456_date);
        imgView1 = findViewById(R.id.add_img_1);
        imgView2 = findViewById(R.id.add_img_2);
        imgView3 = findViewById(R.id.add_img_3);
        btnAdd = findViewById(R.id.btn_add_investor);

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

        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri1 == null) {
                    OpenFileChooser1();
                } else {
                    Glide.with(AddInvestorActivity.this).load(imgUri1).into(zoomPic);
                    dialog.show();

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
                            Toast.makeText(AddInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgUri1 = null;
                                    imgView1.setImageURI(null);
                                    imgView1.setBackgroundResource(R.drawable.stroke_bg_cyan);
                                    dialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri2 == null) {
                    OpenFileChooser2();
                } else {
                    Glide.with(AddInvestorActivity.this).load(imgUri2).into(zoomPic);
                    dialog.show();

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
                            Toast.makeText(AddInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgUri2 = null;
                                    imgView2.setImageURI(null);
                                    imgView2.setBackgroundResource(R.drawable.stroke_bg_blue);
                                    dialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });
        imgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri3 == null) {
                    OpenFileChooser3();
                } else {
                    Glide.with(AddInvestorActivity.this).load(imgUri3).into(zoomPic);
                    dialog.show();

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
                            Toast.makeText(AddInvestorActivity.this, "Removing image...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgUri3 = null;
                                    imgView3.setImageURI(null);
                                    imgView3.setBackgroundResource(R.drawable.stroke_bg_pale_blue);
                                    dialog.dismiss();
                                }
                            }, 2000);
                        }
                    });
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddInvestor();
            }
        });
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgView1.setBackgroundResource(android.R.color.transparent);
            imgUri1 = data.getData();
            imgView1.setImageURI(imgUri1);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgView2.setBackgroundResource(android.R.color.transparent);
            imgUri2 = data.getData();
            imgView2.setImageURI(imgUri2);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgView3.setBackgroundResource(android.R.color.transparent);
            imgUri3 = data.getData();
            imgView3.setImageURI(imgUri3);
        }
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

    private void AddInvestor() {
        String strName = name.getText().toString();
        String strCompanyID = companyID.getText().toString();
        String strPhone = phone.getText().toString();
        String strNRC = nrc.getText().toString();
        String strAddress = address.getText().toString();

        String str811amount = "";
        String str811percent = "";
        String str811date = "";

        String str58amount = "";
        String str58percent = "";
        String str58date = "";

        String str456amount = "";
        String str456percent = "";
        String str456date = "";

        if (strName.isEmpty() | strCompanyID.isEmpty() | strPhone.isEmpty() | strNRC.isEmpty() | strAddress.isEmpty()) {
            alertDialog.show();
        } else {
            progressDialog.show();
            progressDialog.setCancelable(false);
            collRef.add(new Investor(strName, strCompanyID, strPhone, strNRC, strAddress,
                    str811amount, str811percent, str811date,
                    str58amount, str58percent, str58date,
                    str456amount, str456percent, str456date,
                    cashBonus, dailyProfit, strImgOne, strImgTwo, strImgThree, preProfit))
                    .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if (imgUri1 != null) {
                                StorageReference fileRef = storageReference.child(strName + "/First Contract/" + System.currentTimeMillis()
                                        + "." + getFileExtension(imgUri1));
                                fileRef.putFile(imgUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(documentReference.getId()).update("imgUrlOne", uri.toString());
                                                collRef.document(documentReference.getId()).update("amount811", amount811.getText().toString());
                                                collRef.document(documentReference.getId()).update("percent811", percent811.getText().toString());
                                                collRef.document(documentReference.getId()).update("date811", date811.getText().toString());
                                            }
                                        });
                                    }
                                });
                            }

                            if (imgUri2 != null) {
                                StorageReference fileRef = storageReference.child(strName + "/Second Contract/" + System.currentTimeMillis()
                                        + "." + getFileExtension(imgUri2));
                                fileRef.putFile(imgUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(documentReference.getId()).update("imgUrlTwo", uri.toString());
                                                collRef.document(documentReference.getId()).update("amount58", amount58.getText().toString());
                                                collRef.document(documentReference.getId()).update("percent58", percent58.getText().toString());
                                                collRef.document(documentReference.getId()).update("date58", date58.getText().toString());
                                            }
                                        });
                                    }
                                });
                            }

                            if (imgUri3 != null) {
                                StorageReference fileRef = storageReference.child(strName + "/Third Contract/" + System.currentTimeMillis()
                                        + "." + getFileExtension(imgUri3));
                                fileRef.putFile(imgUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                collRef.document(documentReference.getId()).update("imgUrlThree", uri.toString());
                                                collRef.document(documentReference.getId()).update("amount456", amount456.getText().toString());
                                                collRef.document(documentReference.getId()).update("percent456", percent456.getText().toString());
                                                collRef.document(documentReference.getId()).update("date456", date456.getText().toString());
                                            }
                                        });
                                    }
                                });
                            }

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddInvestorActivity.this, "Investor added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, 8000);
                        }
                    });
        }
    }
}