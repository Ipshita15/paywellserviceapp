package com.cloudwell.paywell.services.activity.utility.nid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by android on 6/28/2016.
 */
public class NidQueryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_TRX_DETAILS = "trxDetails";
    private ConnectionDetector mCd;
    private RelativeLayout mRelativeLayout;
    private EditText mEtNidOrPhone;
    private Button mConfirm;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nid_query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_passport_status_inquiry_title);
        mCd = new ConnectionDetector(this.getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        mEtNidOrPhone = (EditText) findViewById(R.id.etNidOrPhone);
        mConfirm = (Button) findViewById(R.id.btnNidQueryConfirm);

        ((TextView) mRelativeLayout.findViewById(R.id.tvNidOrPhoneNumber)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtNidOrPhone.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            String nidOrPhoneNo = mEtNidOrPhone.getText().toString().trim();
            if (nidOrPhoneNo.length() == 0) {
                mEtNidOrPhone.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                return;
            }
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                new NidQueryAsync().execute(getResources().getString(R.string.nid_or_passport_query),
                        "username=" + mAppHandler.getImeiNo(),
                        "&nid_number_or_phone_no=" + nidOrPhoneNo,
                        "&format=" + "json");
            }
        }
    }

    private class NidQueryAsync extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(NidQueryActivity.this, "", getString(R.string.loading_msg), true);
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
            if (jsonObject != null) {
                try {
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_TRX_DETAILS);
                        Intent intent = new Intent(NidQueryActivity.this, NidLogActivity.class);
                        intent.putExtra(NidLogActivity.TRX_DETAILS, jsonArray.toString());
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, jsonObject.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NidQueryActivity.this, NidMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
