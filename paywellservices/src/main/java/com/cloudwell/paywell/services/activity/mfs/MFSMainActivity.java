package com.cloudwell.paywell.services.activity.mfs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.MYCashMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.utils.StringConstant;


public class MFSMainActivity extends BaseActivity {

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


        addRecentUsedList();

    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_mfs_mycash, StringConstant.KEY_home_mfs_fav, IconConstant.KEY_ic_my_cash, 0, 36);
        addItemToRecentListInDB(recentUsedMenu);
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
