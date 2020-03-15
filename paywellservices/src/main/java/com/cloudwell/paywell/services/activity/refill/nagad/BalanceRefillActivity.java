package com.cloudwell.paywell.services.activity.refill.nagad;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.UtilityBaseActivity;
import com.cloudwell.paywell.services.activity.refill.nagad.fragment.ClimBalanceRefillFragment;
import com.cloudwell.paywell.services.activity.refill.nagad.fragment.DirectBalanceRefillFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class BalanceRefillActivity extends UtilityBaseActivity {

    private TabLayout tabLayout;
    private HeightWrappingViewPager  viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_refill);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_refill_balance_title);
        }

        viewPager = (HeightWrappingViewPager) findViewById(R.id.viewpager);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DirectBalanceRefillFragment(), "ডিরেক্ট ব্যালেন্স রিফিল");
        adapter.addFragment(new ClimBalanceRefillFragment(), "ক্লেইম ব্যালেন্স রিফিল");
       // adapter.addFragment(new BalanceRefillFragment(), "USSD রিফিল প্রক্রিয়া");
//        adapter.addFragment(new UssdRefillFragment(), "USSD রিফিল প্রক্রিয়া");
        viewPager.setAdapter(adapter);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
