package com.cloudwell.paywell.services.activity.payments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.payments.bkash.BKashMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class PaymentsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_payments);
        }
        AppHandler mAppHandler = new AppHandler(this);

        Button btnBkash = findViewById(R.id.homeBtnBkash);
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnBkash.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnBkash.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBkash:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_PAYMENT_MENU, AnalyticsParameters.KEY_PAYMENT_BKASH_MENU);
                startActivity(new Intent(this, BKashMainActivity.class));
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
        Intent intent = new Intent(PaymentsMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
