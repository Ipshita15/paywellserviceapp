package com.cloudwell.paywell.services.activity.topup;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OperatorMenuActivity extends BaseActivity {

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
        Button btnSkitto = findViewById(R.id.homeBtnSkitto);
        Button btnRobi = findViewById(R.id.homeBtnRb);
        Button btnBanglalink = findViewById(R.id.homeBtnBl);
        Button btnTeletalk = findViewById(R.id.homeBtnTt);
        Button btnAirtel = findViewById(R.id.homeBtnAt);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnGp.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnSkitto.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnRobi.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBanglalink.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnTeletalk.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnAirtel.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnGp.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnSkitto.setTypeface(AppController.getInstance().getAponaLohitFont());
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
//        Intent intent = new Intent(OperatorMenuActivity.this, TopupMainActivity.class);
//        startActivity(intent);
        finish();
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnGp:
                if (mCd.isConnectingToInternet()) {
                    operator = 1;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    showNoInternetConnectionFound();
                }
                break;
            case R.id.homeBtnSkitto:
                if (mCd.isConnectingToInternet()) {
                    operator = 7;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    showNoInternetConnectionFound();
                }
                break;

            case R.id.homeBtnRb:
                if (mCd.isConnectingToInternet()) {
                    operator = 3;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    showNoInternetConnectionFound();
                }
                break;
            case R.id.homeBtnBl:
                if (mCd.isConnectingToInternet()) {
                    operator = 2;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    showNoInternetConnectionFound();
                }
                break;
            case R.id.homeBtnTt:
                if (mCd.isConnectingToInternet()) {
                    operator = 6;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    showNoInternetConnectionFound();
                }
                break;
            case R.id.homeBtnAt:
                if (mCd.isConnectingToInternet()) {
                    operator = 4;
                    new InquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator));
                } else {
                    showNoInternetConnectionFound();
                }
                break;
            default:
                break;
        }
    }

    private void showNoInternetConnectionFound() {
        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();
    }

    private class InquiryAsync extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
           showProgressDialog();
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
           dismissProgressDialog();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equals("200")) {
                        JSONArray array = jsonObject.getJSONArray(TAG_RECHARGE_OFFER);
                        Bundle bundle = new Bundle();
                        bundle.putString("array", array.toString());
                        OperatorType operatorType;

                        switch (operator) {
                            case 1:
                                operatorType = OperatorType.GP;
                                OfferMainActivity.operatorName = getString(R.string.home_gp);
                                startOfferMainActivity(bundle, operatorType);
                                break;

                            case 7:
                                operatorType = OperatorType.Skitto;
                                OfferMainActivity.operatorName = getString(R.string.home_skitto);
                                startOfferMainActivity(bundle, operatorType);
                                break;

                            case 2:
                                operatorType = OperatorType.BANGLALINK;
                                OfferMainActivity.operatorName = getString(R.string.home_bl);
                                startOfferMainActivity(bundle, operatorType);
                                break;
                            case 3:
                                operatorType = OperatorType.ROBI;
                                OfferMainActivity.operatorName = getString(R.string.home_rb);
                                startOfferMainActivity(bundle, operatorType);
                                break;
                            case 4:
                                operatorType = OperatorType.AIRTEL;
                                OfferMainActivity.operatorName = getString(R.string.home_at);
                                startOfferMainActivity(bundle, operatorType);
                                break;


                            case 6:
                                operatorType = OperatorType.TELETALK;
                                OfferMainActivity.operatorName = getString(R.string.home_tt);
                                startOfferMainActivity(bundle, operatorType);

                                break;

                            default:
                                break;

                        }


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

    private void startOfferMainActivity(Bundle bundle, OperatorType operatorType) {
        Intent intent = new Intent(OperatorMenuActivity.this, OfferMainActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("key", operatorType.getText());
        startActivity(intent);
        finish();
    }

}
