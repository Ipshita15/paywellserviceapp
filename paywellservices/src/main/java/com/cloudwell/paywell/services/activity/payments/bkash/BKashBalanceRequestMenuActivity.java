package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BKashBalanceRequestMenuActivity extends BaseActivity {

    private AppHandler mAppHandler;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_balance_request_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_bkash_balance_req_title);
        }
        mAppHandler = new AppHandler(this);

        mCoordinatorLayout = findViewById(R.id.coordinateLayout);

        Button homePwBalance = findViewById(R.id.homeBtnBkashPWBalance);
        Button homeForBank = findViewById(R.id.homeBtnBkashBankTransfer);
        Button homeForCash = findViewById(R.id.homeBtnBkashForCash);
        Button homeReverse = findViewById(R.id.homeBtnBkashPWBalanceReverse);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            setMargins(homeForCash, 0, -15, 0, 0);

            homePwBalance.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeForBank.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeForCash.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeReverse.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            homePwBalance.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeForBank.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeForCash.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeReverse.setTypeface(AppController.getInstance().getAponaLohitFont());
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
            case R.id.homeBtnBkashPWBalance:
                Intent intent_pw = new Intent(BKashBalanceRequestMenuActivity.this, BKashInquiryBalanceRequestActivity.class);
                intent_pw.putExtra(BKashInquiryBalanceRequestActivity.REQUEST_TYPE, BKashInquiryBalanceRequestActivity.PW_BALANCE);
                startActivity(intent_pw);
                finish();
                break;
            case R.id.homeBtnBkashBankTransfer:
                Intent intent_bank = new Intent(BKashBalanceRequestMenuActivity.this, BKashInquiryBalanceRequestActivity.class);
                intent_bank.putExtra(BKashInquiryBalanceRequestActivity.REQUEST_TYPE, BKashInquiryBalanceRequestActivity.BANK_BALANCE);
                startActivity(intent_bank);
                finish();
                break;
            case R.id.homeBtnBkashForCash:
                Intent intent_cash = new Intent(BKashBalanceRequestMenuActivity.this, BKashInquiryBalanceRequestActivity.class);
                intent_cash.putExtra(BKashInquiryBalanceRequestActivity.REQUEST_TYPE, BKashInquiryBalanceRequestActivity.CASH_BALANCE);
                startActivity(intent_cash);
                finish();
                break;
            case R.id.homeBtnBkashPWBalanceReverse:
                new BkashPWBalanceReverseAsync().execute(getResources().getString(R.string.bkash_reverse_inq_url));
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private class BkashPWBalanceReverseAsync extends AsyncTask<String, Integer, String> {


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
                List<NameValuePair> nameValuePairs = new ArrayList<>(5);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("paymentType", "1"));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String response) {
          dismissProgressDialog();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if(status.equalsIgnoreCase("200")) {
                        JSONArray array = jsonObject.getJSONArray("requestData");
                        ReversePWBalanceActivity.RESPONSE_DATA = array.toString();
                        startActivity(new Intent(BKashBalanceRequestMenuActivity.this, ReversePWBalanceActivity.class));
                        finish();
                    } else {
                        String msg = jsonObject.getString("message");
                        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mCoordinatorLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(BKashBalanceRequestMenuActivity.this, BKashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
