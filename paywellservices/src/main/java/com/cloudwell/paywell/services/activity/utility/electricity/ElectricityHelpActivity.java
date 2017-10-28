package com.cloudwell.paywell.services.activity.utility.electricity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;


public class ElectricityHelpActivity extends AppCompatActivity {

    private ViewPager viewPager;
    MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;

    private String serviceName;
    private String TAG_DESCO_SERVICE = "DESCO";
    private String TAG_DPDC_SERVICE = "DPDC";
    private String TAG_WASA_SERVICE = "WASA";
    private String TAG_WZPDCL_SERVICE = "WZPDCL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_help);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                serviceName = null;
            } else {
                serviceName = extras.getString("serviceName");
            }
        } else {
            serviceName = (String) savedInstanceState.getSerializable("serviceName");
        }

        // layouts of all welcome sliders
        // add few more layouts if you want
        if(serviceName != null) {
            if (serviceName.equalsIgnoreCase(TAG_DESCO_SERVICE)) {
                layouts = new int[]{
                        R.drawable.ic_help_desco_one,
                        R.drawable.ic_help_desco_two,
                        R.drawable.ic_help_desco_three,
                        R.drawable.ic_help_desco_four,
                        R.drawable.ic_help_desco_five,
                        R.drawable.ic_help_desco_six,
                        R.drawable.ic_help_desco_seven};
            } else if (serviceName.equalsIgnoreCase(TAG_DPDC_SERVICE)) {
                layouts = new int[]{
                        R.drawable.ic_help_dpdc_one,
                        R.drawable.ic_help_dpdc_two,
                        R.drawable.ic_help_dpdc_three,
                        R.drawable.ic_help_dpdc_four,
                        R.drawable.ic_help_desco_five,
                        R.drawable.ic_help_dpdc_five,
                        R.drawable.ic_help_dpdc_six};
            } else if (serviceName.equalsIgnoreCase(TAG_WASA_SERVICE)) {
                layouts = new int[]{
                        R.drawable.ic_help_wasa_one,
                        R.drawable.ic_help_wasa_two,
                        R.drawable.ic_help_wasa_three,
                        R.drawable.ic_help_desco_four,
                        R.drawable.ic_help_desco_five,
                        R.drawable.ic_help_wasa_four,
                        R.drawable.ic_help_desco_seven};
            } else if (serviceName.equalsIgnoreCase(TAG_WZPDCL_SERVICE)) {
                layouts = new int[]{
                        R.drawable.ic_help_west_zone_one,
                        R.drawable.ic_help_west_zone_two,
                        R.drawable.ic_help_west_zone_three,
                        R.drawable.ic_help_west_zone_four,
                        R.drawable.ic_help_desco_five,
                        R.drawable.ic_help_west_zone_five,
                        R.drawable.ic_help_west_zone_six};
            }
        } else {
            startActivity(new Intent(this, UtilityMainActivity.class));
        }

        // adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchHomeScreen();
                onBackPressed();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    //launchHomeScreen();
                    onBackPressed();
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#004d00"));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#00b300"));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    /*private void launchHomeScreen() {
        startActivity(new Intent(ElectricityHelpActivity.this, DESCOMainActivity.class));
        finish();
    }*/

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.got_it_btn));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next_btn));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        private MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.help_pager_item, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(layouts[position]);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onBackPressed() {
        if(serviceName.equalsIgnoreCase(TAG_DESCO_SERVICE)) {
            startActivity(new Intent(ElectricityHelpActivity.this, DESCOMainActivity.class));
        } else if(serviceName.equalsIgnoreCase(TAG_DPDC_SERVICE)) {
            startActivity(new Intent(ElectricityHelpActivity.this, DPDCMainActivity.class));
        } else if(serviceName.equalsIgnoreCase(TAG_WASA_SERVICE)) {
            startActivity(new Intent(ElectricityHelpActivity.this, WASAMainActivity.class));
        } else if(serviceName.equalsIgnoreCase(TAG_WZPDCL_SERVICE)) {
            startActivity(new Intent(ElectricityHelpActivity.this, WZPDCLMainActivity.class));
        }
        finish();
    }

}