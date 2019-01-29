package com.cloudwell.paywell.services.activity.eticket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;

public class ETicketMainActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private RelativeLayout relativeLayout;
    private Button home_bus, home_train;

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

        mAppHandler = new AppHandler(this);

        relativeLayout = findViewById(R.id.eticketRelativeLayout);

        home_bus = findViewById(R.id.homeBtnBusTicket);
        home_train = findViewById(R.id.homeBtnTrainTicket);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_bus.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_train.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_bus.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_train.setTypeface(AppController.getInstance().getAponaLohitFont());
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
            case R.id.homeBtnBusTicket:
//                startActivity(new Intent(this, BusMainActivity.class));
//                finish();
                showCommingSoonMesage();
                break;
            case R.id.homeBtnTrainTicket:
//                startActivity(new Intent(this, TrainMainActivity.class));
//                finish();
                showCommingSoonMesage();
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
