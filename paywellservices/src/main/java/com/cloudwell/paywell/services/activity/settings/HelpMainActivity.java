package com.cloudwell.paywell.services.activity.settings;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.WebViewActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class HelpMainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppHandler mAppHandler;
    private ConnectionDetector mCd;
    private String packageNameYoutube = "com.google.android.youtube";
    private String linkRefillBalance = "https://www.youtube.com/watch?v=mRg-yT20Iyc";
    private String linkDpdcBillPay = "https://www.youtube.com/watch?v=EovJfDwrKSc&t=4s";
    private String linkPolliBillPay = "https://www.youtube.com/watch?v=SAuIFcUclvs&t=1s";
    private String linkAllServices = "https://www.youtube.com/watch?v=RNEjADit-PQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_main);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_settings_help);
        }

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());
        initializeView();
    }

    private void initializeView() {
        Button btnBalanceRefill = findViewById(R.id.homeBtnBalanceRefill);
        Button btnDpdcBillPay = findViewById(R.id.homeBtnDpdcBillPay);
        Button btnPolliBiddutBillPay = findViewById(R.id.homeBtnPolliBiddutBillPay);
        Button btnAllServices = findViewById(R.id.homeBtnAllServices);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnBalanceRefill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnDpdcBillPay.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPolliBiddutBillPay.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnAllServices.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnBalanceRefill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnDpdcBillPay.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPolliBiddutBillPay.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnAllServices.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        btnBalanceRefill.setOnClickListener(this);
        btnDpdcBillPay.setOnClickListener(this);
        btnPolliBiddutBillPay.setOnClickListener(this);
        btnAllServices.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.homeBtnBalanceRefill) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                boolean isYoutubeInstalled = isAppInstalled(packageNameYoutube);
                if (isYoutubeInstalled) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkRefillBalance));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage(packageNameYoutube);
                    startActivity(intent);
                } else {
                    WebViewActivity.TAG_LINK = linkRefillBalance;
                    startActivity(new Intent(HelpMainActivity.this, WebViewActivity.class));
                }
            }
        } else if (i == R.id.homeBtnDpdcBillPay) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                if (isAppInstalled(packageNameYoutube)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkDpdcBillPay));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage(packageNameYoutube);
                    startActivity(intent);
                } else {
                    WebViewActivity.TAG_LINK = linkDpdcBillPay;
                    startActivity(new Intent(HelpMainActivity.this, WebViewActivity.class));
                }
            }
        } else if (i == R.id.homeBtnPolliBiddutBillPay) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                if (isAppInstalled(packageNameYoutube)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPolliBillPay));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage(packageNameYoutube);
                    startActivity(intent);
                } else {
                    WebViewActivity.TAG_LINK = linkPolliBillPay;
                    startActivity(new Intent(HelpMainActivity.this, WebViewActivity.class));
                }
            }
        } else if (i == R.id.homeBtnAllServices) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                if (isAppInstalled(packageNameYoutube)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkAllServices));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage(packageNameYoutube);
                    startActivity(intent);
                } else {
                    WebViewActivity.TAG_LINK = linkAllServices;
                    startActivity(new Intent(HelpMainActivity.this, WebViewActivity.class));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HelpMainActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }
}
