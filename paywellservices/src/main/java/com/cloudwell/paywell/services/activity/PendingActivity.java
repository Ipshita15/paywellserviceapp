package com.cloudwell.paywell.services.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import androidx.appcompat.app.AppCompatActivity;

public class PendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        TextView mConErrorMsg = findViewById(R.id.connErrorMsg);
        Button mBtnRetry = findViewById(R.id.btnRetry);
        Button mBtnClose = findViewById(R.id.btnClose);
        ProgressBar mPBAppLoading = findViewById(R.id.pbAppLoading);

        mConErrorMsg.setText(Html.fromHtml(AppLoadingActivity.pendingStr));
        mConErrorMsg.setVisibility(View.VISIBLE);
        mPBAppLoading.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
        mBtnClose.setVisibility(View.VISIBLE);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mConErrorMsg.setTypeface(AppController.getInstance().getOxygenLightFont());
            mBtnRetry.setTypeface(AppController.getInstance().getOxygenLightFont());
            mBtnClose.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mConErrorMsg.setTypeface(AppController.getInstance().getAponaLohitFont());
            mBtnRetry.setTypeface(AppController.getInstance().getAponaLohitFont());
            mBtnClose.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_PENDING);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClickClose(View v) {
        finish();
    }
}
