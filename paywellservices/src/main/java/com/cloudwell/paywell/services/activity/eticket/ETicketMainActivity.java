package com.cloudwell.paywell.services.activity.eticket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.google.android.material.snackbar.Snackbar;

public class ETicketMainActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private RelativeLayout relativeLayout;
    private Button home_bus, home_train, home_lucnch;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_eticket);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        relativeLayout = findViewById(R.id.eticketRelativeLayout);

        home_bus = findViewById(R.id.et_shop_visit);
        home_train = findViewById(R.id.et_ek_shop_report);
        home_lucnch = findViewById(R.id.et_launch);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_bus.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_train.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_lucnch.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_bus.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_train.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_lucnch.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        checkIsComeFromFav(getIntent());

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_ABOUT_PAGE);

    }

    private void checkIsComeFromFav(Intent intent) {
        boolean isFav = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE, false);
        if (isFav) {
            boolean booleanExtra = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_BUS, false);
            if (booleanExtra) {
                showCommingSoonMesage();
            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AIR, false)) {
                showCommingSoonMesage();
            }

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

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.et_shop_visit:
                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW, true);
                AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_BUS_TICKET);
                startActivity(new Intent(this, BusTicketMenuActivity.class));
                break;
            case R.id.et_ek_shop_report:

                AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_AIR_TICKET);
                startActivity(new Intent(this, AirTicketMenuActivity.class));
                break;

            case R.id.et_launch:
                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW, false);
                AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_Launch);
                startActivity(new Intent(this, BusTicketMenuActivity.class));
                break;

            default:
                break;
        }
    }

    private void showCommingSoonMesage() {
        Snackbar snackbar = Snackbar.make(relativeLayout, R.string.coming_soon_msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();
    }
}
