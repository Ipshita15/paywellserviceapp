package com.cloudwell.paywell.services.activity.topup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.topup.offer.OfferMainActivity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OperatorMenuActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;
    private ConnectionDetector mCd;
    private static final String TAG_STATUS = "status";
    private static final String TAG_RECHARGE_OFFER = "RechargeOffer";
    private static final String TAG_MESSAGE = "message";
    private int operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_topup_offer_title);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());

        Button btnGp = findViewById(R.id.homeBtnGp);
        Button btnRobi = findViewById(R.id.homeBtnRb);
        Button btnBanglalink = findViewById(R.id.homeBtnBl);
        Button btnTeletalk = findViewById(R.id.homeBtnTt);
        Button btnAirtel = findViewById(R.id.homeBtnAt);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnGp.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnRobi.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBanglalink.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnTeletalk.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnAirtel.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnGp.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnRobi.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBanglalink.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnTeletalk.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnAirtel.setTypeface(AppController.getInstance().getAponaLohitFont());
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
        Intent intent = new Intent(OperatorMenuActivity.this, TopupMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnGp:
                if (mCd.isConnectingToInternet()) {
                    operator = 1;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnRb:
                if (mCd.isConnectingToInternet()) {
                    operator = 3;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnBl:
                if (mCd.isConnectingToInternet()) {
                    operator = 2;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnTt:
                if (mCd.isConnectingToInternet()) {
                    operator = 6;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnAt:
                if (mCd.isConnectingToInternet()) {
                    operator = 4;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            default:
                break;
        }
    }

    private class InquiryAsync extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(OperatorMenuActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("subServiceId", data[1]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
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
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equals("200")) {
                        JSONArray array = jsonObject.getJSONArray(TAG_RECHARGE_OFFER);
                        Bundle bundle = new Bundle();
                        bundle.putString("array", array.toString());
                        if (operator == 1) {
                            OfferMainActivity.operatorName = getString(R.string.home_gp);
                        } else if (operator == 2) {
                            OfferMainActivity.operatorName = getString(R.string.home_bl);
                        } else if (operator == 3) {
                            OfferMainActivity.operatorName = getString(R.string.home_rb);
                        } else if (operator == 4) {
                            OfferMainActivity.operatorName = getString(R.string.home_at);
                        } else if (operator == 6) {
                            OfferMainActivity.operatorName = getString(R.string.home_tt);
                        }
                        Intent intent = new Intent(OperatorMenuActivity.this, OfferMainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else{
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

}
