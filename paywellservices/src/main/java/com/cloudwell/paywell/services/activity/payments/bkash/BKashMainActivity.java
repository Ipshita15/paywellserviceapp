package com.cloudwell.paywell.services.activity.payments.bkash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

/**
 * Created by Naima Gani on 11/28/2016.
 */

public class BKashMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText mPin;
    private Button mConfirm;
    private String _pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_payment_bkash_title);
        mCd = new ConnectionDetector(this);
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mPin = (EditText) findViewById(R.id.bkash_pin);
        mConfirm = (Button) findViewById(R.id.bkash_confirm);

        ((TextView) mLinearLayout.findViewById(R.id.tvBkashPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());

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
            new BKashMainActivity.SubmitAsync().execute(getResources().getString(R.string.bkash_balance_check),
                    "imei=" + mAppHandler.getImeiNo(),
                    "&pin=" + _pin);
        }
    }

    private class SubmitAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        private String txt;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2]);
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                if (result.startsWith("200")) {
                    mAppHandler.setPin(_pin);
                    Intent intent = new Intent(BKashMainActivity.this, BKashMenuActivity.class);
                    startActivity(intent);
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
        Intent intent = new Intent(BKashMainActivity.this, PaymentsMainActivity.class);
        startActivity(intent);
        finish();
    }
}

