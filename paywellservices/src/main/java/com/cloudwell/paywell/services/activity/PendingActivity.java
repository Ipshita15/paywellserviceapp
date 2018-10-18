package com.cloudwell.paywell.services.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;

public class PendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        TextView mConErrorMsg = findViewById(R.id.connErrorMsg);
        Button mBtnRetry = findViewById(R.id.btnRetry);
        Button mBtnClose = findViewById(R.id.btnClose);
        ProgressBar mPBAppLoading = findViewById(R.id.pbAppLoading);

        mConErrorMsg.setText(AppLoadingActivity.pendingStr);
        mConErrorMsg.setVisibility(View.VISIBLE);
        mPBAppLoading.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
        mBtnClose.setVisibility(View.VISIBLE);
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
