package com.cloudwell.paywell.services.activity.payments.rocket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;

import java.util.List;


/**
 * Created by Naima Gani on 12/19/2016.
 */

public class RocketMainActivity extends AppCompatActivity {

    public static final String _serviceRocket = "Pay To Rocket";
    private CoordinatorLayout mCoordinateLayout;
    boolean hasRocketService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_payment_pay_to_rocket);
        mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        initView();
    }

    private void initView() {
        List<String> serviceList = AppLoadingActivity.getPaymentsService();
        for (int i = 0; i < serviceList.size(); i++) {
            if (_serviceRocket.equalsIgnoreCase(serviceList.get(i))) {
                hasRocketService = true;
                break;
            }
        }
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnCashIn:
                if (hasRocketService) {
                    startActivity(new Intent(this, RocketCashInActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnCashInInquiry:
                if (hasRocketService) {
                    startActivity(new Intent(this, RocketInquiryActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(RocketMainActivity.this, PaymentsMainActivity.class);
        startActivity(intent);
        finish();
    }
}

