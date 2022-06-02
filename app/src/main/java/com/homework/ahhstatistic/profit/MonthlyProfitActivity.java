package com.homework.ahhstatistic.profit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.ahhstatistic.R;
import com.homework.ahhstatistic.model.Investor;

import java.util.ArrayList;
import java.util.List;

public class MonthlyProfitActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProfitRecyclerAdapter adapter;
    private FirebaseFirestore db;
    private CollectionReference collRef;
    private List<Investor> list;
    private List<String> nameList;
    private List<String> idList;
    private ProgressBar progressBar;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_profit);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat controllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        controllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        controllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        toolbar = findViewById(R.id.monthly_profit_toolbar);
        toolbar.setTitle("Monthly Profit");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_normal_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Investors");

        progressBar = findViewById(R.id.monthly_profit_progress_bar);

        list = new ArrayList<>();
        nameList = new ArrayList<>();
        idList = new ArrayList<>();

        recyclerView = findViewById(R.id.monthly_profit_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProfitRecyclerAdapter(this, list);
        adapter.setListener(new ProfitRecyclerAdapter.Listener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(MonthlyProfitActivity.this, ProfitDetailActivity.class);
                intent.putExtra(ProfitDetailActivity.ID_PASS, idList.get(pos));

                String position = String.valueOf(pos+1);
                intent.putExtra(ProfitDetailActivity.POS_PASS, position);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profit_menu_item, menu);

        MenuItem item = menu.findItem(R.id.action_search_2);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.black);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setThreshold(1);

        searchAutoComplete.setAdapter(arrayAdapter);

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                searchAutoComplete.setText(s);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (nameList.contains(query)) {
                    int pos = nameList.indexOf(query);
                    String position = String.valueOf(pos+1);

                    Intent intent = new Intent(MonthlyProfitActivity.this, ProfitDetailActivity.class);
                    intent.putExtra(ProfitDetailActivity.ID_PASS, idList.get(pos));
                    intent.putExtra(ProfitDetailActivity.POS_PASS,position);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.orderBy("companyID", Query.Direction.ASCENDING).get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                list.clear();
                idList.clear();
                nameList.clear();
                for (QueryDocumentSnapshot qs : queryDocumentSnapshots) {
                    Investor data = qs.toObject(Investor.class);
                    data.setId(qs.getId());
                    idList.add(data.getId());
                    list.add(data);
                    nameList.add(data.getName());
                }
                searchAutoComplete.setAdapter(arrayAdapter);
                adapter.notifyDataSetChanged();
            }
        });
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
    }
}