package com.cloudwell.paywell.services.activity.utility.passport;

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
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.adapter.CustomAdapter;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by android on 6/27/2016.
 */
public class PassportActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private ConnectionDetector mCd;
    private LinearLayout mLinearLayout;
    private Button mConfirm;
    private EditText mEtFristName;
    private EditText mEtLastName;
    private EditText mEtEmail;
    private EditText mEtPhone;
    private String mAmountType;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_passport_pay_fee_title);
        mCd = new ConnectionDetector(this.getApplicationContext());
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEtFristName = (EditText) findViewById(R.id.etFirstName);
        mEtLastName = (EditText) findViewById(R.id.etLastName);
        mEtEmail = (EditText) findViewById(R.id.etEmail);
        mEtPhone = (EditText) findViewById(R.id.etPhoneNo);
        Spinner spinnerAmountType = (Spinner) findViewById(R.id.spinnerAmountType);

        // Spinner click listener
        assert spinnerAmountType != null;
        spinnerAmountType.setOnItemSelectedListener(this);

        ((TextView) mLinearLayout.findViewById(R.id.tvFirstName)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtFristName.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvLastName)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtLastName.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvEmail)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtEmail.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvPhoneNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtPhone.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mLinearLayout.findViewById(R.id.tvAmountType)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        // Creating adapter for spinner
        assert spinnerAmountType != null;
        CharSequence[] amountTypes = this.getResources().getTextArray(R.array.passport_fee_array);
        CustomAdapter<CharSequence> serviceAdapter = new CustomAdapter<>(this, android.R.layout.simple_spinner_item, amountTypes);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmountType.setAdapter(serviceAdapter);
        mConfirm = (Button) findViewById(R.id.btnPassportConfirm);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PassportActivity.this, PassportMainActivity.class);
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
            } else if (!validateLastName()) {
                return;
            } else if (!validateEmail()) {
                return;
            } else if (!validatePhone()) {
                return;
            } else
                askForPin(v, mEtFristName.getText().toString().trim(), mEtLastName.getText().toString().trim(), mEtEmail.getText().toString().trim(), mEtPhone.getText().toString().trim(), mAmountType);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            mAmountType = "3450";
        } else{
            mAmountType = "6900";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validatePhone() {
        if (mEtPhone.getText().toString().trim().isEmpty() || mEtPhone.getText().toString().trim().length() < 11) {
            mEtPhone.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = mEtEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            mEtEmail.setError(Html.fromHtml("<font color='red'>" + getString(R.string.passport_email_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    private boolean validateLastName() {
        if (mEtLastName.getText().toString().trim().isEmpty()) {
            mEtLastName.setError(Html.fromHtml("<font color='red'>" + getString(R.string.passport_last_name_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    private boolean validateFirstName() {
        if (mEtFristName.getText().toString().trim().isEmpty()) {
            mEtFristName.setError(Html.fromHtml("<font color='red'>" + getString(R.string.passport_first_name_error_msg) + "</font>"));
            return false;
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void askForPin(final View view, final String firstName, final String lastName, final String email, final String phone, final String amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setLayoutParams(lp);
        pinNoET.setRawInputType(InputType.TYPE_CLASS_NUMBER |
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
                    Snackbar snackbar = Snackbar.make(mLinearLayout,  R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                } else {
                    if (!mCd.isConnectingToInternet()) {
                        Snackbar snackbar = Snackbar.make(view, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    } else {
                        try {
                            new PassportAsync().execute(getResources().getString(R.string.nid_or_passport_billpay),
                                    "username=" + mAppHandler.getImeiNo(),
                                    "&service_type=" + "DIP",
                                    "&first_name=" + URLEncoder.encode(firstName.toUpperCase(), "UTF-8"),
                                    "&last_name=" + URLEncoder.encode(lastName.toUpperCase(), "UTF-8"),
                                    "&email=" + URLEncoder.encode(email, "UTF-8"),
                                    "&notify_phone_no=" + URLEncoder.encode(phone, "UTF-8"),
                                    "&amount=" + URLEncoder.encode(amount, "UTF-8"),
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


    private class PassportAsync extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PassportActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7] + params[8] + params[9];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_STATUS);
                if (status.equalsIgnoreCase("100")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PassportActivity.this);
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
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, jsonObject.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG);
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
}
