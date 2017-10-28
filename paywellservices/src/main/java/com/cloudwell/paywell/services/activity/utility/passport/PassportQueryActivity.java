package com.cloudwell.paywell.services.activity.utility.passport;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by android on 6/28/2016.
 */
public class PassportQueryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_TRX_DETAILS = "trxDetails";
    private ConnectionDetector mCd;
    private EditText mEtName;
    private EditText mEtPhone;
    private Button mConfirm;
    private RelativeLayout mRelativeLayout;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport_query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_passport_status_inquiry_title);
        mCd = new ConnectionDetector(this.getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        mEtName = (EditText) findViewById(R.id.etName);
        mEtPhone = (EditText) findViewById(R.id.etPhoneNo);
        mConfirm = (Button) findViewById(R.id.btnPassportQueryConfirm);

        ((TextView) mRelativeLayout.findViewById(R.id.tvFirstName)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtName.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mRelativeLayout.findViewById(R.id.tvPhoneNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtPhone.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PassportQueryActivity.this, PassportMainActivity.class);
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

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            if (!validateFirstName()) {
                return;
            } else if (!validatePhone()) {
                return;
            } else {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    try {
                        new PasswordQueryAsync().execute(getResources().getString(R.string.nid_or_passport_query),
                                "username=" + mAppHandler.getImeiNo(),
                                "&firstName=" + URLEncoder.encode(mEtName.getText().toString().trim().toUpperCase(), "UTF-8"),
                                "&notify_phone_no=" + URLEncoder.encode(mEtPhone.getText().toString().trim(), "UTF-8"),
                                "&format=" + "json");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean validateFirstName() {
        if (mEtName.getText().toString().trim().isEmpty()) {
            mEtName.setError(Html.fromHtml("<font color='red'>" + getString(R.string.passport_first_name_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    private boolean validatePhone() {
        if (mEtPhone.getText().toString().trim().isEmpty() || mEtPhone.getText().toString().trim().length() < 11) {
            mEtPhone.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    private class PasswordQueryAsync extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PassportQueryActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4];
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
                        Intent intent = new Intent(PassportQueryActivity.this, PassportLogActivity.class);
                        intent.putExtra(PassportLogActivity.TRX_DETAILS, jsonArray.toString());
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
}
