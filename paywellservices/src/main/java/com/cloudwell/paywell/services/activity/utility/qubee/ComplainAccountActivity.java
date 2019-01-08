package com.cloudwell.paywell.services.activity.utility.qubee;

import android.content.DialogInterface;
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

public class ComplainAccountActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLinearLayout;
    private EditText mPin, mIncorrecctAccount, mCorrectAccount, mAmount;
    private Button mConfirm;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private String _returnMessage, _trxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_complain_account);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_qubee_wrong_acc_title);
        }

        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        TextView _pin = findViewById(R.id.tvQbeePin3);
        TextView _incorrectAcc = findViewById(R.id.tvIncorrectAccount);
        TextView _correctAcc = findViewById(R.id.tvCorrectAccount);
        TextView _amount = findViewById(R.id.tvAmount);

        mPin = findViewById(R.id.etQubeePin3);
        mIncorrecctAccount = findViewById(R.id.etIncorrectAccount);
        mCorrectAccount = findViewById(R.id.etCorrectAccount);
        mAmount = findViewById(R.id.etBillAmount);
        mConfirm = findViewById(R.id.btnComplainConfirm);


        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            _incorrectAcc.setTypeface(AppController.getInstance().getOxygenLightFont());
            mIncorrecctAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _correctAcc.setTypeface(AppController.getInstance().getOxygenLightFont());
            mCorrectAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _amount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _incorrectAcc.setTypeface(AppController.getInstance().getAponaLohitFont());
            mIncorrecctAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _correctAcc.setTypeface(AppController.getInstance().getAponaLohitFont());
            mCorrectAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _amount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
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
                String _incorrectAccount = mIncorrecctAccount.getText().toString().trim();
                if (_incorrectAccount.length() < 4) {
                    mIncorrecctAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font>"));
                    return;
                }
                String _correctAccount = mCorrectAccount.getText().toString().trim();
                if (_correctAccount.length() < 4) {
                    mCorrectAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font>"));
                    return;
                }
                String _amount = mAmount.getText().toString().trim();
                if (_amount.length() < 1) {
                    mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }
                new SubmitAsync().execute(getString(R.string.qb_com), _pin, _correctAccount, _incorrectAccount, _amount);
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(7);
                nameValuePairs.add(new BasicNameValuePair("iemi_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin_code", params[1]));
                nameValuePairs.add(new BasicNameValuePair("wimax", AppHandler.KEY_WIMAX));
                nameValuePairs.add(new BasicNameValuePair("type", AppHandler.KEY_TYPE));
                nameValuePairs.add(new BasicNameValuePair("correct_account_no", params[2]));
                nameValuePairs.add(new BasicNameValuePair("wrong_account_no", params[3]));
                nameValuePairs.add(new BasicNameValuePair("amount", params[4]));
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
                        if (splitArray[0].equalsIgnoreCase("200")) {
                            _returnMessage = splitArray[1];
                            _trxId = splitArray[2];
                            showStatusDialog();
                        } else {
                            Snackbar snackbar = Snackbar.make(mLinearLayout, splitArray[1], Snackbar.LENGTH_LONG);
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
        reqStrBuilder.append(getString(R.string.message_des) + " " + _returnMessage
                + "\n" + getString(R.string.trx_id_des) + " " + _trxId);
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplainAccountActivity.this);
        builder.setTitle("Result");
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
}
