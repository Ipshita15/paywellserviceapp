package com.cloudwell.paywell.services.activity.topup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.topup.brilliant.BrilliantTopupActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

public class TopupMenuActivity extends AppCompatActivity {

    Button mobileOperatorBtn, brilliantBtn;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topup);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mobileOperatorBtn = findViewById(R.id.btnMobileOperators);
        brilliantBtn = findViewById(R.id.btnBrilliant);

        mobileOperatorBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU);
                startActivity(new Intent(TopupMenuActivity.this, TopupMainActivity.class));
            }
        });

        brilliantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_MENU, AnalyticsParameters.KEY_TOPUP_BRILLIANT_MENU);
                startActivity(new Intent(TopupMenuActivity.this, BrilliantTopupActivity.class));
            }
        });

        refrashLanguage();
    }

    private void refrashLanguage() {
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mobileOperatorBtn.setTypeface(AppController.getInstance().getOxygenLightFont());
            brilliantBtn.setTypeface(AppController.getInstance().getOxygenLightFont());

        } else {
            mobileOperatorBtn.setTypeface(AppController.getInstance().getAponaLohitFont());
            brilliantBtn.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    private void showErrorMessage() {
        View mRelativeLayout = findViewById(R.id.relativeLayout);
        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TopupMenuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
