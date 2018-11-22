package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.CashInActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.CashOutActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

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

public class CashInOutActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_MESSAGE = "message";
    String selectedLimit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in_out);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_cash_in_out_title);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());

        Button btnCashIn = findViewById(R.id.homeBtnCashIn);
        Button btnCashOut = findViewById(R.id.homeBtnCashOut);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnCashIn.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnCashOut.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnCashIn.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnCashOut.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnCashIn:
                startActivity(new Intent(this, CashInActivity.class));
                finish();
                break;
            case R.id.homeBtnCashOut:
                checkTrxLimit();
                break;
            default:
                break;
        }
    }

    private void checkTrxLimit() {
        final String[] limitTypes = {"5", "10", "20"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.log_limit_title_msg);
        builder.setSingleChoiceItems(limitTypes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectedLimit = limitTypes[arg1];
            }

        });

        // Button OK
        builder.setPositiveButton(R.string.okay_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (selectedLimit.isEmpty()) {
                            selectedLimit = "5";
                        }
                        int limit = Integer.parseInt(selectedLimit);

                        if (mCd.isConnectingToInternet()) {
                            new CashOutInquiryAsync().execute(getResources().getString(R.string.mycash_cashout_inq), String.valueOf(limit));
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

    private class CashOutInquiryAsync extends AsyncTask<String, Void, String> {


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

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("limit", data[1]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equals("200")) {
                        JSONArray array = jsonObject.getJSONArray(TAG_MESSAGE_TEXT);
                        Bundle bundle = new Bundle();
                        bundle.putString("array", array.toString());
                        Intent intent = new Intent(CashInOutActivity.this, CashOutActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, jsonObject.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(CashInOutActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
