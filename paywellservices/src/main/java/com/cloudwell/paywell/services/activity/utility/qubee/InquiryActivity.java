package com.cloudwell.paywell.services.activity.utility.qubee;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
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
public class InquiryActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText mPin;
    private static EditText mAccountNO;
    private Button mSubmitInquiry;
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;
    private static String status;
    private static String trxId;
    private static String accountNo;
    private static String amount;
    private static String hotline;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_inquiry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_qubee_inq_title);

        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView _pin = (TextView) findViewById(R.id.tvQubeePin2);
        _pin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPin = (EditText) findViewById(R.id.etQubeePin2);
        mPin.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _inq_acc = (TextView) findViewById(R.id.tvQubeeccount2);
        _inq_acc.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAccountNO = (EditText) findViewById(R.id.etQubeeccount2);
        mAccountNO.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mSubmitInquiry = (Button) findViewById(R.id.btnQubeeConfirm2);
        mSubmitInquiry.setTypeface(AppController.getInstance().getRobotoRegularFont());

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
                new SubmitAsync().execute(getResources().getString(R.string.qb_inq),
                        "iemi_no=" + mAppHandler.getImeiNo(),
                        "&account_no=" + _account,
                        "&pin_code=" + _pin,
                        "&wimax=" + AppHandler.KEY_WIMAX);
            }

        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(InquiryActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4]);
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
                        if (splitedArray[0].equalsIgnoreCase("200") || splitedArray[0].equalsIgnoreCase("100")) {
                            status = splitedArray[1];
                            trxId = splitedArray[2];
                            accountNo = splitedArray[3];
                            amount = splitedArray[4];
                            hotline = splitedArray[7];
                            showStatusDialog();
                        } else {
                            Snackbar snackbar = Snackbar.make(mLinearLayout, splitedArray[1].toString(), Snackbar.LENGTH_LONG);
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

