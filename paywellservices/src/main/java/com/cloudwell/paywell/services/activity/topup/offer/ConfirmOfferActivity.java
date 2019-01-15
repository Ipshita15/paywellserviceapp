package com.cloudwell.paywell.services.activity.topup.offer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.topup.TopUpOperatorMenuActivity;
import com.cloudwell.paywell.services.activity.topup.model.RequestTopup;
import com.cloudwell.paywell.services.activity.topup.model.TopupData;
import com.cloudwell.paywell.services.activity.topup.model.TopupDatum;
import com.cloudwell.paywell.services.activity.topup.model.TopupReposeData;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_offer);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(operatorName);
        }

        mAppHandler = new AppHandler(this);
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
                handleTopupAPIValidation(mPin);
            }
        }
    }

    private void handleTopupAPIValidation(String pin) {
        showProgressDialog();

        List<TopupData> topupDatumList = new ArrayList<>();
        TopupData topupDatum = new TopupData(amount, "prepaid", mPhn, key);
        topupDatumList.add(topupDatum);

        final RequestTopup requestTopup = new RequestTopup();
        requestTopup.setPassword("" + pin);
        requestTopup.setUserName("" + mAppHandler.getImeiNo());

        requestTopup.setTopupData(topupDatumList);

        Call<TopupReposeData> responseBodyCall = ApiUtils.getAPIService().callTopAPI(requestTopup);

        responseBodyCall.enqueue(new Callback<TopupReposeData>() {
            @Override
            public void onResponse(Call<TopupReposeData> call, Response<TopupReposeData> response) {
                dismissProgressDialog();
                Log.d(KEY_TAG, "onResponse:" + response.body());

                showReposeUI(response.body());
            }

            @Override
            public void onFailure(Call<TopupReposeData> call, Throwable t) {
                dismissProgressDialog();
                Log.d(KEY_TAG, "onFailure:");
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });

    }

    private void showReposeUI(TopupReposeData response) {
        StringBuilder receiptBuilder = new StringBuilder();

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOfferActivity.this);

        // check authentication
        TopupData topupData = response.getTopupData().get(0).getTopupData();
        if (topupData == null) {
            showAuthticationError();
        } else {
            showDialog(response, receiptBuilder, builder);
        }
    }

    private void showAuthticationError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOfferActivity.this);
        builder.setMessage(R.string.services_alert_msg);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void showDialog(TopupReposeData response, StringBuilder receiptBuilder, AlertDialog.Builder builder) {
        boolean isTotalRequestSuccess = false;
        for (int i = 0; i < response.getTopupData().size(); i++) {

            TopupDatum topupData = response.getTopupData().get(i);
            if (topupData != null) {
                int number = i + 1;
                receiptBuilder.append(number + ".");


                receiptBuilder.append(getString(R.string.phone_no_des) + " " + topupData.getTopupData().getMsisdn());
                receiptBuilder.append("\n" + getString(R.string.amount_des) + " " + topupData.getTopupData().getAmount() + " " + getString(R.string.tk_des));

                if (topupData.getStatus().toString().startsWith("3")) {

                    receiptBuilder.append("\n" + Html.fromHtml("<font color='#ff0000'>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());

                    isTotalRequestSuccess = false;
                } else {
                    receiptBuilder.append("\n" + Html.fromHtml("<font color='#008000>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());

                    isTotalRequestSuccess = true;
                }
                receiptBuilder.append("\n" + getString(R.string.trx_id_des) + " " + topupData.getTransId());

                receiptBuilder.append("\n\n");

            }

        }

        String mHotLine = response.getHotlineNumber();
        receiptBuilder.append("\n\n" + getString(R.string.using_paywell_des) + "\n" + getString(R.string.hotline_des) + " " + mHotLine);

        if (isTotalRequestSuccess == false) {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        }

        builder.setMessage(receiptBuilder.toString());


        final boolean finalIsTotalRequestSuccess = isTotalRequestSuccess;
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
