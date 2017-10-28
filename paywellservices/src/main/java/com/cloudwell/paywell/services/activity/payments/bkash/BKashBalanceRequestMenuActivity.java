package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class BKashBalanceRequestMenuActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private ConnectionDetector mCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_balance_request_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_bkash_balance_req_title);
        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(this);
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBkashPWBalance:
                Intent intent_pw = new Intent(BKashBalanceRequestMenuActivity.this, BKashInquiryBalanceRequestActivity.class);
                intent_pw.putExtra(BKashInquiryBalanceRequestActivity.REQUEST_TYPE, BKashInquiryBalanceRequestActivity.PW_BALANCE);
                startActivity(intent_pw);
                finish();
                break;
            case R.id.homeBtnBkashBankTransfer:
                Intent intent_bank = new Intent(BKashBalanceRequestMenuActivity.this, BKashInquiryBalanceRequestActivity.class);
                intent_bank.putExtra(BKashInquiryBalanceRequestActivity.REQUEST_TYPE, BKashInquiryBalanceRequestActivity.BANK_BALANCE);
                startActivity(intent_bank);
                finish();
                break;
            case R.id.homeBtnBkashForCash:
                Intent intent_cash = new Intent(BKashBalanceRequestMenuActivity.this, BKashInquiryBalanceRequestActivity.class);
                intent_cash.putExtra(BKashInquiryBalanceRequestActivity.REQUEST_TYPE, BKashInquiryBalanceRequestActivity.CASH_BALANCE);
                startActivity(intent_cash);
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
        Intent intent = new Intent(BKashBalanceRequestMenuActivity.this, BKashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
