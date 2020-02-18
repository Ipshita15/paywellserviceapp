package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.BalanceTransferRequestActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.MYCashToPayWellActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.PayWellToMYCashActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.PaymentConfirmationActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
                    new SubmitAsync().execute(getResources().getString(R.string.mycash_balance_transfer_confirm_pending));
                }
                break;
            default:
                break;
        }
    }

    private class SubmitAsync extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getUserName()));
                nameValuePairs.add(new BasicNameValuePair("password", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                nameValuePairs.add(new BasicNameValuePair(ParameterUtility.KEY_REF_ID, uniqueKey));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();

            } catch (IOException e) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            try {
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
