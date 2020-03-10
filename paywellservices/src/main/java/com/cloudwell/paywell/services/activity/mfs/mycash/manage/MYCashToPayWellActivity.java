package com.cloudwell.paywell.services.activity.mfs.mycash.manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.cloudwell.paywell.services.activity.mfs.mycash.ManageMenuActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestFundManagment;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MYCashToPayWellActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText etAmount;
    private Button btnConfirm;
    private String mTrxType = "M2P";
    private String mAmount;
    private EditText etOTP;
    private String mAgentOTP;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycash_to_paywell);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_mycash_to_paywell_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MYCASH_MYCASH_TO_PAYWELL);

    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.fundManageLinearLayout);
        etAmount = findViewById(R.id.mycash_amount);
        etOTP = findViewById(R.id.mycash_otp);
        btnConfirm = findViewById(R.id.mycash_confirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashAmount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashOTP)).setTypeface(AppController.getInstance().getOxygenLightFont());
            etAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            etOTP.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashAmount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvMyCashOTP)).setTypeface(AppController.getInstance().getAponaLohitFont());
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
            mAmount = etAmount.getText().toString().trim();
            mAgentOTP = etOTP.getText().toString().trim();
            if (mAmount.length() == 0) {
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
            //new SubmitAsync().execute(getResources().getString(R.string.mycash_management));

            requestMyCashManagement();

        }
    }

    private void requestMyCashManagement() {

        showProgressDialog();
        RequestFundManagment m = new RequestFundManagment();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());
        m.setAgentOTP(mAgentOTP);
        m.setAmount(mAmount);
        m.setTrxType(mTrxType);
        m.setServiceType("Fund_Management");



        ApiUtils.getAPIServiceV2().fundManagement(m).enqueue(new Callback<ResponseBody>() {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(MYCashToPayWellActivity.this);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(MYCashToPayWellActivity.this);
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
        Intent intent = new Intent(MYCashToPayWellActivity.this, ManageMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
