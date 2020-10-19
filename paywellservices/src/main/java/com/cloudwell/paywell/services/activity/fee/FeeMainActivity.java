package com.cloudwell.paywell.services.activity.fee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;

public class FeeMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.fee);
        }
        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Button btnIvac = findViewById(R.id.homeBtnIvac);



        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_FEE_MENU);

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

    public void onButtonClicker(View v) {
        switch (v.getId()) {


            case R.id.homeBtnIvac:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_MENU);
                startActivity(new Intent(this, IvacMainActivity.class));
            default:
                break;
        }
    }
}
