package com.cloudwell.paywell.services.activity.mfs.mycash;

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
import android.view.MenuItem;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.BalanceTransferRequestActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.MYCashToPayWellActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.PayWellToMYCashActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.manage.PaymentConfirmationActivity;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ipshita on 2/18/2017.
 */

public class ManageMenuActivity extends AppCompatActivity {

    private ConnectionDetector mCd;
    private CoordinatorLayout mCoordinateLayout;
    private AppHandler mAppHandler;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycash_balance_transfer_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_fund_management_title);
        mAppHandler = new AppHandler(this);
        mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(this);
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ManageMenuActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();

        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
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
            } catch (JSONException e) {
                e.printStackTrace();
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
