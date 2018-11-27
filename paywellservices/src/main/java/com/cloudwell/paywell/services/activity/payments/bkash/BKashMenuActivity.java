package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

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


public class BKashMenuActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private AppHandler mAppHandler;
    private ConnectionDetector mCd;
    private static final String TAG_RESPONSE_STATUS = "Status";
    private static final String TAG_MESSAGE = "Message";
    private LinearLayout mLinearLayout;
    String selectedLimit = "";
    RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    Button requestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_menu_list);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_payment_bkash_title);
        }
        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());

        mLinearLayout = findViewById(R.id.linearLayout);
        requestBtn = findViewById(R.id.homeBtnBkashRequest);

        TextView mAgent = findViewById(R.id.bkashAgentNo);
        TextView mRID = findViewById(R.id.bkashRID);
        String agentNo = getString(R.string.agent_phn_des) + " " + mAppHandler.getAgentPhnNum();
        String rid = getString(R.string.rid) + " " + mAppHandler.getRID().substring(mAppHandler.getRID().length() - 5, mAppHandler.getRID().length());

        mAgent.setText(agentNo);
        mRID.setText(rid);

        Button btnTrx = findViewById(R.id.homeBtnBkashTrx);
        Button btnBalance = findViewById(R.id.homeBtnBkashBalance);
        Button btnRequest = findViewById(R.id.homeBtnBkashRequest);
        Button btnConfirm = findViewById(R.id.homeBtnBkashPaymentConf);
        Button btnInquiry = findViewById(R.id.homeBtnBkashInquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mAgent.setTypeface(AppController.getInstance().getOxygenLightFont());
            mRID.setTypeface(AppController.getInstance().getOxygenLightFont());

            btnTrx.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBalance.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnRequest.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mAgent.setTypeface(AppController.getInstance().getAponaLohitFont());
            mRID.setTypeface(AppController.getInstance().getAponaLohitFont());

            btnTrx.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBalance.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnRequest.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnBkashTrx:
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    showLimitPrompt();
                }
                break;
            case R.id.homeBtnBkashBalance:
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    new BkashBalanceAsync().execute(getString(R.string.bkash_balance_check));
                }
                break;
            case R.id.homeBtnBkashRequest:
                startActivity(new Intent(BKashMenuActivity.this, BKashBalanceRequestMenuActivity.class));
                finish();
                break;
            case R.id.homeBtnBkashPaymentConf:
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    new PaymentConfirmAsync().execute(getResources().getString(R.string.bkash_paymnt_pending_check),
                            "imei=" + mAppHandler.getImeiNo(),
                            "&pin=" + mAppHandler.getPin(),
                            "&format=" + "json",
                            "&version=" + "new",
                            "&gateway_id=" + mAppHandler.getGatewayId());
                }
                break;
            case R.id.homeBtnBkashInquiry:
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    startActivity(new Intent(BKashMenuActivity.this, BKashTrxClaimActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void showLimitPrompt() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setTitle(R.string.log_limit_title_msg);
        dialog.setContentView(R.layout.dialog_trx_limit);

        Button btn_okay = dialog.findViewById(R.id.buttonOk);

        radioButton_five = dialog.findViewById(R.id.radio_five);
        radioButton_ten = dialog.findViewById(R.id.radio_ten);
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty);
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty);
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred);
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred);

        radioButton_five.setOnCheckedChangeListener(this);
        radioButton_ten.setOnCheckedChangeListener(this);
        radioButton_twenty.setOnCheckedChangeListener(this);
        radioButton_fifty.setOnCheckedChangeListener(this);
        radioButton_hundred.setOnCheckedChangeListener(this);
        radioButton_twoHundred.setOnCheckedChangeListener(this);

        assert btn_okay != null;
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (selectedLimit.isEmpty()) {
                    selectedLimit = "5";
                }
                int limit = Integer.parseInt(selectedLimit);
                if (mCd.isConnectingToInternet()) {
                    new TransactionLogAsync().execute(getString(R.string.bkash_trx_check), "" + limit);
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.radio_five) {
                selectedLimit = "5";
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_ten) {
                selectedLimit = "10";
                radioButton_five.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_twenty) {
                selectedLimit = "20";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_fifty) {
                selectedLimit = "50";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_hundred) {
                selectedLimit = "100";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_twoHundred) {
                selectedLimit = "200";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
            }
        }
    }

    private class PaymentConfirmAsync extends AsyncTask<String, String, JSONObject> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4] + params[5];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            dismissProgressDialog();
            try {
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                String message = jsonObject.getString(TAG_MESSAGE);
                if (status.equalsIgnoreCase("610")) {
                    Intent intent = new Intent(BKashMenuActivity.this, ConfirmPaymentActivity.class);
                    intent.putExtra(ConfirmPaymentActivity.JSON_RESPONSE, jsonObject.toString());
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private class BkashBalanceAsync extends AsyncTask<String, Integer, String> {


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
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
                String[] splitArray = response.split("@");
                String txt = String.valueOf(splitArray[1]);
                if (response.startsWith("200")) {
                    Intent intent = new Intent(BKashMenuActivity.this, BKashBalanceActivity.class);
                    intent.putExtra(BKashBalanceActivity.RESPONSE, response);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, txt, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private class TransactionLogAsync extends AsyncTask<String, Integer, String> {


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
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                nameValuePairs.add(new BasicNameValuePair("limit", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
                String[] splitArray = response.split("@");
                String txt = String.valueOf(splitArray[1]);
                if (response.startsWith("200")) {
                    TransactionStatusActivity.RESPONSE = response;
                    startActivity(new Intent(BKashMenuActivity.this, TransactionStatusActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, txt, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(BKashMenuActivity.this, PaymentsMainActivity.class);
        startActivity(intent);
        finish();
    }
}
