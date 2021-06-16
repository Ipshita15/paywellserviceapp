package com.cloudwell.paywell.services.activity.mfs.mycash.manage;

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
import com.cloudwell.paywell.services.activity.mfs.mycash.ManageMenuActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestBalanceTransferRequest;
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

public class BalanceTransferRequestActivity extends BaseActivity implements View.OnClickListener {

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
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _linearLayout = findViewById(R.id.linearLayout);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

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

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MYCASH_BALANCE_TRANSFER_REQUEST);

    }

    private void initializeView() {
        etAmount = findViewById(R.id.etAmount);
        etAgentOTP = findViewById(R.id.etAgentOTP);
        mBtnSubmit = findViewById(R.id.mycash_confirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) _linearLayout.findViewById(R.id.tvAmount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            etAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            etAgentOTP.setTypeface(AppController.getInstance().getOxygenLightFont());
            mBtnSubmit.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) _linearLayout.findViewById(R.id.tvAmount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            etAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            etAgentOTP.setTypeface(AppController.getInstance().getAponaLohitFont());
            mBtnSubmit.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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
            } else if (etAgentOTP.getText().toString().trim().length() == 0) {
                // Not allowed
                etAgentOTP.setError(Html.fromHtml("<font color='red'>" + getString(R.string.otp_error_msg) + "</font>"));
            } else {
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
           // new ResponseAsync().execute(getResources().getString(R.string.mycash_balance_transfer));
            callBalanceTransferRequest();



        }
    }

    private void callBalanceTransferRequest() {
        showProgressDialog();
        RequestBalanceTransferRequest m = new RequestBalanceTransferRequest();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());
        m.setAmount(mAmount);
        m.setAgentOTP(mAgentOTP);
        m.setServiceType(serviceType);

        m.setServiceType("PIN_Change");

        ApiUtils.getAPIServiceV2().myCasyhToCashOrBankTransfer(m).enqueue(new Callback<ResponseBody>() {
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
        Intent intent = new Intent(BalanceTransferRequestActivity.this, ManageMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
