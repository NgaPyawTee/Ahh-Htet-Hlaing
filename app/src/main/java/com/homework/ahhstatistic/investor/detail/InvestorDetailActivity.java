package com.homework.ahhstatistic.investor.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.homework.ahhstatistic.R;

public class InvestorDetailActivity extends AppCompatActivity {
    private String ID;
    public static final String ID_PASS = "ID Pass";
    public static final String BUNDLE_PASS = "Bundle Pass";

    private ViewPager pager;
    private TabLayout tabLayout;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_detail);

        Intent intent = getIntent();
        ID = intent.getStringExtra(ID_PASS);

        bundle.putString(BUNDLE_PASS,ID);

        pager = findViewById(R.id.detail_pager);
        tabLayout = findViewById(R.id.detail_tablayout);
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    fragment = new FirstContractFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 2:
                    fragment = new SecondContractFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 3:
                    fragment = new ThirdContractFragment();
                    fragment.setArguments(bundle);
                    return fragment;
            }
            return new HomeFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    String home = "Home";
                    return home;
                case 1:
                    String firstPlan = "1st";
                    return firstPlan;
                case 2:
                    String secondPlan = "2nd";
                    return secondPlan;
                case 3:
                    String finalPlan = "3rd";
                    return finalPlan;
            }
            return null;
        }
    }
}