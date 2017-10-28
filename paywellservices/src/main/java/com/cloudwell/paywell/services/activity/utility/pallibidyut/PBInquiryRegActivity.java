package com.cloudwell.paywell.services.activity.utility.pallibidyut;

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
public class PBInquiryRegActivity extends AppCompatActivity implements View.OnClickListener {

    private static EditText mPin;
    private static EditText mAccount;
    private Button mInquireComfirm;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private String _pin;
    private static String _accountNo;
    private static String _dialogHeader;
    private static String _custName;
    private static String _phone;
    private static String _custId;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_inquiry_reg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_pb_reg_inq_tilte);

        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView _pin = (TextView) findViewById(R.id.tvPBPin3);
        _pin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPin = (EditText) findViewById(R.id.etPBPin3);
        mPin.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _accountNo = (TextView) findViewById(R.id.tvPBAccount2);
        _accountNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAccount = (EditText) findViewById(R.id.etPBAccount2);
        mAccount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mInquireComfirm = (Button) findViewById(R.id.btnPBConfirm2);
        mInquireComfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mInquireComfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mInquireComfirm) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                _pin = mPin.getText().toString().trim();
                if (_pin.length() == 0) {
                    mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                _accountNo = mAccount.getText().toString().trim();
                if (_accountNo.length() < 8) {
                    mAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_error_msg) + "</font>"));
                    return;
                }
                new SubmitAsync().execute(getResources().getString(R.string.pb_inq_reg),
                        "imei_no=" + mAppHandler.getImeiNo(),
                        "&pin_code=" + _pin, "&acc_no=" + _accountNo);
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PBInquiryRegActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
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
                    if (splitArray.length > 0) {
                        if (splitArray[0].equalsIgnoreCase("100")) {
                            _dialogHeader = splitArray[1].toString();
                            _custName = splitArray[3].toString();
                            _phone = splitArray[4].toString();
                            _custId = splitArray[5].toString();
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

    private void showStatusDialog() {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + _accountNo
                + "\n" + getString(R.string.cust_name_des) + " " + _custName
                + "\n" + getString(R.string.phone_no_des) + _phone
                + "\n" + getString(R.string.trx_id_des) + _custId);

        AlertDialog.Builder builder = new AlertDialog.Builder(PBInquiryRegActivity.this);
        builder.setTitle("Result " + _dialogHeader);
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
        Intent intent = new Intent(PBInquiryRegActivity.this, PBMainActivity.class);
        startActivity(intent);
        finish();
    }
}
