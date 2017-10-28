package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeMYCashPinActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private EditText mOldPin;
    private EditText mNewPin;
    private Button mConfirm;
    private LinearLayout mLinearLayout;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mycash_pin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_settings_change_pin);
        initializeView();
    }

    private void initializeView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.changePinLinearLayout);
        mOldPin = (EditText) findViewById(R.id.oldPin);
        mNewPin = (EditText) findViewById(R.id.newPin);
        mConfirm = (Button) findViewById(R.id.mycash_confirm);

        ((TextView) mLinearLayout.findViewById(R.id.tvOldPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mOldPin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvNewPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mNewPin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mLinearLayout.findViewById(R.id.mycash_confirm)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        mConfirm.setOnClickListener(this);

        mCd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
    }

    public void resetPin(View v) {


    }

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            String _oldPin = mOldPin.getText().toString();
            String _newPin = mNewPin.getText().toString();
            if (_oldPin.length() == 0) {
                mOldPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.old_pin_error_msg) + "</font>"));
                return;
            }
            if (_newPin.length() == 0) {
                mNewPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.new_pin_error_msg) + "</font>"));
                return;
            }
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                new submitConfirm().execute(getResources().getString(R.string.mycash_change_pin), _oldPin, _newPin);
            }
        }
    }

    private class submitConfirm extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ChangeMYCashPinActivity.this, "", getString(R.string.loading_msg), true);
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
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("pin", data[1]));
                nameValuePairs.add(new BasicNameValuePair("newpin", data[2]));
                nameValuePairs.add(new BasicNameValuePair("service_type", "PIN_Change"));
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
            progressDialog.dismiss();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);

                    if (status.equals("200")) {
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMYCashPinActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage(msg_text);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(true);
                        alert.show();
                    } else {
                        String msg = jsonObject.getString(TAG_MESSAGE);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMYCashPinActivity.this);
                        builder.setMessage(msg + "\n" + msg_text);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(true);
                        alert.show();
                    }
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
        Intent intent = new Intent(ChangeMYCashPinActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
