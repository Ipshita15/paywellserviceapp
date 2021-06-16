package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestGenerateOTP;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateOTPActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText mPin;
    private Button mConfirm;
    private String _pin;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_otp);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_generate_otp_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();
    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.generateOTPLinearLayout);
        mPin = findViewById(R.id.mycash_pin);
        mConfirm = findViewById(R.id.mycash_confirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashPin)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            _pin = mPin.getText().toString().trim();
            if (_pin.length() == 0) {
                mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.mycash_pin_error_msg) + "</font>"));
            } else {
                submitConfirm();
            }
        }
    }

    private void submitConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {

            callGenerateOTP();
        }
    }

    private void callGenerateOTP() {
        showProgressDialog();
        RequestGenerateOTP m = new RequestGenerateOTP();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());
        m.setMyCashPin(_pin);
        m.setServiceType("GenerateOTP");

        ApiUtils.getAPIServiceV2().generateOTPMYCash(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dismissProgressDialog();
                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString(TAG_STATUS);
                        String msg = jsonObject.getString(TAG_MESSAGE);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        if (status.equals("200")) {
                            String otp = msg_text.substring(12, 17);
                            String otp_validation_period = msg_text.substring(41, 50);

                            mAppHandler.setMYCashOTP(otp);

                            AlertDialog.Builder builder = new AlertDialog.Builder(GenerateOTPActivity.this);
                            builder.setTitle(msg);
                            builder.setMessage(msg_text);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(GenerateOTPActivity.this);
                            builder.setTitle("Result");
                            builder.setMessage(msg);
                            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {


                                }
                            });
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.setCanceledOnTouchOutside(true);
                            alert.show();
                        }
                    } else {
                       showTryAgainDialog();
                    }
                } catch (Exception e) {
                   showTryAgainDialog();
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
        Intent intent = new Intent(GenerateOTPActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
