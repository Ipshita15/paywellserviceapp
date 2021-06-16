package com.cloudwell.paywell.services.activity.refill;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.bank_info_update.BankSelectionActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.card.CardTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.model.RequestSDAInfo;
import com.cloudwell.paywell.services.activity.refill.nagad.NagadMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefillBalanceMainActivity extends BaseActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_SDA_NAME = "name";
    private static final String TAG_PHONE_NO = "phoneNumber";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_INFORMATION = "information";
    private CoordinatorLayout mCoordinateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_balance_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_refill_balance_title);
        }
        initView();
    }

    private void initView() {
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mCoordinateLayout = findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(AppController.getContext());

        Button btnSda = findViewById(R.id.homeBtnSDA);
        Button btnBank = findViewById(R.id.homeBtnBankTransfer);
        Button btnCard = findViewById(R.id.homeBtnCard);
        Button btnNagad = findViewById(R.id.homeNagad);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnSda.setTypeface(AppController.getInstance().getOxygenLightFont());

            btnBank.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnCard.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnNagad.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnSda.setTypeface(AppController.getInstance().getAponaLohitFont());

            btnBank.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnCard.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnNagad.setTypeface(AppController.getInstance().getAponaLohitFont());
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
        Intent intent = new Intent(RefillBalanceMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnSDA:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_BALANCE_REFILL_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_SDA_INFO_MENU);
                showSDAInformation();
                break;
            case R.id.homeBtnBankTransfer:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_BALANCE_REFILL_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_BANK_TRANSFER_INFO_MENU);
                startActivity(new Intent(this, BankTransferMainActivity.class));
                break;
            case R.id.homeBtnCard:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_BALANCE_REFILL_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_CARD_MENU);
                startActivity(new Intent(this, CardTransferMainActivity.class));
                break;

            case R.id.homeNagad:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_BALANCE_REFILL_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_NAGAD_MENU);
                startActivity(new Intent(this, NagadMainActivity.class));
                break;

            case R.id.bank_btn:
                startActivity(new Intent(this, BankSelectionActivity.class));
                break;

            default:
                break;
        }
    }

    public void showSDAInformation() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            callGetSDAInfo();

        }
    }

    private void callGetSDAInfo() {
        showProgressDialog();

        final RequestSDAInfo requestSDAInfo = new RequestSDAInfo();
        requestSDAInfo.setUsername("" + mAppHandler.getUserName());


        Call<ResponseBody> responseBodyCall = ApiUtils.getAPIServiceV2().getRtlrSDAinfo(requestSDAInfo);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();

                try {
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                    if (status.equalsIgnoreCase("200")) {
                        String sdaName = jsonObject.getString(TAG_SDA_NAME);
                        String sdaPhone = jsonObject.getString(TAG_PHONE_NO);

                        AlertDialog.Builder builder = new AlertDialog.Builder(RefillBalanceMainActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage("SDA Name: " + sdaName + "\nPhone No: " + sdaPhone);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        String message = jsonObject.getString(TAG_MESSAGE);
                        showErrorMessagev1(message);
                    }
                } catch (Exception e) {
                    showTryAgainDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                showTryAgainDialog();
            }
        });
    }

}
