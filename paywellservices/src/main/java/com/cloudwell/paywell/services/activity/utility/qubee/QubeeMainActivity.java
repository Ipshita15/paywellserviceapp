package com.cloudwell.paywell.services.activity.utility.qubee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;

import java.util.List;

public class QubeeMainActivity extends AppCompatActivity {

    private static final String QUBEE = "Qubee";
    boolean hasQubeeService = false;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_qubee);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        initView();
    }

    private void initView() {
        List<String> serviceList = AppLoadingActivity.getUtilityService();
        for (int i = 0; i < serviceList.size(); i++) {
            if (QUBEE.equalsIgnoreCase(serviceList.get(i))) {
                hasQubeeService = true;
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QubeeMainActivity.this, UtilityMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnRecharge:
                if (hasQubeeService) {
                    startActivity(new Intent(this, RechargeActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnInquiry:
                if (hasQubeeService) {
                    startActivity(new Intent(this, InquiryActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnWrongAccount:
                if (hasQubeeService) {
                    startActivity(new Intent(this, ComplainAccountActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnWrongAmount:
                if (hasQubeeService) {
                    startActivity(new Intent(this, ComplainAmountActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnPayment:
                if (hasQubeeService) {
                    startActivity(new Intent(this, ComplainTrxActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            default:
                break;
        }
    }
}

