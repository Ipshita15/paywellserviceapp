package com.cloudwell.paywell.services.activity.payments.bkash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BKashTrxClaimActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private LinearLayout mLinearLayout;
    private Button mConfirm;
    private EditText mEtPhoneNo;
    private EditText mEtAmount;
    private String mPhone = "";
    private String mAmount = "";
    private AppHandler mAppHandler;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_inquiry_trx);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_bkash_trx_claim_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);
        mEtPhoneNo = findViewById(R.id.etPhoneNoOrId);
        mEtAmount = findViewById(R.id.etAmount);
        mConfirm = findViewById(R.id.btnBkashInqConfirm);
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvPhnOrId)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mEtPhoneNo.setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvAmount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mEtAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvPhnOrId)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mEtPhoneNo.setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvAmount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mEtAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BKashTrxClaimActivity.this, BKashMenuActivity.class);
        startActivity(intent);
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
    public void onClick(View v) {
        if (v == mConfirm) {
            if (validatePhoneID() && validateAmount()) {
                submitInquiry();
            }
        }
    }


    @SuppressWarnings("deprecation")
    private boolean validatePhoneID() {
        if (mEtPhoneNo.getText().toString().trim().isEmpty() || mEtPhoneNo.getText().toString().length() < 10) {
            mEtPhoneNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.id_phone_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean validateAmount() {
        if (mEtAmount.getText().toString().trim().isEmpty()) {
            mEtAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    public void submitInquiry() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            mPhone = mEtPhoneNo.getText().toString().trim();
            mAmount = mEtAmount.getText().toString().trim();
            new TransactionInquiryAsync().execute(getResources().getString(R.string.bkash_inq_url));
        }
    }

    @SuppressWarnings("deprecation")
    private class TransactionInquiryAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashTrxClaimActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(6);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("trxOrPhoneNo", mPhone));
                nameValuePairs.add(new BasicNameValuePair("amount", mAmount));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    String msg = jsonObject.getString(TAG_MESSAGE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(BKashTrxClaimActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(msg);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
