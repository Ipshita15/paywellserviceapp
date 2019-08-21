package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber;

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

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.ResBiddyutPhoneNoChange;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
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

public class MobileNumberChangeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLinearLayout;
    private EditText mPin, mAccount, mPhone;
    private Button mConfirm;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_requst_mobile_number_change);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_polli_request_mobile_number_change_title);
        }
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initView();


        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MOBILE_NUMBER_CHANGE);
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);


        mPin = findViewById(R.id.etPBPin2);
        mAccount = findViewById(R.id.etPBAccount);
        mPhone = findViewById(R.id.etPBBIllNo);
        mConfirm = findViewById(R.id.btnPBBillConfirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPhone.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPhone.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                String _pin = mPin.getText().toString().trim();
                if (_pin.length() == 0) {
                    mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                String _account = mAccount.getText().toString().trim();
                if (_account.length() < 8) {
                    mAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_error_msg) + "</font>"));
                    return;
                }

                String _phone = mPhone.getText().toString().trim();
                if (_phone.length() < 11) {
                    mPhone.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                    return;
                }

                new SubmitAsync().execute(getString(R.string.pb_bill_pay_pollyBiddyutPhoneNoChange), _pin, _account, _phone);
            }
        }
    }

    private void showErroMessageFromServer(ResBiddyutPhoneNoChange resBiddyutPhoneNoChange) {
        Snackbar snackbar = Snackbar.make(mLinearLayout, resBiddyutPhoneNoChange.getMessage(), Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();
    }

    private void showStatusDialog(Long status, ResBiddyutPhoneNoChange object) {
        StringBuilder reqStrBuilder = new StringBuilder();

        reqStrBuilder.append(getString(R.string.trx_id_des)).append(" ").append(object.getTrxId());
        reqStrBuilder.append("\n");

        if (status == 100) {
            reqStrBuilder.append("\n").append(getString(R.string.status_des)).append(object.getMessage());
            reqStrBuilder.append("\n");

        } else if (status == 200) {
            reqStrBuilder.append("\n").append(getString(R.string.status_des)).append(" ").append(object.getMessage());
            reqStrBuilder.append("\n").append(getString(R.string.sms)).append(" ").append(object.getSms());
        }
        reqStrBuilder.append("\n");
        reqStrBuilder.append("\n");
        reqStrBuilder.append(getString(R.string.using_paywell_des));

        AlertDialog.Builder builder = new AlertDialog.Builder(MobileNumberChangeActivity.this);
        builder.setTitle("Message");
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

    private class SubmitAsync extends AsyncTask<String, String, String> {


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
                List<NameValuePair> nameValuePairs = new ArrayList<>(6);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pass", params[1]));
                nameValuePairs.add(new BasicNameValuePair("account_no", params[2]));
                nameValuePairs.add(new BasicNameValuePair("msisdn", params[3]));
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
            dismissProgressDialog();

            ResBiddyutPhoneNoChange object = new Gson().fromJson(result, ResBiddyutPhoneNoChange.class);
            if (object.getStatus() == 100) {
                showStatusDialog(object.getStatus(), object);
            } else if (object.getStatus() == 200) {
                showStatusDialog(object.getStatus(), object);
            } else {
                showErroMessageFromServer(object);

            }
        }

    }
}
