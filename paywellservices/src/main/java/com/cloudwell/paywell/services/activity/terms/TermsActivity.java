package com.cloudwell.paywell.services.activity.terms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;

import java.io.InputStream;

public class TermsActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_terms_and_conditions_title);

        mWebView = findViewById(R.id.wvStatementView);
        mWebView.getSettings().setBuiltInZoomControls(true);

        displayTermsAndConditions();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_TERMS_AND_CONDITIONS);

    }

    private void displayTermsAndConditions() {
        InputStream is;
        try {
            is = getAssets().open("terms_and_conditions_format.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            mWebView.loadUrl("file:///android_asset/terms_and_conditions_format.html");
        } catch (Exception e) {
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
