package com.cloudwell.paywell.services.activity.utility.qubee;

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

import java.util.ArrayList;
import java.util.List;

public class ComplainAmountActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private EditText mPin, mAccount, mIncorrectAmount, mCorrectAmount;
    private Button mConfirmAmount;
    private String _returnMessage, _trxId;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_complain_amount);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_qubee_wrong_amount_title);
        }
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        TextView _pin = findViewById(R.id.tvQbeePin4);
        TextView _account = findViewById(R.id.tvAccount);
        TextView _incorrectAmount = findViewById(R.id.tvIncorrectAmount);
        TextView _correctAmount = findViewById(R.id.tvCorrectAmount);

        mPin = findViewById(R.id.etQubeePin4);
        mAccount = findViewById(R.id.etAccount);
        mIncorrectAmount = findViewById(R.id.etIncorrectAmount);
        mCorrectAmount = findViewById(R.id.etCorrectAmount);
        mConfirmAmount = findViewById(R.id.btnComplainConfirm2);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            _account.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _incorrectAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mIncorrectAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _correctAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mCorrectAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirmAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _account.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _incorrectAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mIncorrectAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _correctAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mCorrectAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirmAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mConfirmAmount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirmAmount) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                String _pin = mPin.getText().toString().trim();
                if (_pin.length() == 0) {
                    mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                String _account = mAccount.getText().toString().trim();
                if (_account.length() < 4) {
                    mAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font>"));
                    return;
                }
                String _incorrectAmount = mIncorrectAmount.getText().toString().trim();
                if (_incorrectAmount.length() < 1) {
                    mIncorrectAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }
                String _correctAmount = mCorrectAmount.getText().toString().trim();
                if (_correctAmount.length() < 1) {
                    mCorrectAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }
                new SubmitAsync().execute(getString(R.string.qb_com), _pin, _account, _correctAmount, _incorrectAmount);
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ComplainAmountActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
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
                nameValuePairs.add(new BasicNameValuePair("type", AppHandler.KEY_TYPE2));
                nameValuePairs.add(new BasicNameValuePair("account_no", params[2]));
                nameValuePairs.add(new BasicNameValuePair("correct_amount", params[3]));
                nameValuePairs.add(new BasicNameValuePair("wrong_amount", params[4]));
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
                if (result != null && result.contains("@")) {
                    String splitedArray[] = result.split("@");
                    if (splitedArray.length > 0) {
                        if (splitedArray[0].equalsIgnoreCase("200")) {
                            _returnMessage = splitedArray[1];
                            _trxId = splitedArray[2];
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
        reqStrBuilder.append(getString(R.string.message_des) + " " + _returnMessage
                + "\n" + getString(R.string.trx_id_des) + " " + _trxId);
        AlertDialog.Builder builder = new AlertDialog.Builder(ComplainAmountActivity.this);
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
        Intent intent = new Intent(ComplainAmountActivity.this, QubeeMainActivity.class);
        startActivity(intent);
        finish();
    }
}
