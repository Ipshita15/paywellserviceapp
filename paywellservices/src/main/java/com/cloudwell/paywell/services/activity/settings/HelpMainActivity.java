package com.cloudwell.paywell.services.activity.settings;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class HelpMainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppHandler mAppHandler;
    private ConnectionDetector mCd;
    private Button btnBalanceRefill, btnBkashPayment, btnDpdcBillPay, btnPolliBiddutBillPay, btnAllServices;

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
        btnBalanceRefill = findViewById(R.id.homeBtnBalanceRefill);
        btnBkashPayment = findViewById(R.id.homeBtnBkashPayment);
        btnDpdcBillPay = findViewById(R.id.homeBtnDpdcBillPay);
        btnPolliBiddutBillPay = findViewById(R.id.homeBtnPolliBiddutBillPay);
        btnAllServices = findViewById(R.id.homeBtnAllServices);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnBalanceRefill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBkashPayment.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnDpdcBillPay.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPolliBiddutBillPay.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnAllServices.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnBalanceRefill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBkashPayment.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnDpdcBillPay.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPolliBiddutBillPay.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnAllServices.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        btnBalanceRefill.setOnClickListener(this);
        btnBkashPayment.setOnClickListener(this);
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=mRg-yT20Iyc"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        } else if (i == R.id.homeBtnBkashPayment) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=wOlYSchfWPo"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        } else if (i == R.id.homeBtnDpdcBillPay) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=EovJfDwrKSc&t=4s"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        } else if (i == R.id.homeBtnPolliBiddutBillPay) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=SAuIFcUclvs&t=1s"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        } else if (i == R.id.homeBtnAllServices) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(this.getSupportFragmentManager());
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=RNEjADit-PQ"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
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
}
