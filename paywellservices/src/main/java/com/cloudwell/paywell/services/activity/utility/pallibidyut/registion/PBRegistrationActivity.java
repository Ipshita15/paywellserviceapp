package com.cloudwell.paywell.services.activity.utility.pallibidyut.registion;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.REBNotification;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class PBRegistrationActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPinNumber, mAccount, mConfirmAccount, mCustomerName, mPhone;
    private Button mSubmitButton;
    ImageView imageViewInfo;
    private ConnectionDetector cd;
    private String _pinNo, _accountNo, _customerName, _phone, dialogHeader;
    private int otp_check = 0;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private String transitionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_registration);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_pb_reg_title);
        }
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION_INQUIRY);

    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        TextView _pin = findViewById(R.id.tvPBPin);
        TextView _accountNo = findViewById(R.id.tvPBAccount);
        TextView _accountConfirm = findViewById(R.id.tvPBAccountConfirm);
        TextView _customerName = findViewById(R.id.tvFullName);
        TextView _phone = findViewById(R.id.tvPhone);

        mPinNumber = findViewById(R.id.etPBPin);
        mAccount = findViewById(R.id.etPBAccount);
        mConfirmAccount = findViewById(R.id.etPBConfirmAccount);
        mCustomerName = findViewById(R.id.etFullName);
        mPhone = findViewById(R.id.etPhone);
        mSubmitButton = findViewById(R.id.btnPBConfirm);

        imageViewInfo = findViewById(R.id.imageView_info);
        imageViewInfo.setOnClickListener(this);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPinNumber.setTypeface(AppController.getInstance().getOxygenLightFont());
            _accountNo.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _accountConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirmAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _customerName.setTypeface(AppController.getInstance().getOxygenLightFont());
            mCustomerName.setTypeface(AppController.getInstance().getOxygenLightFont());
            _phone.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPhone.setTypeface(AppController.getInstance().getOxygenLightFont());
            mSubmitButton.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPinNumber.setTypeface(AppController.getInstance().getAponaLohitFont());
            _accountNo.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _accountConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirmAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _customerName.setTypeface(AppController.getInstance().getAponaLohitFont());
            mCustomerName.setTypeface(AppController.getInstance().getAponaLohitFont());
            _phone.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPhone.setTypeface(AppController.getInstance().getAponaLohitFont());
            mSubmitButton.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mSubmitButton.setOnClickListener(this);


        try {
            String data = getIntent().getStringExtra("REBNotification");
            if (!data.equals("")) {
                Gson gson = new Gson();
                REBNotification notificationDetailMessage = gson.fromJson(data, REBNotification.class);
                mAccount.setText("" + notificationDetailMessage.getTrxData().getAccountNo());
                mConfirmAccount.setText("" + notificationDetailMessage.getTrxData().getAccountNo());
                mCustomerName.setText("" + notificationDetailMessage.getTrxData().getCustomerName());
                mPhone.setText("" + notificationDetailMessage.getTrxData().getCustomerPhone());
                mSubmitButton.setText("" + getString(R.string.re_submit_reb));
            }
        } catch (Exception e) {

        }
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
                new CheckOTPAsync().execute(getString(R.string.pb_reg));
            }
        } else if (v.getId() == R.id.imageView_info) {
            showBillImageGobal(R.drawable.ic_polli_biddut_sms_acc_no);

        }
    }

    private class CheckOTPAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(5);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin_code", _pinNo));
                nameValuePairs.add(new BasicNameValuePair("acc_no", _accountNo));
                nameValuePairs.add(new BasicNameValuePair("cust_name", URLEncoder.encode(_customerName, "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("cust_phn", _phone));
                nameValuePairs.add(new BasicNameValuePair("format", ""));
                nameValuePairs.add(new BasicNameValuePair(ParameterUtility.KEY_REF_ID, uniqueKey));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
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
                        transitionID = splitArray[2].toString();
                        if (splitArray[0].equalsIgnoreCase("100")) {
                            checkOTPValidation();
                        } else {
                            showStatusDialogForRegtionSuccessfull(splitArray[1]);
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

    private void showStatusDialogForRegtionSuccessfull(String message) {

        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + _accountNo
                + "\n" + getString(R.string.cust_name_des) + " " + _customerName
                + "\n" + getString(R.string.phone_no_des) + " " + _phone);
        AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
        builder.setTitle("Result :" + message);
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

    private void checkOTPValidation() {
        if (otp_check < 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
            builder.setTitle(getString(R.string.cust_pin_msg));

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
                    if (!otpET.getText().toString().equalsIgnoreCase("")) {
                        new SubmitAsync().execute(getResources().getString(R.string.pb_reg_with_otp), otpET.getText().toString());
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
                        builder.setTitle(getString(R.string.cust_pin_msg));

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


        @Override
        protected void onPreExecute() {
            showStatusDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin_code", _pinNo));
                nameValuePairs.add(new BasicNameValuePair("trx_id", transitionID));
                nameValuePairs.add(new BasicNameValuePair("reb_pin", params[1]));
                nameValuePairs.add(new BasicNameValuePair("format", ""));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
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
