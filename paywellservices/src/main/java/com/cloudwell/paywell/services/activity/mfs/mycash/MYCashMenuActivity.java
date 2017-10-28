package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.mfs.MFSMainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class MYCashMenuActivity extends AppCompatActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycash_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_mfs_mycash);
        mAppHandler = new AppHandler(this);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mCd = new ConnectionDetector(this);
        CardView mCardView = (CardView) findViewById(R.id.fbCardView);
        if(!mAppHandler.getMYCashBalance().equals("unknown") && !mAppHandler.getMYCashBalance().equals("null") && !mAppHandler.getMYCashBalance().equals("")) {
            TextView mBalance = (TextView) findViewById(R.id.mycashBalance);
            mBalance.setText(getString(R.string.mycash_balance_des) + " " + mAppHandler.getMYCashBalance());
        } else {
            mCardView.setVisibility(View.GONE);
        }
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
//            case R.id.homeBtnUtilityBillPay:
//                startActivity(new Intent(this, UtilityMenuActivity.class));
//                finish();
//                break;
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
