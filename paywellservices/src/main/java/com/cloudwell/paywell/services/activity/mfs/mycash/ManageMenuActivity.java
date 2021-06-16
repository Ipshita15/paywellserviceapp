package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestBalanceTransferConfirm;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.BalanceTransferRequestActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.MYCashToPayWellActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.PayWellToMYCashActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.PaymentConfirmationActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMenuActivity extends BaseActivity {

    private ConnectionDetector mCd;
    private CoordinatorLayout mCoordinateLayout;
    private AppHandler mAppHandler;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycash_balance_transfer_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_fund_management_title);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mCoordinateLayout = findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(AppController.getContext());

        Button btnFromPw = findViewById(R.id.homeBtnFromPayWell);
        Button btnFromMycash = findViewById(R.id.homeBtnFromMYCash);
        Button btnForBank = findViewById(R.id.homeBtnMYCashBankTransfer);
        Button btnForCash = findViewById(R.id.homeBtnMYCashCashTransfer);
        Button btnConfirm = findViewById(R.id.homeBtnConfirmation);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            setMargins(btnForCash, 0, -15, 0, 0);
            btnFromPw.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnFromMycash.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnForBank.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnForCash.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnFromPw.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnFromMycash.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnForBank.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnForCash.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnFromPayWell:
                Intent intent_from_paywell = new Intent(ManageMenuActivity.this, PayWellToMYCashActivity.class);
                startActivity(intent_from_paywell);
                finish();
                break;
            case R.id.homeBtnFromMYCash:
                Intent intent_from_mycash = new Intent(ManageMenuActivity.this, MYCashToPayWellActivity.class);
                startActivity(intent_from_mycash);
                finish();
                break;
            case R.id.homeBtnMYCashBankTransfer:
                Intent intent_bank_transfer = new Intent(ManageMenuActivity.this, BalanceTransferRequestActivity.class);
                intent_bank_transfer.putExtra(BalanceTransferRequestActivity.REQUEST_TYPE, BalanceTransferRequestActivity.BANK_TRANSFER);
                startActivity(intent_bank_transfer);
                finish();
                break;
            case R.id.homeBtnMYCashCashTransfer:
                Intent intent_cash_request = new Intent(ManageMenuActivity.this, BalanceTransferRequestActivity.class);
                intent_cash_request.putExtra(BalanceTransferRequestActivity.REQUEST_TYPE, BalanceTransferRequestActivity.CASH_TRANSFER);
                startActivity(intent_cash_request);
                finish();
                break;
            case R.id.homeBtnConfirmation:
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(ManageMenuActivity.this.getSupportFragmentManager());
                } else {
                    requestBalanceTransferConfirm();
                }
                break;
            default:
                break;
        }
    }

    private void requestBalanceTransferConfirm() {
        showProgressDialog();
        RequestBalanceTransferConfirm m = new RequestBalanceTransferConfirm();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());


        ApiUtils.getAPIServiceV2().checkMyCashPendingCashRequest(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString(TAG_STATUS);
                        if (status.equals("200")) {
                            JSONArray array = jsonObject.getJSONArray(TAG_MESSAGE);
                            Intent intent = new Intent(ManageMenuActivity.this, PaymentConfirmationActivity.class);
                            intent.putExtra(PaymentConfirmationActivity.JSON_RESPONSE, array.toString());
                            startActivity(intent);
                        } else {
                            String msg = jsonObject.getString(TAG_MESSAGE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ManageMenuActivity.this);
                            builder.setMessage(msg);
                            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.setCanceledOnTouchOutside(true);
                            alert.show();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(ManageMenuActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
