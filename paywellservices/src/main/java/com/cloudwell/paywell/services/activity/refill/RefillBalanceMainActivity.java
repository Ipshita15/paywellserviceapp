package com.cloudwell.paywell.services.activity.refill;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.refill.banking.MFSInfoMainActivity;
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RefillBalanceMainActivity extends AppCompatActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    public static final String _serviceSDA = "SDA/Super Agent";
    public static final String _serviceMFS = "MFS";
    public static final String _serviceBank = "Bank Account";
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_SDA_NAME = "name";
    private static final String TAG_PHONE_NO = "phoneNumber";
    private static final String TAG_MESSAGE = "message";
    private CoordinatorLayout mCoordinateLayout;
    boolean hasSDAService = false;
    boolean hasMFSService = false;
    boolean hasBankService = false;
    String sdaName;
    String sdaPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_balance_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_refill_balance_title);
        initView();
    }

    private void initView() {
        List<String> serviceList = AppLoadingActivity.getRefillService();
        for (int i = 0; i < serviceList.size(); i++) {
            if (_serviceSDA.equalsIgnoreCase(serviceList.get(i))) {
                hasSDAService = true;
            } else if (_serviceMFS.equalsIgnoreCase(serviceList.get(i))) {
                hasMFSService = true;
            } else if (_serviceBank.equalsIgnoreCase(serviceList.get(i))) {
                hasBankService = true;
            }
        }
        mAppHandler = new AppHandler(this);
        mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(this);
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

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnSDA:
                showSDAInformation();
                break;
            case R.id.homeBtnMfs:
                startActivity(new Intent(this, MFSInfoMainActivity.class));
                break;
            case R.id.homeBtnBankTransfer:
                startActivity(new Intent(this, BankTransferMainActivity.class));
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RefillBalanceMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("imei", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                if (status.equalsIgnoreCase("200")) {
                    sdaName = jsonObject.getString(TAG_SDA_NAME);
                    sdaPhone = jsonObject.getString(TAG_PHONE_NO);

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
                    TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);

                } else {
                    String message = jsonObject.getString(TAG_MESSAGE);
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
