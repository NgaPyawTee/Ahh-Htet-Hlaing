package com.homework.ahhstatistic.investor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
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
import android.widget.Toast;

import com.homework.ahhstatistic.R;

public class InvestorCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InvestorRecyclerAdapter adapter;

    //search view auto fill
    private SearchView.SearchAutoComplete searchAutoComplete;
    private String sentName;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_category);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat controllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        controllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        controllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        Toolbar toolbar = findViewById(R.id.investor_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(R.color.black);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setThreshold(1);

        String[] names = {"Khun Kyaw Win Soe","Ah Htet Hlaing","Kyaw Nyi Win","Hein Htet Aung","Sai Khay Kham"};
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,names);
        searchAutoComplete.setAdapter(arrayAdapter);

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String getName = (String) adapterView.getItemAtPosition(i);
                searchAutoComplete.setText(getName);
                sentName = getName;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (sentName != null){
                    Intent intent = new Intent(InvestorCategory.this,InvestorDetail.class);
                    intent.putExtra(InvestorDetail.SENT_NAME,sentName);
                    startActivity(intent);
                }else{
                    Toast.makeText(InvestorCategory.this, "Check the name again", Toast.LENGTH_SHORT).show();
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

    }
}