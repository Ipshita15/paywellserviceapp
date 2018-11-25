package com.cloudwell.paywell.services.activity.utility.ivac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
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

public class IvacMainActivity extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;

    private static String TAG_RESPONSE_IVAC_STATUS = "status";
    private static String TAG_RESPONSE_IVAC_MSG = "message";
    private static String TAG_RESPONSE_IVAC_CENTER_DETAILS = "centerDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivac_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_ivac);
        }

        mRelativeLayout = findViewById(R.id.relativeLayout);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);

        Button btnBill = findViewById(R.id.homeBtnBillPay);
        Button btnInquiry = findViewById(R.id.homeBtnInquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBillPay:
                if (cd.isConnectingToInternet()) {
                    new TransactionLogAsync().execute(getResources().getString(R.string.utility_ivac_get_center));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnInquiry:
                startActivity(new Intent(IvacMainActivity.this, IvacFeeInquiryMainActivity.class));
                finish();
                break;
            default:
                break;
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
        Intent intent = new Intent(IvacMainActivity.this, UtilityMainActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("deprecation")
    private class TransactionLogAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(IvacMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                try {
                    JSONObject object = new JSONObject(result);

                    String status = object.getString(TAG_RESPONSE_IVAC_STATUS);
                    String msg = object.getString(TAG_RESPONSE_IVAC_MSG);

                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray(TAG_RESPONSE_IVAC_CENTER_DETAILS);
                        IvacFeePayActivity.TAG_CENTER_DETAILS = jsonArray.toString();
                        startActivity(new Intent(IvacMainActivity.this, IvacFeePayActivity.class));
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception ex) {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        }
    }
}
