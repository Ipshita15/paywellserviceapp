package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestLCustomerReg;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRegistrationActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText etPhn;
    private EditText etSerial;
    private EditText etPin;
    private Button btnConfirm;
    private String mPhone;
    private String mSerial;
    private String mPin;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_customer_registration_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();
    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.registrationLinearLayout);
        etPhn = findViewById(R.id.mycash_phone);
        etSerial = findViewById(R.id.mycash_serial);
        etPin = findViewById(R.id.mycash_pin);
        btnConfirm = findViewById(R.id.mycash_confirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashPhone)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashSerial)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
            etPhn.setTypeface(AppController.getInstance().getOxygenLightFont());
            etSerial.setTypeface(AppController.getInstance().getOxygenLightFont());
            etPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashPhone)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashSerial)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashPin)).setTypeface(AppController.getInstance().getAponaLohitFont());
            etPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            etSerial.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            mPhone = etPhn.getText().toString().trim();
            mSerial = etSerial.getText().toString().trim();
            mPin = etPin.getText().toString().trim();
            if (mPhone.length() != 11) {
                etPhn.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            } else if (mSerial.length() == 0) {
                etSerial.setError(Html.fromHtml("<font color='red'>" + getString(R.string.serial_no_error_msg) + "</font>"));
            } else if (mPin.length() == 0) {
                etPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.mycash_pin_error_msg) + "</font>"));
            } else {
                submitConfirm();
            }
        }
    }

    private void submitConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
//            new SubmitAsync().execute(getResources().getString(R.string.mycash_customer_reg));
            callCustomerReg();

        }
    }

    private void callCustomerReg() {


        showProgressDialog();
        RequestLCustomerReg m = new RequestLCustomerReg();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());
        m.setFrmserial(mSerial);
        m.setCustomerMobileNo(mPhone);
        m.setMyCashPin(mPin);
        m.setServiceType("Customer_Registration");



        ApiUtils.getAPIServiceV2().customerRegistration(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();

                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString(TAG_STATUS);
                        if (status.equals("200")) {
                            String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                            String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegistrationActivity.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegistrationActivity.this);
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

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });
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
        Intent intent = new Intent(CustomerRegistrationActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
