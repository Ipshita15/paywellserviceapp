package com.cloudwell.paywell.services.activity.utility.banglalion;

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

public class BanglalionRechargeActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText mPin;
    private static EditText mAccountNo;
    private Button mConfirm;
    private ConnectionDetector cd;
    private static EditText mAmount;
    private static String accountNo;
    private static String amount;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banglalion_recharge);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_banglalion_recharge_title);
        }
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.banglalionRechargeLL);
        TextView _pin = findViewById(R.id.tvQbeePin);
        _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
        mPin = findViewById(R.id.etQubeePin);
        mPin.setTypeface(AppController.getInstance().getOxygenLightFont());

        TextView _qubeeAccNo = findViewById(R.id.tvQbeeAccount);
        _qubeeAccNo.setTypeface(AppController.getInstance().getOxygenLightFont());
        mAccountNo = findViewById(R.id.etQubeeAccount);
        mAccountNo.setTypeface(AppController.getInstance().getOxygenLightFont());

        TextView _amount = findViewById(R.id.tvQbeeAmount);
        _amount.setTypeface(AppController.getInstance().getOxygenLightFont());
        mAmount = findViewById(R.id.etQbeeAmount);
        mAmount.setTypeface(AppController.getInstance().getOxygenLightFont());

        mConfirm = findViewById(R.id.btnQubeeConfirm);
        mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        mConfirm.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == mConfirm) {
            String _pin = mPin.getText().toString().trim();
            accountNo = mAccountNo.getText().toString().trim();
            amount = mAmount.getText().toString().trim();
            if (_pin.length() == 0) {
                mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else if (accountNo.length() < 4) {
                mAccountNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font></font>"));
            } else if (amount.length() == 0) {
                mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font></font>"));
            } else {
                if (!cd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    new SubmitAsync().execute(getResources().getString(R.string.banglalion_bill_pay),
                            mAppHandler.getImeiNo(),
                            accountNo,
                            amount,
                            _pin);
                }
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BanglalionRechargeActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(5);
                nameValuePairs.add(new BasicNameValuePair("userName", params[1]));
                nameValuePairs.add(new BasicNameValuePair("customerID", params[2]));
                nameValuePairs.add(new BasicNameValuePair("amount", params[3]));
                nameValuePairs.add(new BasicNameValuePair("password", params[4]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
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
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

                if (status != null && status.equals("200")) {
                    String trxId = jsonObject.getString("trans_id");
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {
                        String blcTrx = data.getString("BLCTrx");
                        String amount = data.getString("amount");
                        String retailCommission = data.getString("retCommission");
                        String accountNum = data.getString("customerID");
                        String hotLine = data.getString("contact");
                        showStatusDialog(message, accountNum, amount, trxId, blcTrx, retailCommission, hotLine);
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG);
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

    private void showStatusDialog(String msg, String accountNo, String amount, String trxId, String banglalinkTrx, String retailCommission,String hotline) {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + accountNo
                        + "\n" + getString(R.string.amount_des) + " " + amount + " " + R.string.tk_des
                        + "\n" + getString(R.string.trx_id_des) + " " + trxId
                        + "\n" + getString(R.string.banglalion_trx_id_des) + " " + banglalinkTrx
                        + "\n" + getString(R.string.retail_commission) + " " + retailCommission + " " + R.string.tk_des
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + " " + hotline);
        AlertDialog.Builder builder = new AlertDialog.Builder(BanglalionRechargeActivity.this);
        builder.setTitle("Result " + msg);
        builder.setMessage(reqStrBuilder.toString());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BanglalionRechargeActivity.this, BanglalionMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}