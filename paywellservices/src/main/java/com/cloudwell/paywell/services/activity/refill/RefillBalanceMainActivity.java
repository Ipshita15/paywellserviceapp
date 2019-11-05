package com.cloudwell.paywell.services.activity.refill;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.card.CardTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.nagad.NagadMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

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
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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

            default:
                break;
        }
    }

    public void showSDAInformation() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new SDAInformationAsync().execute(
                    getResources().getString(R.string.refill_sda_info), mAppHandler.getImeiNo());
        }
    }

    private class SDAInformationAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("imei", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            dismissProgressDialog();
            try {
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
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, message, Snackbar.LENGTH_LONG);
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

    public void showInformation() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new InformationAsync().execute(
                    getResources().getString(R.string.refill_mfs_info));
        }
    }

    private class InformationAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("serviceName", "bKash"));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                if (status.equalsIgnoreCase("200")) {
                    String serviceName = jsonObject.getString(TAG_MESSAGE);
                    String serviceInfo = jsonObject.getString(TAG_INFORMATION);

                    AlertDialog.Builder builder = new AlertDialog.Builder(RefillBalanceMainActivity.this);
                    builder.setTitle(serviceName);
                    builder.setMessage(serviceInfo);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    String message = jsonObject.getString(TAG_MESSAGE);
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, message, Snackbar.LENGTH_LONG);
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
}
