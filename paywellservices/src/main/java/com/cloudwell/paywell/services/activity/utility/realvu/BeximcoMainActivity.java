package com.cloudwell.paywell.services.activity.utility.realvu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;
import com.cloudwell.paywell.services.utils.TelephonyInfo;
import com.cloudwell.paywell.services.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by android on 4/10/2016.
 */
public class BeximcoMainActivity extends AppCompatActivity {
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_DATA = "data";
    private static final String TAG_POSTED_ON = "posted_on";
    private static final String TAG_PAYMENT_NUMBER = "payment_number";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_RETAILER_COMMISSION = "retailer_commission";
    private static final int REQUEST_PHONE_STATE = 1;
    private ConnectionDetector cd;
    private EditText mPin;
    private EditText mAccountNo;
    private EditText mAmount;
    private LinearLayout mLinearLayout;
    private String pin;
    private String account;
    private String amount;
    private String mImeiNo;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beximco_main);
        getSupportActionBar().setTitle(R.string.home_utility_beximco);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mPin = (EditText) findViewById(R.id.etPinNo);
        mAccountNo = (EditText) findViewById(R.id.etAccountNo);
        mAmount = (EditText) findViewById(R.id.etBillAmount);

        ((TextView) mLinearLayout.findViewById(R.id.tvPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((EditText) mLinearLayout.findViewById(R.id.etPinNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvAccount)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((EditText) mLinearLayout.findViewById(R.id.etAccountNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvBillAmount)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((EditText) mLinearLayout.findViewById(R.id.etBillAmount)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mLinearLayout.findViewById(R.id.btnConfirm)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        // Check if the Phone permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Phone permission has not been granted.
            requestPhonePermission();
        } else {
            // Phone permissions is already available.
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
            mImeiNo = telephonyInfo.getImeiSIM1();
            mAppHandler.setImeiNo(mImeiNo);
        }
    }

    private void requestPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mLinearLayout, R.string.access_msg,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.okay_btn, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(BeximcoMainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
                        }
                    })
                    .show();
        } else {
            // Phone permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE) {
            // Check if the only required permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Phone permission has been granted
                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
                mImeiNo = telephonyInfo.getImeiSIM1(); //imei_no=a10000289bbb5d///imei no=359254058225400
//                String imeiNoSIM2 = telephonyInfo.getImeiSIM2();
                mAppHandler.setImeiNo(mImeiNo);
            } else {
                Snackbar.make(mLinearLayout, R.string.access_msg,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.okay_btn, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(BeximcoMainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
                            }
                        })
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onSubmitButtonClick(View v) {
        pin = mPin.getText().toString().trim();
        account = mAccountNo.getText().toString().trim();
        amount = mAmount.getText().toString().trim();
        if (!cd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            if (pin.length() == 0) {
                mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                return;
            }
            if (mAccountNo.length() == 0) {
                mAccountNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                return;
            }
            if (mAmount.length() == 0) {
                mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                return;
            }
            try {
                new SubmitAsync().execute(getString(R.string.beximco_url),
                        "/" + mAppHandler.getImeiNo(),
                        "/" + URLEncoder.encode(pin, "UTF-8"),
                        "/" + URLEncoder.encode(account, "UTF-8"),
                        "/" + URLEncoder.encode(amount, "UTF-8"),
                        "/" + "json");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private class SubmitAsync extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BeximcoMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4] + params[5];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                String message = jsonObject.getString(TAG_MESSAGE);

                if (status.equalsIgnoreCase("200")) {
                    JSONObject jo = jsonObject.getJSONObject(TAG_DATA);
                    String postedDate = jo.getString(TAG_POSTED_ON);
                    String paymentNumber = jo.getString(TAG_PAYMENT_NUMBER);
                    String amount = jo.getString(TAG_AMOUNT);
                    String retailerCommission = jo.getString(TAG_RETAILER_COMMISSION);
                    AlertDialog.Builder builder = new AlertDialog.Builder(BeximcoMainActivity.this);
                    builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
                    builder.setMessage(getString(R.string.date_des) + " " + postedDate + "\n"
                            + getString(R.string.trx_id_des) + " " + paymentNumber
                            + "\n" + getString(R.string.amount_des) + " " + amount
                            + "\n" + getString(R.string.commission_des) + " " + retailerCommission + getString(R.string.tk));
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
                    Snackbar snackbar = Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG);
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
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BeximcoMainActivity.this, UtilityMainActivity.class);
        startActivity(intent);
        finish();
    }
}
