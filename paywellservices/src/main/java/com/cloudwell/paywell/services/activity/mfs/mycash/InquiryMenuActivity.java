package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestTrxInquiry;
import com.cloudwell.paywell.services.activity.mfs.mycash.inquiry.LastTransactionsActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.inquiry.StatementActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryMenuActivity extends BaseActivity {

    private ConnectionDetector mCd;
    private RelativeLayout mRelativeLayout;
    private AppHandler mAppHandler;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE_TEXT = "ResponseDetails";
    private static final String TAG_MESSAGE = "message";
    private String selectedLimit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_inquiry);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mCd = new ConnectionDetector(AppController.getContext());

        Button btnStatement = findViewById(R.id.homeBtnStatement);
        Button btnTrx = findViewById(R.id.homeBtnTransactions);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnStatement.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnTrx.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnStatement.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnTrx.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnStatement:
                startActivity(new Intent(this, StatementActivity.class));
                finish();
                break;
            case R.id.homeBtnTransactions:
                checkTrxLimit();
                break;
            default:
                break;
        }
    }

    private void checkTrxLimit() {
        final String[] limitTypes = {"5", "10", "20"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activity_log_limit_title_msg);
        builder.setSingleChoiceItems(limitTypes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectedLimit = limitTypes[arg1].toString();
            }
        });
        builder.setPositiveButton(R.string.okay_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (selectedLimit.isEmpty()) {
                            selectedLimit = "5";
                        }
                        int limit = Integer.parseInt(selectedLimit);

                        if (mCd.isConnectingToInternet()) {
//                            new TrxInquiryAsync().execute(getResources().getString(R.string.mycash_trx_inq), String.valueOf(limit));

                            callTrxInquiryAsync();
                        } else {
                            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void callTrxInquiryAsync() {
        showProgressDialog();
        RequestTrxInquiry m = new RequestTrxInquiry();
        m.setUsername(mAppHandler.getUserName());
        m.setLimit(selectedLimit);

        ApiUtils.getAPIServiceV2().lastTransactionList(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dismissProgressDialog();

                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString(TAG_STATUS);
                        if (status.equals("200")) {
                            JSONArray array = jsonObject.getJSONArray(TAG_MESSAGE_TEXT);
                            Bundle bundle = new Bundle();
                            bundle.putString("array", array.toString());
                            Intent intent = new Intent(InquiryMenuActivity.this, LastTransactionsActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            showErrorMessagev1(jsonObject.getString(TAG_MESSAGE));
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
        Intent intent = new Intent(InquiryMenuActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
