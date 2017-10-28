package com.cloudwell.paywell.services.activity.utility.pallibidyut;

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
 * Created by Android on 12/7/2015.
 */
@SuppressWarnings("ALL")
public class PBBillPayActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText mPin;
    private static EditText mBillNo;
    private static EditText mAmount;
    private Button mComfirm;
    private ConnectionDetector cd;
    private static String billNo;
    private static String status;
    private static String trxId;
    private static String retailerNumber;
    private static String amount;
    private static String tbpsCharge;
    private static String totalAmount;
    private static String outletName;
    private static String outletAddress;
    private static String hotline;
    private static TextView dialogHeader;
    private AppHandler mAppHandler;

    private static LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_billpay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_pb_billpay);
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView _pin = (TextView) findViewById(R.id.tvPBPin2);
        _pin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPin = (EditText) findViewById(R.id.etPBPin2);
        mPin.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _billNo = (TextView) findViewById(R.id.tvPBBillNo);
        _billNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBillNo = (EditText) findViewById(R.id.etPBBIllNo);
        mBillNo.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _amount = (TextView) findViewById(R.id.tvPBBillAmount);
        _amount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAmount = (EditText) findViewById(R.id.etPBBillAmount);
        mAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mComfirm = (Button) findViewById(R.id.btnPBBillConfirm);
        mComfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mComfirm.setOnClickListener(this);
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
                if (_amount.length() < 2) {
                    mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                    return;
                }
                new SubmitAsync().execute(getResources().getString(R.string.pb_bill_pay),
                        "imei_no=" + mAppHandler.getImeiNo(),
                        "&pin_code=" + _pin,
                        "&bill_no=" + billNo,
                        "&amount=" + _amount,
                        "&smsAccountNumber=0",
                        "&coustomerPhoneNumber=0");
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PBBillPayActivity.this, "", getString(R.string.loading_msg), true);
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
                    String splitArray[] = result.split("@");
                    if (splitArray != null && splitArray.length > 0) {
                        if (splitArray[0].equalsIgnoreCase("100")) {
                            status = splitArray[1].toString();
                            trxId = splitArray[2].toString();
                            amount = splitArray[3].toString();
                            tbpsCharge = splitArray[4].toString();
                            totalAmount = splitArray[5].toString();
                            outletName = splitArray[6].toString();
                            outletAddress = splitArray[7].toString();
                            hotline = splitArray[8].toString();
                            retailerNumber = splitArray[9].toString();
                            checkBalance();
                            showStatusDialog();
                        } else {
                            Snackbar snackbar = Snackbar.make(mLinearLayout, splitArray[1].toString(), Snackbar.LENGTH_LONG);
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

    private void clearFields() {
        mPin.setText("");
        mBillNo.setText("");
        mAmount.setText("");
    }

    private void checkBalance() {
        new PayWellBalanceAsync().execute(getResources().getString(R.string.pw_bal),
                "imei_no=" + mAppHandler.getImeiNo());
    }

    private class PayWellBalanceAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PBBillPayActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1]);
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
                    String splitArray[] = result.split("@");
                    if (splitArray != null && splitArray.length > 0) {
                        if ((splitArray[0].equalsIgnoreCase("\r\n200")) || (splitArray[0].equalsIgnoreCase("200"))) {
                            mAppHandler.setPWBalance(splitArray[1].toString());
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
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PBBillPayActivity.this);
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
        Intent intent = new Intent(PBBillPayActivity.this, PBMainActivity.class);
        startActivity(intent);
        finish();
    }
}
