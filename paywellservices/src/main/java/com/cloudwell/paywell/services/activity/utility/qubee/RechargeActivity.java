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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.adapter.CustomAdapter;
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
 * Created by Android on 12/8/2015.
 */
@SuppressWarnings("ALL")
public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String POSTPAID = "postpaid";
    private static EditText mPin;
    private static EditText mAccountNo;
    private Button mConfirm;
    private ConnectionDetector cd;
    private static EditText mAmount;
    private static String status;
    private static String trxId;
    private static String accountNo;
    private static String hotline;
    private static String amount;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_recharge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_qubee_recharge);

        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView _pin = (TextView) findViewById(R.id.tvQbeePin);
        _pin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPin = (EditText) findViewById(R.id.etQubeePin);
        mPin.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _qubeeAccNo = (TextView) findViewById(R.id.tvQbeeAccount);
        _qubeeAccNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAccountNo = (EditText) findViewById(R.id.etQubeeAccount);
        mAccountNo.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _amount = (TextView) findViewById(R.id.tvQbeeAmount);
        _amount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAmount = (EditText) findViewById(R.id.etQbeeAmount);
        mAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mConfirm = (Button) findViewById(R.id.btnQubeeConfirm);
        mConfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirm.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == mConfirm) {
            String _pin = mPin.getText().toString().trim();
            accountNo = mAccountNo.getText().toString().trim();
            amount = mAmount.getText().toString().trim();
            if (_pin.length() == 0) {
                mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                return;
            } else if (accountNo.length() < 4) {
                mAccountNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.qubee_acc_error_msg) + "</font></font>"));
                return;
            } else if (amount.length() == 0) {
                mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font></font>"));
                return;
            } else {
                if (!cd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    new SubmitAsync().execute(getResources().getString(R.string.qb_bill_pay),
                            "iemi_no=" + mAppHandler.getImeiNo(),
                            "&account_no=" + accountNo,
                            "&amount=" + amount,
                            "&con_type=" + POSTPAID,
                            "&pin_code=" + _pin,
                            "&wimax=" + AppHandler.KEY_WIMAX);
                }
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RechargeActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (ClientProtocolException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
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
                        if (splitedArray[0].equalsIgnoreCase("100")) {
                            status = splitedArray[1];
                            trxId = splitedArray[2];
                            accountNo = splitedArray[3];
                            hotline = splitedArray[6];
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
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
        builder.setTitle("Result " + status);
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
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
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
        Intent intent = new Intent(RechargeActivity.this, QubeeMainActivity.class);
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
