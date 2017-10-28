package com.cloudwell.paywell.services.activity.payments.rocket;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Naima Gani on 12/19/2016.
 */

public class RocketInquiryActivity extends AppCompatActivity implements View.OnClickListener {

    private AppHandler mAppHandler;
    private ConnectionDetector cd;
    private static EditText etPhnNo;
    private Button mComfirm;
    private static LinearLayout mLinearLayout;
    private static String mPhone;
    LinearLayout llCalendar1, llCalendar2;
    private TextView _tvFromDate;
    private TextView _tvFromHiddenDate;
    private TextView _tvToDate;
    private TextView _tvToHiddenDate;
    private String defaultDate;
    private TextView _tvFromToday;
    private TextView _tvToToday;
    private static final int CODE_FROM_CALENDAR = 1;
    private static final int CODE_TO_CALENDAR = 2;
    String dateFrom = "", dateTo = "";
    public static String[] mPhn = null;
    public static String[] mAmount = null;
    public static String[] mDate = null;
    public static String[] mStatus = null;
    public static String[] mTrx = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_inquiry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_rocket_pay_inquiry_title);
        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        etPhnNo = (EditText) findViewById(R.id.etPhoneNo);
        etPhnNo.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mComfirm = (Button) findViewById(R.id.btnConfirm);
        mComfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mComfirm.setOnClickListener(this);

        llCalendar1 = (LinearLayout) findViewById(R.id.llCalendar1);
        llCalendar2 = (LinearLayout) findViewById(R.id.llCalendar2);
        _tvFromDate = (TextView) findViewById(R.id.tvFromDate);
        _tvFromToday = (TextView) findViewById(R.id.tvFromToday);
        _tvFromHiddenDate = (TextView) findViewById(R.id.tvFromHiddenDate);
        _tvToDate = (TextView) findViewById(R.id.tvToDate);
        _tvToToday = (TextView) findViewById(R.id.tvToToday);
        _tvToHiddenDate = (TextView) findViewById(R.id.tvToHiddenDate);

        llCalendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent();
                intnt.setClass(RocketInquiryActivity.this, DateFromActivity.class);
                startActivityForResult(intnt, CODE_FROM_CALENDAR);
            }
        });

        llCalendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent();
                intnt.setClass(RocketInquiryActivity.this, DateToActivity.class);
                startActivityForResult(intnt, CODE_TO_CALENDAR);
            }
        });

        GregorianCalendar today_month = (GregorianCalendar) GregorianCalendar.getInstance();
        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
        defaultDate = df.format(today_month.getTime());
        populateFromDate(defaultDate);
        _tvFromHiddenDate.setText(defaultDate);
        populateToDate(defaultDate);
        _tvToHiddenDate.setText(defaultDate);
    }

    private void populateFromDate(String date) {
        _tvFromDate.setText(date);
        if (date.equals(defaultDate)) {
            _tvFromToday.setText(R.string.today_msg);
            _tvFromToday.setVisibility(View.VISIBLE);
        } else {
            _tvFromToday.setVisibility(View.GONE);
        }

    }

    private void populateToDate(String date) {
        _tvToDate.setText(date);
        if (date.equals(defaultDate)) {
            _tvToToday.setText(R.string.today_msg);
            _tvToToday.setVisibility(View.VISIBLE);
        } else {
            _tvToToday.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_FROM_CALENDAR) { //from calender
                String calenderDate = data.getStringExtra("result_calender");
                _tvFromHiddenDate.setText(calenderDate);
                populateFromDate(data.getStringExtra("result_calender"));
            } else if (requestCode == CODE_TO_CALENDAR) { //to calender
                String calenderDate = data.getStringExtra("result_calender");
                _tvToHiddenDate.setText(calenderDate);
                populateToDate(data.getStringExtra("result_calender"));
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mComfirm) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                mPhone = etPhnNo.getText().toString().trim();
                if ((_tvFromHiddenDate.getText() == "") || (_tvToHiddenDate.getText() == "")) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.fillup_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                } else {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
                        SimpleDateFormat formatTarget = new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.US);

                        String str_input_form = _tvFromHiddenDate.getText().toString();
                        dateFrom = formatTarget.format(format.parse(str_input_form));

                        String str_input_to = _tvToHiddenDate.getText().toString();
                        dateTo = formatTarget.format(format.parse(str_input_to));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new RocketInquiryActivity.SubmitInquiryDataAsync().execute(getResources().getString(R.string.rocket_inquiry),
                            mAppHandler.getImeiNo(), mPhone, dateFrom, dateTo);
                }

            }
        }
    }

    private class SubmitInquiryDataAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RocketInquiryActivity.this, "", getString(R.string.loading_msg), true);
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("username", data[1]));
                nameValuePairs.add(new BasicNameValuePair("account_no", data[2]));
                nameValuePairs.add(new BasicNameValuePair("date_from", data[3]));
                nameValuePairs.add(new BasicNameValuePair("date_to", data[4]));
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
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        try {
                            JSONObject jsonObj = new JSONObject(result);
                            JSONArray jsonArray = jsonObj.getJSONArray("trxDetails");

                            mPhn = new String[jsonArray.length()];
                            mAmount = new String[jsonArray.length()];
                            mDate = new String[jsonArray.length()];
                            mStatus = new String[jsonArray.length()];
                            mTrx = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                mPhn[i] = "Payment To " + object.getString("customer_account_no");
                                mAmount[i] = "Tk." + object.getString("total_amount");
                                mDate[i] = object.getString("request_datetime");
                                mStatus[i] = object.getString("status_name");
                                mTrx[i] = "Trx. ID: " + object.getString("trx_id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(RocketInquiryActivity.this, DisplayStatement.class);
                        startActivity(intent);

                    } else {
                        String msg = jsonObject.getString("message");

                        Snackbar snackbar = Snackbar.make(mLinearLayout, msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RocketInquiryActivity.this, RocketMainActivity.class);
        startActivity(intent);
        finish();
    }
}