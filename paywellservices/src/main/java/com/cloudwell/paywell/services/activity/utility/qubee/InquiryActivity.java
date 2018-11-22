package com.cloudwell.paywell.services.activity.utility.qubee;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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

public class InquiryActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPin, mAccountNO;
    private Button mSubmitInquiry;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private String status, trxId, accountNo, amount, hotline;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_inquiry);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_qubee_inq_title);
        }
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);
        TextView _pin = findViewById(R.id.tvQubeePin2);
        _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
        mPin = findViewById(R.id.etQubeePin2);
        mPin.setTypeface(AppController.getInstance().getOxygenLightFont());

        TextView _inq_acc = findViewById(R.id.tvQubeeccount2);
        _inq_acc.setTypeface(AppController.getInstance().getOxygenLightFont());
        mAccountNO = findViewById(R.id.etQubeeccount2);
        mAccountNO.setTypeface(AppController.getInstance().getOxygenLightFont());

        mSubmitInquiry = findViewById(R.id.btnQubeeConfirm2);
        mSubmitInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        mSubmitInquiry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSubmitInquiry) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                String _pin = mPin.getText().toString().trim();
                if (_pin.length() == 0) {
                    mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                String _account = mAccountNO.getText().toString().trim();
                if (_account.length() < 4) {
                    mAccountNO.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font></font>"));
                    return;
                }
                new SubmitAsync().execute(getString(R.string.qb_inq), _account, _pin);
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            showStatusDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("iemi_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("account_no", params[1]));
                nameValuePairs.add(new BasicNameValuePair("pin_code", params[2]));
                nameValuePairs.add(new BasicNameValuePair("wimax", AppHandler.KEY_WIMAX));
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
            try {
                if (result != null && result.contains("@")) {
                    String splitedArray[] = result.split("@");
                    if (splitedArray.length > 0) {
                        if (splitedArray[0].equalsIgnoreCase("200") || splitedArray[0].equalsIgnoreCase("100")) {
                            status = splitedArray[1];
                            trxId = splitedArray[2];
                            accountNo = splitedArray[3];
                            amount = splitedArray[4];
                            hotline = splitedArray[7];
                            showStatusDialog();
                        } else {
                            Snackbar snackbar = Snackbar.make(mLinearLayout, splitedArray[1], Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
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

    private void showStatusDialog() {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + accountNo
                + "\n" + getString(R.string.amount_des) + " " + amount
                + "\n" + getString(R.string.trx_id_des) + " " + trxId
                + "\n\n" + getString(R.string.using_paywell_des)
                + "\n" + getString(R.string.hotline_des) + " " + hotline);
        AlertDialog.Builder builder = new AlertDialog.Builder(InquiryActivity.this);
        builder.setTitle("Result " + status);
        builder.setMessage(reqStrBuilder.toString());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InquiryActivity.this, QubeeMainActivity.class);
        startActivity(intent);
        finish();
    }
}

