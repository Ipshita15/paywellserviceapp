package com.cloudwell.paywell.services.activity.topup.offer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.topup.TopUpOperatorMenuActivity;
import com.cloudwell.paywell.services.activity.topup.model.SingleTopUp.Data;
import com.cloudwell.paywell.services.activity.topup.model.SingleTopUp.RequestSingleTopup;
import com.cloudwell.paywell.services.activity.topup.model.SingleTopUp.SingleTopupResponse;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmOfferActivity extends BaseActivity implements View.OnClickListener {

    private static String KEY_TAG = ConfirmOfferActivity.class.getName();

    private AppHandler mAppHandler;
    public static String amount;
    public static String retCom;
    public static String details;
    public static String operatorName;
    private String mPhn, mPin;
    private EditText editTextPhn, editTextPin;
    private String key;
    private View mRelativeLayout;
    private static String mHotLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_offer);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(operatorName);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        TextView textView = findViewById(R.id.tvDetails);
        textView.setText(getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                + "\n" + getString(R.string.ret_com_des) + " " + retCom + getString(R.string.tk)
                + "\n" + getString(R.string.offer_details_des) + " " + details);

        editTextPhn = findViewById(R.id.etPhn);
        editTextPin = findViewById(R.id.etPin);
        mRelativeLayout = findViewById(R.id.linearLayout);

        Button submitBtn = findViewById(R.id.confirmBtn);
        submitBtn.setOnClickListener(this);


        try {
            key = getIntent().getStringExtra("key");
            Log.d("Key", key);
        } catch (Exception e) {

        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_BUNDLE_OFFER_PAGE);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmBtn) {
            mPhn = editTextPhn.getText().toString().trim();
            mPin = editTextPin.getText().toString().trim();
            if (mPhn.length() != 11) {
                editTextPhn.setError(getString(R.string.phone_no_error_msg));
            }
            if (mPin.isEmpty()) {
                editTextPin.setError(getString(R.string.pin_no_error_msg));
            } else {
                //handleTopupAPIValidation(mPin);
                rechargeForOffer(mPin);
            }
        }
    }


    private void rechargeForOffer(String pin){
        showProgressDialog();

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).getRID());

        RequestSingleTopup singleTopup = new RequestSingleTopup();
        singleTopup.setAmount(Integer.parseInt(amount));
        singleTopup.setConType("prepaid");
        singleTopup.setMsisdn(mPhn);
        singleTopup.setOperator(key);
        singleTopup.setClientTrxId(uniqueKey);
        singleTopup.setUsername(mAppHandler.getUserName());
        singleTopup.setPassword(pin);

        ApiUtils.getAPIServiceV2().callSingleTopUpAPI(singleTopup).enqueue(new Callback<SingleTopupResponse>() {
            @Override
            public void onResponse(Call<SingleTopupResponse> call, Response<SingleTopupResponse> response) {
                dismissProgressDialog();
                if (response.code() == 200){
                    response.body().getData().getStatus();
                    String msg = response.body().getData().getMessage();
                    if (response.body().getData().getStatus()==200){
                        if (response.body().getData().getTopupData() != null){

                            StringBuilder receiptBuilder = new StringBuilder();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOfferActivity.this);

                            SingleTopupResponse singleTopupResponse = response.body();
                            showDialogforsingle(singleTopupResponse,receiptBuilder, builder);


                        }else {

                            //showAuthticationError();
                            showErrorMessagev1(msg);

                    }
                    }else {
                        //showAuthticationError();
                        showErrorMessagev1(msg);
                    }


                }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }
            }

            @Override
            public void onFailure(Call<SingleTopupResponse> call, Throwable t) {

                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });
    }


    private void showDialogforsingle(SingleTopupResponse response, StringBuilder receiptBuilder, AlertDialog.Builder builder) {

        boolean isRequestSuccess;
        Data topupData = null;
        topupData = response.getData();

        if (topupData != null) {
            receiptBuilder.append(getString(R.string.phone_no_des) + " " + topupData.getTopupData().getMsisdn());
            receiptBuilder.append("\n" + getString(R.string.amount_des) + " " + topupData.getTopupData().getAmount() + " " + getString(R.string.tk_des));

            if (topupData.getStatus().toString().equals("200")) {
                receiptBuilder.append("\n" + Html.fromHtml("<font color='#ff0000'>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());
            } else {
                receiptBuilder.append("\n" + Html.fromHtml("<font color='#008000>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());
            }

            receiptBuilder.append("\n" + getString(R.string.trx_id_des) + " " + topupData.getTransId());
            receiptBuilder.append("\n\n");
        }
        //}

        if (response.getData().getStatus()==200){
            isRequestSuccess = true;
        }else {
            isRequestSuccess = false;
        }


        mHotLine = response.getHotlineNumber();
        receiptBuilder.append("\n\n" + getString(R.string.using_paywell_des) + "\n" + getString(R.string.hotline_des) + " " + mHotLine);

        if (isRequestSuccess) {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
        }

        builder.setMessage(receiptBuilder.toString());


        final boolean finalIsTotalRequestSuccess = isRequestSuccess;
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                if (finalIsTotalRequestSuccess) {
                    startActivity(new Intent(ConfirmOfferActivity.this, TopUpOperatorMenuActivity.class));
                    finish();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
