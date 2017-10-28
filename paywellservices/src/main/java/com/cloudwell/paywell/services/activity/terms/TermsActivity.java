package com.cloudwell.paywell.services.activity.terms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppController;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Naima Gani on 12/21/2016.
 */

public class TermsActivity extends AppCompatActivity {

    private WebView mWebView;
    private TextView mTvErrorMsg;
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_terms_and_conditions_title);

        mWebView = (WebView) findViewById(R.id.wvStatementView);
        mWebView.getSettings().setBuiltInZoomControls(true);

        mTvErrorMsg = (TextView) findViewById(R.id.tvErrorMessage);
        mTvErrorMsg.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        displayTermsAndConditions();
    }

    private void displayTermsAndConditions() {
        InputStream is;
        String str;
        try {
            is = getAssets().open("terms_and_conditions_format.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            mWebView.loadUrl("file:///android_asset/terms_and_conditions_format.html");
        } catch (IOException e) {
            e.printStackTrace();
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
        Intent intent = new Intent(TermsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
