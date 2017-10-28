package com.cloudwell.paywell.services.activity.payments.bkash;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;
import com.cloudwell.paywell.services.utils.MyHttpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;


public class BKashMenuActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private AppHandler mAppHandler;
    private ConnectionDetector mCd;
    private static final String TAG_RESPONSE_STATUS = "Status";
    private static final String TAG_MESSAGE = "Message";
    private LinearLayout mLinearLayout;
    String selectedLimit = "";
    RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;


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
        mCd = new ConnectionDetector(this);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView mRID = (TextView) findViewById(R.id.bkashRID);
        mRID.setText(getString(R.string.rid) + " " + mAppHandler.getRID().substring(mAppHandler.getRID().length() - 5, mAppHandler.getRID().length()));
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
                    new BkashBalanceAsync().execute(getResources().getString(R.string.bkash_balance_check),
                            "imei=" + mAppHandler.getImeiNo(),
                            "&pin=" + mAppHandler.getPin());
                }
                break;
            case R.id.homeBtnBkashPurpose:
                new PDAsyncTask().execute(getResources().getString(R.string.bkash_pd_url),
                        "imei=" + mAppHandler.getImeiNo(),
                        "&pin=" + mAppHandler.getPin(),
                        "&format=json");
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
                            "&format=" + "json");
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
        // custom dialog
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setTitle(R.string.log_limit_title_msg);
        dialog.setContentView(R.layout.dialog_trx_limit);

        Button btn_okay = (Button) dialog.findViewById(R.id.buttonOk);

        radioButton_five = (RadioButton) dialog.findViewById(R.id.radio_five);
        radioButton_ten = (RadioButton) dialog.findViewById(R.id.radio_ten);
        radioButton_twenty = (RadioButton) dialog.findViewById(R.id.radio_twenty);
        radioButton_fifty = (RadioButton) dialog.findViewById(R.id.radio_fifty);
        radioButton_hundred = (RadioButton) dialog.findViewById(R.id.radio_hundred);
        radioButton_twoHundred = (RadioButton) dialog.findViewById(R.id.radio_twoHundred);

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
                    new TransactionLogAsync().execute(getResources().getString(R.string.bkash_trx_check),
                            "imei=" + mAppHandler.getImeiNo(),
                            "&pin=" + mAppHandler.getPin(),
                            "&limit=" + limit);
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashMenuActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class PDAsyncTask extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashMenuActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                if (status.equalsIgnoreCase("200")) {
                    Intent intent = new Intent(BKashMenuActivity.this, DeclarePurposeActivity.class);
                    intent.putExtra(DeclarePurposeActivity.JSON_RESPONSE, jsonObject.toString());
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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

    @SuppressWarnings("deprecation")
    private class BkashBalanceAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashMenuActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            HttpGet request = new HttpGet(params[0] + params[1] + params[2]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BKashMenuActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 1000000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 1000000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new MyHttpClient(httpParameters, getApplicationContext());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = httpClient.execute(request, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
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
