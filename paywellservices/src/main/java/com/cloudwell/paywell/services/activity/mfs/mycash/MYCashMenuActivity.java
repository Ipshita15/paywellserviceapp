package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.mfs.MFSMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class MYCashMenuActivity extends AppCompatActivity {

    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycash_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_mfs_mycash);
        }
        mAppHandler = new AppHandler(this);

        CardView mCardView = findViewById(R.id.fbCardView);
        if (!mAppHandler.getMYCashBalance().equals("unknown") && !mAppHandler.getMYCashBalance().equals("null") && !mAppHandler.getMYCashBalance().equals("")) {
            TextView mBalance = findViewById(R.id.mycashBalance);
            mBalance.setText(getString(R.string.mycash_balance_des) + " " + mAppHandler.getMYCashBalance());
            if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                mBalance.setTypeface(AppController.getInstance().getOxygenLightFont());
            } else {
                mBalance.setTypeface(AppController.getInstance().getAponaLohitFont());
            }
        } else {
            mCardView.setVisibility(View.GONE);
        }

        Button btnCashInOut = findViewById(R.id.homeBtnCashInOut);
        Button btnFundManagement = findViewById(R.id.homeBtnFundManagement);
        Button btnBalance = findViewById(R.id.homeBtnBalance);
        Button btnRegistration = findViewById(R.id.homeBtnCustomerRegistration);
        Button btnGenerateOtp = findViewById(R.id.homeBtnGenerateOTP);
        Button btnChangePin = findViewById(R.id.homeBtnPinChange);
        Button btnInquiry = findViewById(R.id.homeBtnInquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnCashInOut.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnFundManagement.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBalance.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnRegistration.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnGenerateOtp.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnChangePin.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnCashInOut.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnFundManagement.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBalance.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnRegistration.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnGenerateOtp.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnChangePin.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnCashInOut:
                startActivity(new Intent(this, CashInOutActivity.class));
                finish();
                break;
            case R.id.homeBtnFundManagement:
                startActivity(new Intent(this, ManageMenuActivity.class));
                finish();
                break;
            case R.id.homeBtnBalance:
                startActivity(new Intent(this, BalanceActivity.class));
                finish();
                break;
            case R.id.homeBtnCustomerRegistration:
                startActivity(new Intent(this, CustomerRegistrationActivity.class));
                finish();
                break;
            case R.id.homeBtnGenerateOTP:
                startActivity(new Intent(this, GenerateOTPActivity.class));
                finish();
                break;
            case R.id.homeBtnPinChange:
                startActivity(new Intent(this, ChangeMYCashPinActivity.class));
                finish();
                break;
            case R.id.homeBtnInquiry:
                startActivity(new Intent(this, InquiryMenuActivity.class));
                finish();
                break;
            default:
                break;
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
        Intent intent = new Intent(MYCashMenuActivity.this, MFSMainActivity.class);
        startActivity(intent);
        finish();
    }
}
