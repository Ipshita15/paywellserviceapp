package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.LanguagesBaseActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.REBNotification;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

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

import androidx.appcompat.app.AlertDialog;

public class PBBillPayOldActivity extends LanguagesBaseActivity implements View.OnClickListener {

    private LinearLayout mLinearLayout;
    private EditText mPin, mBillNo, mAmount;
    private Button mComfirm;
    private ConnectionDetector cd;
    private String billNo, status, trxId, amount, tbpsCharge, totalAmount, hotline;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbbill_pay_old);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_pb_billpay_title);
        }
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_PAY);

    }



    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        TextView _pin = findViewById(R.id.tvPBPin2);
        TextView _billNo = findViewById(R.id.tvPBBillNo);
        TextView _amount = findViewById(R.id.tvPBBillAmount);

        mPin = findViewById(R.id.etPBPin2);
        mBillNo = findViewById(R.id.etPBBIllNo);
        mAmount = findViewById(R.id.etPBBillAmount);
        mComfirm = findViewById(R.id.btnPBBillConfirm);

        mComfirm.setOnClickListener(this);


        try {
            String data = getIntent().getStringExtra("REBNotification");
            if (!data.equals("")){
                Gson gson = new Gson();
                REBNotification notificationDetailMessage = gson.fromJson(data, REBNotification.class);
                mBillNo.setText(""+notificationDetailMessage.getTrxData().getBillNo());
                mAmount.setText(""+notificationDetailMessage.getTrxData().getBillAmount());
                mComfirm.setText(""+getString(R.string.re_submit_reb));
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        if (v == mComfirm) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                String _pin = mPin.getText().toString().trim();
                if (_pin.length() == 0) {
                    mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                billNo = mBillNo.getText().toString().trim();
                if (billNo.length() < 8) {
                    mBillNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_error_msg) + "</font>"));
                    return;
                }
                String _amount = mAmount.getText().toString().trim();
                if (_amount.length() < 1) {
                    mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }
                new SubmitAsync().execute(getString(R.string.pb_bill_pay), _pin, billNo, _amount);
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {


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
                String uniqueKey = UniqueKeyGenerator.getUniqueKey();

                List<NameValuePair> nameValuePairs = new ArrayList<>(6);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin_code", params[1]));
                nameValuePairs.add(new BasicNameValuePair("bill_no", params[2]));
                nameValuePairs.add(new BasicNameValuePair("amount", params[3]));
                nameValuePairs.add(new BasicNameValuePair("format", ""));
                nameValuePairs.add(new BasicNameValuePair("ref_id", ""+uniqueKey));
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
                    String splitArray[] = result.split("@");
                    if (splitArray.length > 0) {
                        if (splitArray[0].equalsIgnoreCase("100") || splitArray[0].equalsIgnoreCase("200")) {
                            status = splitArray[1];
                            trxId = splitArray[2];
                            amount = splitArray[3];
                            tbpsCharge = splitArray[4];
                            totalAmount = splitArray[5];
                            hotline = splitArray[8];
                            showStatusDialog();
                        } else {
                            status = splitArray[1];
                            trxId = splitArray[2];
                            amount = splitArray[3];
                            tbpsCharge = splitArray[4];
                            totalAmount = splitArray[5];
                            hotline = splitArray[8];
                            showStatusDialogError();
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
        reqStrBuilder.append(getString(R.string.bill_no_des) + " " + billNo
                + "\n" + getString(R.string.amount_des) + " " + amount
                + "\n" + getString(R.string.tbps_charge_des) + " " + tbpsCharge
                + "\n" + getString(R.string.total_des) + " " + totalAmount
                + "\n" + getString(R.string.trx_id_des) + " " + trxId
                + "\n\n" + getString(R.string.using_paywell_des)
                + "\n" + getString(R.string.hotline_des) + " " + hotline);
        AlertDialog.Builder builder = new AlertDialog.Builder(PBBillPayOldActivity.this);
        builder.setTitle("Result :" + status);
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

    private void showStatusDialogError() {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.bill_no_des) + " " + billNo
                + "\n" + getString(R.string.amount_des) + " " + amount
                + "\n" + getString(R.string.tbps_charge_des) + " " + tbpsCharge
                + "\n" + getString(R.string.total_des) + " " + totalAmount
                + "\n" + getString(R.string.trx_id_des) + " " + trxId
                + "\n\n" + getString(R.string.using_paywell_des)
                + "\n" + getString(R.string.hotline_des) + " " + hotline);
        AlertDialog.Builder builder = new AlertDialog.Builder(PBBillPayOldActivity.this);
        builder.setTitle("Result :" + status);
        builder.setMessage(reqStrBuilder.toString());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
        finish();
    }
}
