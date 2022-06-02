package com.homework.ahhstatistic.Income;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Income;

import java.util.ArrayList;
import java.util.List;

public class MyIncomeCategoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private IncomeRecyclerAdapter adapter;
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Earnings");
    private List<Income> list;
    private List<String> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income_category);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat windowInsetsControllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        toolbar = findViewById(R.id.income_category_toolbar);
        toolbar.setTitle("My Earnings");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        idList = new ArrayList<>();

        adapter = new IncomeRecyclerAdapter(this,list);
        adapter.setListener(position -> {
            Intent intent = new Intent(MyIncomeCategoryActivity.this, MyIncomeDetailActivity.class);
            intent.putExtra(MyIncomeDetailActivity.ID_PASS,idList.get(position));
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.income_category_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.income_category_progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.orderBy("millisTime", Query.Direction.DESCENDING).get().addOnSuccessListener(this, queryDocumentSnapshots -> {
            progressBar.setVisibility(View.GONE);
            list.clear();

            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                Income item = ds.toObject(Income.class);
                list.add(item);
                item.setId(ds.getId());
                idList.add(item.getId());
            }
            adapter.notifyDataSetChanged();
        });
    }
}