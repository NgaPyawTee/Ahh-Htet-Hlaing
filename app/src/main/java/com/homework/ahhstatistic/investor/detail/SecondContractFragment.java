package com.homework.ahhstatistic.investor.detail;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.ExpiredDate;

import java.util.ArrayList;
import java.util.List;

public class SecondContractFragment extends Fragment {
    private String bundlePass;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ContractAdapter adapter;
    private List<ExpiredDate> list = new ArrayList<>();
    private List<String> imageUrlList = new ArrayList<>();
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");

    private Dialog imgDialog;
    private ImageView imgBack, imgDown, imgClear, pic;
    private RelativeLayout RLToolbar;
    private Uri UriDown;
    private boolean visible = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contract_detail, container, false);

        Bundle bundle = getArguments();
        bundlePass = bundle.getString(InvestorDetailActivity.BUNDLE_PASS);

        imgDialog = new Dialog(getContext());
        imgDialog.setContentView(R.layout.layout_image_dialog);
        imgDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgDialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        RLToolbar = imgDialog.findViewById(R.id.dialog_toolbar);
        imgBack = imgDialog.findViewById(R.id.back_img);
        imgClear = imgDialog.findViewById(R.id.clear_img);
        imgClear.setVisibility(View.INVISIBLE);
        imgDown = imgDialog.findViewById(R.id.down_img);
        pic = imgDialog.findViewById(R.id.zoom_img);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgDialog.dismiss();
            }
        });

        progressBar = v.findViewById(R.id.contract_progress_bar);
        recyclerView = v.findViewById(R.id.contract_recycler_view);
        adapter = new ContractAdapter(getActivity(), list);

        adapter.setListener(new ContractAdapter.Listener() {
            @Override
            public void onClick(int position) {
                UriDown = Uri.parse(imageUrlList.get(position));
                if (UriDown != null) {
                    Glide.with(getContext()).load(UriDown).into(pic);
                    imgDialog.show();

                    pic.setOnClickListener(new View.OnClickListener() {
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

                    imgDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();
                            DownloadImage();
                            imgDown.setClickable(false);
                        }
                    });
                }
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void DownloadImage() {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(UriDown);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("Expired 1st Contract");
        request.setDescription("Download Completed");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Invescog/Expired/2nd/expired 2nd.png");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
    }

    @Override
    public void onStart() {
        super.onStart();
        RetrieveContractOneData();
    }

    private void RetrieveContractOneData() {
        collRef.document(bundlePass)
                .collection("Second Date").orderBy("currentTime", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                list.clear();
                imageUrlList.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    ExpiredDate item = ds.toObject(ExpiredDate.class);
                    list.add(item);
                    imageUrlList.add(item.getImageUrl());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}