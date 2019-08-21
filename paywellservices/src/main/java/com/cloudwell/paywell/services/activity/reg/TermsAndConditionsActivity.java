package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.cloudwell.paywell.services.R;

import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class TermsAndConditionsActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Terms And Conditions");
        }

        mWebView = findViewById(R.id.wvStatementView);
        mWebView.getSettings().setBuiltInZoomControls(true);

        displayTermsAndConditions();
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
        Intent intent = new Intent(TermsAndConditionsActivity.this, EntryMainActivity.class);
        startActivity(intent);
        finish();
    }
}
