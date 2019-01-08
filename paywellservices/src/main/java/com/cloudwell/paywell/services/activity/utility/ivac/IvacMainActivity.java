package com.cloudwell.paywell.services.activity.utility.ivac;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class IvacMainActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivac_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_ivac);
        }

        mRelativeLayout = findViewById(R.id.relativeLayout);
        cd = new ConnectionDetector(AppController.getContext());

        AppHandler appHandler = new AppHandler(this);

        Button btnBill = findViewById(R.id.homeBtnBillPay);
        Button btnInquiry = findViewById(R.id.homeBtnInquiry);

        if (appHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBillPay:

                cd = new ConnectionDetector(AppController.getContext());
                if (cd.isConnectingToInternet()) {
                    startActivity(new Intent(IvacMainActivity.this, IvacFeePayActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }

                break;
            case R.id.homeBtnInquiry:
                startActivity(new Intent(IvacMainActivity.this, IvacFeeInquiryMainActivity.class));
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
        finish();
    }


}
