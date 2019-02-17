package com.cloudwell.paywell.services.activity.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.MYCashMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;


public class MFSMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfs_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.nav_mfs);
        }

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Button btnMycash = findViewById(R.id.homeBtnMyCash);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnMycash.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnMycash.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MFS_MENU);

    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnMyCash:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_MFS_MENU, AnalyticsParameters.KEY_MFS_MYCASH_MENU);
                startActivity(new Intent(this, MYCashMainActivity.class));
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
        Intent intent = new Intent(MFSMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
