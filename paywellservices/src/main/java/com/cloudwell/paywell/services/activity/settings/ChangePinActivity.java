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
import android.view.Gravity;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class ChangePinActivity extends AppCompatActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private EditText mOldPin, mNewPin;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_settings_change_pin);

        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        mOldPin = (EditText) findViewById(R.id.oldPin);
        mNewPin = (EditText) findViewById(R.id.newPin);

        ((TextView) mLinearLayout.findViewById(R.id.tvOldPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mOldPin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvNewPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mNewPin.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mLinearLayout.findViewById(R.id.btnChangePin)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        mCd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
    }

    @SuppressWarnings("deprecation")
    public void resetPin(View v) {
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
            new ChangePinAsync().execute(getResources().getString(R.string.pin),
                    "iemi_no=" + mAppHandler.getImeiNo(),
                    "&old_pin=" + _oldPin,
                    "&new_pin=" + _newPin);
        }
    }

    private class ChangePinAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ChangePinActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result != null) {
                String splitStr[] = result.split("@");
                if (result.startsWith("200")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePinActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(R.string.change_pin_status_msg);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, splitStr[1], Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(ChangePinActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
