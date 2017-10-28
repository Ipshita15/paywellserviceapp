package com.cloudwell.paywell.services.activity.payments.bkash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by Android on 12/6/2015.
 */
@SuppressWarnings("ALL")
public class BKashInquiryBalanceRequestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String REQUEST_TYPE = "requestType";
    public static final String PW_BALANCE = "pwBalance";
    public static final String BANK_BALANCE = "bankBalance";
    public static final String CASH_BALANCE = "cashBalance";
    private LinearLayout _linearLayout;
    private Button mBtnSubmit;
    private EditText mBkashAmount;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private static String txt;
    private String requestType = null;
    private String bAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_req_balance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);

        Bundle delivered = getIntent().getExtras();
        if (delivered != null && !delivered.isEmpty()) {
            requestType = delivered.getString(REQUEST_TYPE);
        }
        if (requestType.equalsIgnoreCase(PW_BALANCE)) {
            getSupportActionBar().setTitle(R.string.home_bkash_paywell_balance_req_title);
        } else if (requestType.equalsIgnoreCase(BANK_BALANCE)) {
            getSupportActionBar().setTitle(R.string.home_bkash_bank_trns_req_title);
        } else {
            getSupportActionBar().setTitle(R.string.home_bkash_cash_req_title);
        }
        initView();
    }

    private void initView() {
        mBkashAmount = (EditText) findViewById(R.id.etReqBalance);
        mBtnSubmit = (Button) findViewById(R.id.bKashReqConfirm);
        ((TextView) _linearLayout.findViewById(R.id.tvSelectAmount)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBkashAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnSubmit.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSubmit) {
            if (mBkashAmount.getText().toString().matches("^0") || mBkashAmount.getText().toString().matches("^00") || mBkashAmount.getText().toString().length() < 2) {
                // Not allowed
                mBkashAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                return;
            } else {
                bAmount = mBkashAmount.getText().toString().trim();
                ResponsePrompt();

            }
        }
    }

    private void ResponsePrompt() {
        if (!cd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            if (requestType.equalsIgnoreCase(PW_BALANCE)) {
                new ResponseAsync().execute(getResources().getString(R.string.bkash_paymnt_req_paywell),
                        "imei=" + mAppHandler.getImeiNo(),
                        "&pin=" + mAppHandler.getPin(),
                        "&amount=" + bAmount);
            } else if (requestType.equalsIgnoreCase(BANK_BALANCE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BKashInquiryBalanceRequestActivity.this);
                builder.setTitle(R.string.fund_transfer_title_msg);
                builder.setMessage(R.string.alert_transfer_msg);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                        new ResponseAsync().execute(getResources().getString(R.string.bkash_bank_trns),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&pin=" + mAppHandler.getPin(),
                                "&amount=" + bAmount);
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                new ResponseAsync().execute(getResources().getString(R.string.bkash_req_for_cash),
                        "imei=" + mAppHandler.getImeiNo(),
                        "&pin=" + mAppHandler.getPin(),
                        "&amount=" + bAmount);
            }
        }
    }

    private class ResponseAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashInquiryBalanceRequestActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                String[] splitArray = result.split("@");
                txt = String.valueOf(splitArray[1]);
                //displayResponsePrompt();
                AlertDialog.Builder builder = new AlertDialog.Builder(BKashInquiryBalanceRequestActivity.this);
                builder.setTitle("Result");
                builder.setMessage(txt);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(BKashInquiryBalanceRequestActivity.this, BKashBalanceRequestMenuActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                mBkashAmount.setText("");
            } else {
                Snackbar snackbar = Snackbar.make(_linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
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
        Intent intent = new Intent(BKashInquiryBalanceRequestActivity.this, BKashBalanceRequestMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
