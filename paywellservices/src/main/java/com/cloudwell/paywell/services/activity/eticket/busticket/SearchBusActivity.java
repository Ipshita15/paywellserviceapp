package com.cloudwell.paywell.services.activity.eticket.busticket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticket.fragment.ACFragment;
import com.cloudwell.paywell.services.activity.eticket.busticket.fragment.AllFragment;
import com.cloudwell.paywell.services.activity.eticket.busticket.fragment.NonACFragment;
import com.cloudwell.paywell.services.app.AppHandler;

@SuppressLint("NewApi")
public class SearchBusActivity extends AppCompatActivity {

    AppHandler mAppHandeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_main);
        getSupportActionBar().setTitle(R.string.bus_list_ticket_msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppHandeler = AppHandler.getmInstance(getApplicationContext());
        initSlidingTabs();
    }

    private void initSlidingTabs() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        SampleViewPagerAdapter mViewPagerAdapter = new SampleViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        // key board back pressed
        int _tabPosition = 0;
        Bundle delivered = getIntent().getExtras();
        if (delivered != null && !delivered.isEmpty()) {
            _tabPosition = delivered.getInt("tab_position");
            mViewPager.setCurrentItem(_tabPosition);
        }

//        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
//        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
//
//        mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#ffffff"));
//        mSlidingTabLayout.setDistributeEvenly(true);
//        mSlidingTabLayout.setViewPager(mViewPager);
    }

    private class SampleViewPagerAdapter extends FragmentPagerAdapter {
        public SampleViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new AllFragment();
            } else if (position == 1) {
                return new ACFragment();
            } else if (position == 2) {
                return new NonACFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String pageTitle = null;
            if (position == 0) {
                pageTitle = getString(R.string.all_list_ticket_msg);
            } else if (position == 1) {
                pageTitle = getString(R.string.ac_list_ticket_msg);
            } else if (position == 2) {
                pageTitle = getString(R.string.non_ac_list_ticket_msg);
            }
            return pageTitle;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchBusActivity.this, BusMainActivity.class);
        startActivity(intent);
        finish();
    }
}
