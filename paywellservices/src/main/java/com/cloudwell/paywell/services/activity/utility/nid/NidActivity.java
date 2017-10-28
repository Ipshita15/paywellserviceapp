package com.cloudwell.paywell.services.activity.utility.nid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NidActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private LinearLayout mLinearLayout;
    private EditText mEtNidNo;
    private Button mConfirm;
    private ConnectionDetector mCd;
    private EditText mEtPhoneNo;
    private String mNid;
    private String mPhone;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_nid_correction_title);
        mCd = new ConnectionDetector(this.getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEtNidNo = (EditText) findViewById(R.id.etNidNo);
        mEtPhoneNo = (EditText) findViewById(R.id.etPhoneNo);

        mConfirm = (Button) findViewById(R.id.btnNidConfirm);
        mConfirm.setOnClickListener(this);

        ((TextView) mLinearLayout.findViewById(R.id.tvNidCardNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtNidNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvPhoneNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtPhoneNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mConfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());
    }


    public void onClick(View v) {
        if (v == mConfirm) {
            mNid = mEtNidNo.getText().toString().trim();
            if (mNid.length() == 0) {
                mEtNidNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.nid_card_error_msg) + "</font>"));
                return;
            }
            mPhone = mEtPhoneNo.getText().toString().trim();
            if (mPhone.length() < 11) {
                mEtPhoneNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                return;
            }
            askForPin(v);
        }
    }

    private void askForPin(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                String password = pinNoET.getText().toString().trim();
                if (password.length() == 0) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                } else {
                    if (!mCd.isConnectingToInternet()) {
                        Snackbar snackbar = Snackbar.make(view, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    } else {
                        try {
                            new NidAsync(view).execute(getResources().getString(R.string.nid_or_passport_billpay),
                                    "username=" + mAppHandler.getImeiNo(),
                                    "&service_type=" + "NID",
                                    "&nid_number=" + URLEncoder.encode(mNid, "UTF-8"),
                                    "&nid_service_type=1",
                                    "&nid_correction_type=2",
                                    "&notify_phone_no=" + URLEncoder.encode(mPhone, "UTF-8"),
                                    "&password=" + URLEncoder.encode(password, "UTF-8"),
                                    "&format=" + "json");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private class NidAsync extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;
        private View view;

        public NidAsync(View view) {
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(NidActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7] + params[8];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_STATUS);
                if (status.equalsIgnoreCase("100")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NidActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage("To be Process");
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Snackbar snackbar = Snackbar.make(view, jsonObject.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG);
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
    public void onBackPressed() {
        Intent intent = new Intent(NidActivity.this, NidMainActivity.class);
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
