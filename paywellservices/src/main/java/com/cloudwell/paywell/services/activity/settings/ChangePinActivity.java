package com.cloudwell.paywell.services.activity.settings;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

public class ChangePinActivity extends BaseActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private EditText mOldPin, mNewPin;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_settings_change_pin);
        }

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        mOldPin = findViewById(R.id.oldPin);
        mNewPin = findViewById(R.id.newPin);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvOldPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mOldPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvNewPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mNewPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            ((Button) mLinearLayout.findViewById(R.id.btnChangePin)).setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvOldPin)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mOldPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvNewPin)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mNewPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            ((Button) mLinearLayout.findViewById(R.id.btnChangePin)).setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    @SuppressWarnings("deprecation")
    public void resetPin(View v) {
        String _oldPin = mOldPin.getText().toString();
        String _newPin = mNewPin.getText().toString();
        if (_oldPin.length() == 0) {
            mOldPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.old_pin_error_msg) + "</font>"));
            return;
        }
        if (_newPin.length() == 0 || (_oldPin.equalsIgnoreCase(_newPin))) {
            mNewPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.new_pin_error_msg) + "</font>"));
            return;
        }
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new ChangePinAsync().execute(getString(R.string.pin), _oldPin, _newPin);
        }
    }

    private class ChangePinAsync extends AsyncTask<String, Void, String> {


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
                nameValuePairs.add(new BasicNameValuePair("iemi_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("old_pin", params[1]));
                nameValuePairs.add(new BasicNameValuePair("new_pin", params[2]));
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
        protected void onPostExecute(String result) {
            Log.e("logTag", result);
            dismissProgressDialog();
            if (result != null) {
                String splitStr[] = result.split("@");
                if (result.startsWith("200")) {

                    mAppHandler.setInitialChangePinStatus("true");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePinActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(R.string.change_pin_status_msg);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePinActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(splitStr[1]);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
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
        Intent intent = new Intent(ChangePinActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
