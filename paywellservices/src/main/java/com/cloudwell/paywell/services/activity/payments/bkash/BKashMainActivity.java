package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BKashMainActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText mPin;
    private Button mConfirm;
    private String _pin;

    private static final String TAG_RESPONSE_STATUS = "Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_payment_bkash_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(AppController.getContext());
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);
        mPin = findViewById(R.id.bkash_pin);
        mConfirm = findViewById(R.id.bkash_confirm);

        ((TextView) mLinearLayout.findViewById(R.id.tvBkashPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
        mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
        mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());

        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            _pin = mPin.getText().toString().trim();
            if (_pin.length() == 0) {
                mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else {
                submitConfirm();
            }
        }
    }

    private void submitConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            new BKashMainActivity.SubmitAsync().execute(getString(R.string.bkash_balance_check));
        }
    }

    private class SubmitAsync extends AsyncTask<String, Void, String> {

        private String txt;

        @Override
        protected void onPreExecute() {
            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", _pin));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                if (result.startsWith("200")) {
                    mAppHandler.setPin(_pin);
                    String[] splittedArray = result.split("@");
                    if(mAppHandler.getGatewayId().equalsIgnoreCase("1")) {
                        mAppHandler.setAgentPhnNum(splittedArray[5]);
                    } else if(mAppHandler.getGatewayId().equalsIgnoreCase("4")) {
                        mAppHandler.setAgentPhnNum(splittedArray[6]);
                    }
                    if(splittedArray[3].equals("0.00")) {
                        Intent intent = new Intent(BKashMainActivity.this, BKashMenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        checkPurpose();
                    }
                    mPin.setText("");
                } else {
                    String[] splitArray = result.split("@");
                    txt = String.valueOf(splitArray[1]);

                    Snackbar snackbar = Snackbar.make(mLinearLayout, txt, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    public void checkPurpose() {
        new PDAsyncTask().execute(getResources().getString(R.string.bkash_pd_url),
                "imei=" + mAppHandler.getImeiNo(),
                "&pin=" + mAppHandler.getPin(),
                "&gateway_id=" + mAppHandler.getGatewayId(),
                "&format=json");
    }

    private class PDAsyncTask extends AsyncTask<String, String, JSONObject> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dismissProgressDialog();
            try {
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                if (status.equalsIgnoreCase("200")) {
                    Intent intent = new Intent(BKashMainActivity.this, DeclarePurposeActivity.class);
                    intent.putExtra(DeclarePurposeActivity.JSON_RESPONSE, jsonObject.toString());
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(BKashMainActivity.this, BKashMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_video) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=wOlYSchfWPo"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BKashMainActivity.this, PaymentsMainActivity.class);
        startActivity(intent);
        finish();
    }
}

