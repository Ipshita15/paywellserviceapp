package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BKashInquiryBalanceRequestActivity extends BaseActivity implements View.OnClickListener {

    public static final String REQUEST_TYPE = "requestType";
    public static final String PW_BALANCE = "pwBalance";
    public static final String BANK_BALANCE = "bankBalance";
    public static final String CASH_BALANCE = "cashBalance";
    private LinearLayout _linearLayout;
    private Button mBtnSubmit;
    private EditText mBkashAmount;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private static String txt;
    private String requestType = null;
    private String bAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_req_balance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _linearLayout = findViewById(R.id.linearLayout);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);

        Bundle delivered = getIntent().getExtras();
        if (delivered != null && !delivered.isEmpty()) {
            requestType = delivered.getString(REQUEST_TYPE);
        }
        if (requestType.equalsIgnoreCase(PW_BALANCE)) {
            getSupportActionBar().setTitle(R.string.home_bkash_paywell_balance_req_title);
        } else if (requestType.equalsIgnoreCase(BANK_BALANCE)) {
            getSupportActionBar().setTitle(R.string.home_bkash_bank_trns_req_title);
        } else {
            getSupportActionBar().setTitle(R.string.home_bkash_cash_req_title);
        }
        initView();
    }

    private void initView() {
        mBkashAmount = findViewById(R.id.etReqBalance);
        mBtnSubmit = findViewById(R.id.bKashReqConfirm);
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) _linearLayout.findViewById(R.id.tvSelectAmount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mBkashAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mBtnSubmit.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) _linearLayout.findViewById(R.id.tvSelectAmount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mBkashAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mBtnSubmit.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mBkashAmount.getText().toString().matches("^0") || mBkashAmount.getText().toString().matches("^00") || mBkashAmount.getText().toString().length() == 0) {
                mBkashAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
            } else {
                bAmount = mBkashAmount.getText().toString().trim();
                ResponsePrompt();
            }
        }
    }

    private void ResponsePrompt() {
        if (!cd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            if (requestType.equalsIgnoreCase(PW_BALANCE)) {
                new ResponseAsync().execute(getResources().getString(R.string.bkash_paymnt_req_paywell),
                        "imei=" + mAppHandler.getImeiNo(),
                        "&pin=" + mAppHandler.getPin(),
                        "&amount=" + bAmount,
                        "&gateway_id=" + mAppHandler.getGatewayId());
            } else if (requestType.equalsIgnoreCase(BANK_BALANCE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BKashInquiryBalanceRequestActivity.this);
                builder.setTitle(R.string.fund_transfer_title_msg);
                builder.setMessage(R.string.alert_transfer_msg);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                        new ResponseAsync().execute(getResources().getString(R.string.bkash_bank_trns),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&pin=" + mAppHandler.getPin(),
                                "&amount=" + bAmount,
                                "&gateway_id=" + mAppHandler.getGatewayId());
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (requestType.equalsIgnoreCase(CASH_BALANCE)) {
                new ResponseAsync().execute(getResources().getString(R.string.bkash_req_for_cash),
                        "imei=" + mAppHandler.getImeiNo(),
                        "&pin=" + mAppHandler.getPin(),
                        "&amount=" + bAmount,
                        "&gateway_id=" + mAppHandler.getGatewayId());
            }
        }
    }

    private class ResponseAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("amount", bAmount));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(_linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("logTag", result);
            dismissProgressDialog();
            if (result != null) {
                String[] splitArray = result.split("@");
                txt = String.valueOf(splitArray[1]);
                AlertDialog.Builder builder = new AlertDialog.Builder(BKashInquiryBalanceRequestActivity.this);
                builder.setTitle("Result");
                builder.setMessage(txt);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(BKashInquiryBalanceRequestActivity.this, BKashBalanceRequestMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Snackbar snackbar = Snackbar.make(_linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(BKashInquiryBalanceRequestActivity.this, BKashBalanceRequestMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
