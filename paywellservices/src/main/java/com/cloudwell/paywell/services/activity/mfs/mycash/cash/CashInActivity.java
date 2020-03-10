package com.cloudwell.paywell.services.activity.mfs.mycash.cash;

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
import com.cloudwell.paywell.services.activity.mfs.mycash.CashInOutActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestCashIn;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
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

public class CashInActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText etAccount;
    private EditText etAmount;
    private EditText etOTP;
    private Button btnConfirm;
    private String mAccount;
    private String mAmount;
    private String mAgentOTP;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_cash_in);
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MYCASH_CASH_IN);

    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.cashInLinearLayout);
        etAccount = findViewById(R.id.mycash_account);
        etAmount = findViewById(R.id.mycash_amount);
        etOTP = findViewById(R.id.mycash_otp);
        btnConfirm = findViewById(R.id.mycash_confirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashAccount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashAmount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashOTP)).setTypeface(AppController.getInstance().getOxygenLightFont());
            etAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            etAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            etOTP.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashAccount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashAmount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashOTP)).setTypeface(AppController.getInstance().getAponaLohitFont());
            etAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            etAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            etOTP.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        if (!mAppHandler.getMYCashOTP().equals("unknown") && !mAppHandler.getMYCashOTP().equals("null")) {
            etOTP.setText(mAppHandler.getMYCashOTP());
        }

        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            mAccount = etAccount.getText().toString().trim();
            mAmount = etAmount.getText().toString().trim();
            mAgentOTP = etOTP.getText().toString().trim();
            if (mAccount.length() != 12) {
                etAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.mycash_acc_error_msg) + "</font>"));
            } else if (mAmount.length() == 0) {
                etAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
            } else if (mAgentOTP.length() == 0) {
                etOTP.setError(Html.fromHtml("<font color='red'>" + getString(R.string.otp_error_msg) + "</font>"));
            } else {
                submitConfirm();
            }
        }
    }

    private void submitConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            callCashInAPI();
        }
    }

    private void callCashInAPI() {
        showProgressDialog();
        RequestCashIn m = new RequestCashIn();
        m.setUsername(mAppHandler.getAndroidID());
        m.setAgentOTP(mAgentOTP);
        m.setCustomerWallet(mAccount);
        m.setAmount(mAmount);
        m.setPassword(mAppHandler.getPin());
        m.setServiceType("CashIn");
        m.setFormat("json");


        ApiUtils.getAPIServiceV2().cashIn(m).enqueue(new Callback<ResponseBody>() {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(CashInActivity.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(CashInActivity.this);
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
        Intent intent = new Intent(CashInActivity.this, CashInOutActivity.class);
        startActivity(intent);
        finish();
    }
}
