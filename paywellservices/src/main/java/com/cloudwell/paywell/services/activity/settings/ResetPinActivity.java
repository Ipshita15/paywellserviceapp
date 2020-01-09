package com.cloudwell.paywell.services.activity.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPinActivity extends AppCompatActivity {

    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private RelativeLayout mRelativeLayout;
    private AppHandler mAppHandler;
    private ProgressBar mProgressbar;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_reset_pin);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_settings_reset_pin);
        }
        mRelativeLayout = findViewById(R.id.linearLayout);
        mProgressbar = findViewById(R.id.progressBar);
        mTextView = findViewById(R.id.tvMessage);
        mTextView.setTypeface(AppController.getInstance().getOxygenLightFont());
        ConnectionDetector mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());


        if (!mCd.isConnectingToInternet()) {
            mAppHandler.showDialog(getSupportFragmentManager());
        } else {
            String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());
            new ResetPinAsync().execute(getString(R.string.reset_pin),
                    "username=" + mAppHandler.getImeiNo(),
                    "&mode=" + "json" +"&" + ParameterUtility.KEY_REF_ID +"="+ uniqueKey);
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_SETTINGS_RESET_PIN_MENU);

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
        Intent intent = new Intent(ResetPinActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private class ResetPinAsync extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mProgressbar.setVisibility(View.GONE);
            try {
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                if (status.equalsIgnoreCase("200")) {
                    mTextView.setText(jsonObject.getString(TAG_MESSAGE));
                    mTextView.setVisibility(View.VISIBLE);
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, jsonObject.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }

        }
    }
}
