package com.cloudwell.paywell.services.activity.utility.banglalion;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import androidx.appcompat.app.AppCompatActivity;

public class BanglalionMainActivity extends AppCompatActivity {

    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banglalion_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_banglalion);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Button btnBLRecharge = findViewById(R.id.homeBtnRecharge);
        Button btnBLInq = findViewById(R.id.homeBtnInquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnBLRecharge.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBLInq.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnBLRecharge.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBLInq.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_BANGLALION_MENU);
    }

    public void goToBanglalionRecharge(View view) {
        startActivity(new Intent(BanglalionMainActivity.this, BanglalionRechargeActivity.class));

    }

    public void goToRechargeInquiry(View view) {
        startActivity(new Intent(BanglalionMainActivity.this, BanglalionRechargeInquiryActivity.class));

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
