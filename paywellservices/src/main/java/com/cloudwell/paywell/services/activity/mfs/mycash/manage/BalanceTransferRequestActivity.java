package com.cloudwell.paywell.services.activity.mfs.mycash.manage;

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
import com.cloudwell.paywell.services.activity.mfs.mycash.ManageMenuActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BalanceTransferRequestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String REQUEST_TYPE = "requestType";
    public static final String BANK_TRANSFER = "MyCashToBank";
    public static final String CASH_TRANSFER = "MycashToCash";
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private LinearLayout _linearLayout;
    private Button mBtnSubmit;
    private EditText etAmount;
    private EditText etAgentOTP;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private String requestType = null;
    private String serviceType;
    private String mAmount;
    private String mAgentOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_transfer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);

        Bundle delivered = getIntent().getExtras();
        if (delivered != null && !delivered.isEmpty()) {
            requestType = delivered.getString(REQUEST_TYPE);
        }
        if (requestType.equalsIgnoreCase(BANK_TRANSFER)) {
            getSupportActionBar().setTitle(R.string.home_bkash_bank_trns_req_title);
        } else {
            getSupportActionBar().setTitle(R.string.home_bkash_cash_req_title);
        }
        initializeView();
    }

    private void initializeView() {
        etAmount = (EditText) findViewById(R.id.etAmount);
        etAgentOTP = (EditText) findViewById(R.id.etAgentOTP);
        mBtnSubmit = (Button) findViewById(R.id.mycash_confirm);

        ((TextView) _linearLayout.findViewById(R.id.tvAmount)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        etAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        etAgentOTP.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnSubmit.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnSubmit.setOnClickListener(this);

        if (!mAppHandler.getMYCashOTP().equals("unknown") && !mAppHandler.getMYCashOTP().equals("null")) {
            etAgentOTP.setText(mAppHandler.getMYCashOTP());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (etAmount.getText().toString().trim().length() == 0) {
                // Not allowed
                etAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                return;
            } else if (etAgentOTP.getText().toString().trim().length() == 0) {
                // Not allowed
                etAgentOTP.setError(Html.fromHtml("<font color='red'>" + getString(R.string.otp_error_msg) + "</font>"));
                return;
            }
            else {
                mAmount = etAmount.getText().toString().trim();
                mAgentOTP = etAgentOTP.getText().toString().trim();
                ResponsePrompt();

            }
        }
    }

    private void ResponsePrompt() {
        if (!cd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            if (requestType.equalsIgnoreCase(BANK_TRANSFER)) {
                serviceType = BANK_TRANSFER;
            } else {
                serviceType = CASH_TRANSFER;
            }
            new ResponseAsync().execute(getResources().getString(R.string.mycash_balance_transfer));
        }
    }

    private class ResponseAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BalanceTransferRequestActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("amount", mAmount));
                nameValuePairs.add(new BasicNameValuePair("AgentOTP", mAgentOTP));
                nameValuePairs.add(new BasicNameValuePair("serviceType", serviceType));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);

                    if (status.equals("200")) {
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(BalanceTransferRequestActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage(msg_text + "\nPayWell Trx ID: " + trx_id);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(true);
                        alert.show();
                    } else {
                        String msg = jsonObject.getString(TAG_MESSAGE);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(BalanceTransferRequestActivity.this);
                        builder.setMessage(msg + "\n" + msg_text + "\nPayWell Trx ID: " + trx_id);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(true);
                        alert.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(_linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        ;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BalanceTransferRequestActivity.this, ManageMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
