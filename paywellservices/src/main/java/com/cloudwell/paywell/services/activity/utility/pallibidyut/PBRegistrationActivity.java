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
import android.text.InputType;
import android.util.Log;
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

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 12/7/2015.
 */
@SuppressWarnings("ALL")
public class PBRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static EditText mPinNumber;
    private static EditText mAccount;
    private static EditText mConfirmAccount;
    private static EditText mCustomerName;
    private static EditText mPhone;
    private Button mSubmitButton;
    private ConnectionDetector cd;
    private static String _pinNo;
    private static String _accountNo;
    private static String _customerName;
    private static String _phone;
    private static String otp;
    private int otp_check = 0;
    private static String dialogHeader;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;

    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_RESPONSE_MESSAGE = "message";
    private static final String TAG_RESPONSE_OTP = "otp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_pb_reg);
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView _pin = (TextView) findViewById(R.id.tvPBPin);
        _pin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPinNumber = (EditText) findViewById(R.id.etPBPin);
        mPinNumber.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _accountNo = (TextView) findViewById(R.id.tvPBAccount);
        _accountNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mAccount = (EditText) findViewById(R.id.etPBAccount);
        mAccount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _accountConfirm = (TextView) findViewById(R.id.tvPBAccountConfirm);
        _accountConfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirmAccount = (EditText) findViewById(R.id.etPBConfirmAccount);
        mConfirmAccount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _customerName = (TextView) findViewById(R.id.tvFullName);
        _customerName.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mCustomerName = (EditText) findViewById(R.id.etFullName);
        mCustomerName.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView _phone = (TextView) findViewById(R.id.tvPhone);
        _phone.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPhone = (EditText) findViewById(R.id.etPhone);
        mPhone.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mSubmitButton = (Button) findViewById(R.id.btnPBConfirm);
        mSubmitButton.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSubmitButton) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                _pinNo = mPinNumber.getText().toString().trim();
                if (_pinNo.length() == 0) {
                    mPinNumber.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                if (mAccount.getText().toString().trim().length() < 8) {
                    mAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_error_msg) + "</font>"));
                    return;
                }
                if (mAccount.getText().toString().trim().matches(mConfirmAccount.getText().toString().trim())) {
                    _accountNo = mAccount.getText().toString().trim();
                } else {
                    mConfirmAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_mismatch_error_msg) + "</font>"));
                    return;
                }
                _customerName = mCustomerName.getText().toString().trim();
                if (_customerName.length() == 0) {
                    mCustomerName.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_full_name_error_msg) + "</font>"));
                    return;
                }
                _phone = mPhone.getText().toString().trim();
                if (_phone.length() < 11) {
                    mPhone.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                    return;
                }
                try {
                    new CheckOTPAsync().execute(getResources().getString(R.string.pb_otp_check));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class CheckOTPAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PBRegistrationActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("imei_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin_code", _pinNo));
                nameValuePairs.add(new BasicNameValuePair("acc_no", _accountNo));
                nameValuePairs.add(new BasicNameValuePair("cust_name", URLEncoder.encode(_customerName, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("cust_phn", _phone));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                if (status.equalsIgnoreCase("200")) {
                    otp = jsonObject.getString(TAG_RESPONSE_OTP);
                    checkOTPValidation();
                } else {
                    String message = jsonObject.getString(TAG_RESPONSE_MESSAGE);
                    Snackbar snackbar = Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkOTPValidation() {
        if (otp_check < 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
            builder.setTitle(getString(R.string.otp_error_msg));

            final EditText otpET = new EditText(PBRegistrationActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            otpET.setLayoutParams(lp);
            otpET.setInputType(InputType.TYPE_CLASS_NUMBER);
            otpET.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(otpET);

            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.dismiss();
                    if (otp.equalsIgnoreCase(otpET.getText().toString())) {
                        try {
                            new SubmitAsync().execute(getResources().getString(R.string.pb_reg),
                                    "imei_no=" + mAppHandler.getImeiNo(),
                                    "&pin_code=" + _pinNo,
                                    "&acc_no=" + _accountNo,
                                    "&cust_phn=" + _phone,
                                    "&cust_name=" + URLEncoder.encode(_customerName, "UTF-8"),
                                    "&otp=" + otpET.getText());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
                        builder.setTitle(getString(R.string.otp_missmatch_error_msg));

                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                                otp_check++;
                                checkOTPValidation();
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
                        alert.setCanceledOnTouchOutside(false);
                    }
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
            alert.setCanceledOnTouchOutside(false);
        } else {
            Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
            otp_check = 0;
        }
    }

    private class SubmitAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PBRegistrationActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;

            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

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
                            dialogHeader = splitArray[1].toString();
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
                + "\n" + getString(R.string.cust_name_des) + " " + _customerName
                + "\n" + getString(R.string.phone_no_des) + " " + _phone);
        AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
        builder.setTitle("Result " + dialogHeader);
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
        Intent intent = new Intent(PBRegistrationActivity.this, PBMainActivity.class);
        startActivity(intent);
        finish();
    }
}
