package com.cloudwell.paywell.services.activity.payments.statement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.activity.payments.rocket.DrawableClickListener;
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
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ipshita on 3/19/2017.
 */

public class CashInActivity extends AppCompatActivity implements View.OnClickListener {

    private AppHandler mAppHandler;
    private ConnectionDetector cd;
    private static EditText etPhnNo;
    private static EditText etAmount;
    private Button mComfirm;
    private static LinearLayout mLinearLayout;
    private static String mPhone;
    private static String mAmount;
    private static String mPin;
    RadioGroup radioGroup;
    private static String mPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_cash_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_rocket_pay_to);
        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        etPhnNo = (EditText) findViewById(R.id.etPhoneNo);
        etPhnNo.setTypeface(AppController.getInstance().getRobotoRegularFont());

        etPhnNo.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(etPhnNo) {
            @Override
            public boolean onDrawableClick() {
                // TODO : insert code to perform on clicking of the RIGHT drawable image...
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1001);
                return true;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            etPhnNo.setText(extras.getString("phn"));
        }

        etAmount = (EditText) findViewById(R.id.etAmount);
        etAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mComfirm = (Button) findViewById(R.id.btnConfirm);
        mComfirm.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mComfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mComfirm) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                mPhone = etPhnNo.getText().toString().trim();
                if (mPhone.length() < 11) {
                    etPhnNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                    return;
                }
                mAmount = etAmount.getText().toString().trim();
                if (Integer.parseInt(mAmount) < 20 || Integer.parseInt(mAmount) > 10000) {
                    etAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_limit_error_msg) + "</font>"));
                    return;
                }
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.fillup_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
                if (checkedRadioButtonId == R.id.radio_button1) {
                    mPurpose = getResources().getString(R.string.employee_payment_str);
                } else if (checkedRadioButtonId == R.id.radio_button2) {
                    mPurpose = getResources().getString(R.string.supplier_payment_str);
                } else {
                    mPurpose = getResources().getString(R.string.incentive_payment_str);
                }
                askForPin();
            }
        }
    }

    private void askForPin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    mPin = pinNoET.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        new SubmitCashInDataAsync().execute(getResources().getString(R.string.rocket_cash_in),
                                mAppHandler.getImeiNo(), mPin, mPhone, mAmount, mPurpose);
                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class SubmitCashInDataAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CashInActivity.this, "", getString(R.string.loading_msg), true);
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("username", data[1]));
                nameValuePairs.add(new BasicNameValuePair("service_type", URLEncoder.encode("Cash In", "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("account_no", data[3]));
                nameValuePairs.add(new BasicNameValuePair("amount", data[4]));
                nameValuePairs.add(new BasicNameValuePair("password", data[2]));
                nameValuePairs.add(new BasicNameValuePair("purpose", URLEncoder.encode(data[5], "UTF-8")));
                nameValuePairs.add(new BasicNameValuePair("type", "tab"));
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
                    String msg = jsonObject.getString("message");

                    AlertDialog.Builder builder = new AlertDialog.Builder(CashInActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(msg);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            startActivity(new Intent(CashInActivity.this, PaymentsMainActivity.class));
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                if (resultCode == Activity.RESULT_OK) {
                    /*Cursor s = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            null, null, null);
                    if (s.moveToFirst()) {
                        String phoneNum = s.getString(s.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        etPhnNo.setText(phoneNum);
                    }*/
                    try {
                        String phoneNumber = "";
                        Cursor cursor = managedQuery(data.getData(), null, null, null, null);
                        while (cursor.moveToNext()) {
                            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            if (hasPhone.equalsIgnoreCase("1"))
                                hasPhone = "true";
                            else
                                hasPhone = "false";
                            if (Boolean.parseBoolean(hasPhone)) {
                                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                                while (phones.moveToNext()) {
                                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                }
                                phones.close();
                            }
                            if (phoneNumber.startsWith("+88")) {
                                phoneNumber = phoneNumber.substring(3, phoneNumber.length());
                            }
                            if(phoneNumber.contains(" ")) {
                                phoneNumber = phoneNumber.replace(" ", "");
                            }
                            if (phoneNumber.contains("-")) {
                                phoneNumber = phoneNumber.replace("-", "");
                            }
                            etPhnNo.setText(phoneNumber);
                        }
                    } catch (Exception ex) {
                        Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                }  else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
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
        finish();
    }
}