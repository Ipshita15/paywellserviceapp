package com.cloudwell.paywell.services.activity.utility.banglalion;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.model.RechargeResponsePojo;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

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

public class BanglalionRechargeInquiryActivity extends BaseActivity implements View.OnClickListener {


    private EditText mPin;
    private EditText mAccountNO;
    private Button mSubmitInquiry;
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AsyncTask<String, Integer, String> mSubmitAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banglalion_recharge_inquiry);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_qubee_inq_title);
        }
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_BANGLALION_RECHARGE_INQUIRY);
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.banglalionRechargeInquiryLL);
        TextView _pin = findViewById(R.id.tvQubeePin2);
        TextView _inq_acc = findViewById(R.id.tvQubeeccount2);

        mPin = findViewById(R.id.etQubeePin2);
        mAccountNO = findViewById(R.id.etQubeeccount2);
        mSubmitInquiry = findViewById(R.id.btnQubeeConfirm2);


        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            _inq_acc.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAccountNO.setTypeface(AppController.getInstance().getOxygenLightFont());
            mSubmitInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _inq_acc.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccountNO.setTypeface(AppController.getInstance().getAponaLohitFont());
            mSubmitInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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
                    mAccountNO.setError(Html.fromHtml("<font color='red'>" + getString(R.string.banglalion_acc_error_msg) + "</font></font>"));
                    return;
                }
                submitForInquiry(mAppHandler.getUserName(), _account, _pin);
            }
        }
    }

    private void submitForInquiry(String userName, String account, String pin) {
        showProgressDialog();
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(BanglalionRechargeInquiryActivity.this).getRID());

        RechargeResponsePojo pojo = new RechargeResponsePojo();
        pojo.setCustomerID(account);
        pojo.setUserName(userName);
        pojo.setRef_id(uniqueKey);
        pojo.setPassword(pin);

        ApiUtils.getAPIServiceV2().banglalionRechargeInquiry(pojo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();

                if (response.code() == 200){

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");

                        if (status != null && status.equals("200")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (data != null) {
                                String transactionStatus = data.getString("status");
                                String trxId = data.getString("tran_id");
                                String blcTrx = data.getString("BLCTrx");
                                String amount = data.getString("amount");
                                String retailCommission = data.getString("retCommission");
                                String accountNum = data.getString("customerID");
                                String hotLine = data.getString("contact");
                                showStatusDialog(transactionStatus, accountNum, amount, trxId, blcTrx, retailCommission, hotLine);
                            }
                        } else {
                            String message = jsonObject.getString("message");
                           showErrorMessagev1(message);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        showErrorMessagev1(getString(R.string.try_again_msg));
                    }

                }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });

    }

    private void showStatusDialog(String status, String accountNo, String amount, String trxId, String banglalinkTrx, String retailCommission, String hotline) {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + accountNo
                + "\n" + getString(R.string.amount_des) + " " + amount + " " + R.string.tk_des
                + "\n" + getString(R.string.trx_id_des) + " " + trxId
                + "\n" + getString(R.string.banglalion_trx_id_des) + " " + banglalinkTrx
                + "\n" + getString(R.string.retail_commission_des) + " " + retailCommission + " " + R.string.tk_des
                + "\n\n" + getString(R.string.using_paywell_des)
                + "\n" + getString(R.string.hotline_des) + " " + hotline);
        AlertDialog.Builder builder = new AlertDialog.Builder(BanglalionRechargeInquiryActivity.this);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubmitAsync != null) {
            mSubmitAsync.cancel(true);
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
        finish();
    }
}

