package com.cloudwell.paywell.services.activity.topup;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.topup.brilliant.model.BrilliantTopUpInquiry;
import com.cloudwell.paywell.services.activity.utility.ivac.DrawableClickListener;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
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

import java.io.IOException;

public class BrilliantTopupActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    private static final int CONTACT_REQ_CODE = 333;
    private static final String BRILLIANT_PREFIX = "09638";
    private static final String COUNTRY_CODE = "88";
    public static final String LIMIT_STRING ="limit" ;
    private static final int CONTACT_PERMISSION_CODE = 444;
    public boolean statusCode = false;
    private RelativeLayout topUpLayout;
    private static String PIN_NO = "unknown";
    private ConnectionDetector cd;
    private AppHandler mAppHandler;
    private Button btnConfirmBrilliant;
    private String phoneStr=new String();
    private String amountStr=new String();
    private EditText phoneNoET;
    private EditText amountET;
    private ImageView brilliantTopUpInquiry;
    private ImageView brilliantTrxLog;
    private RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    private String selectedLimit = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brilliant_topup);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.brilliant_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        topUpLayout = (RelativeLayout) findViewById(R.id.brilliantLL);
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        btnConfirmBrilliant= (Button) findViewById(R.id.btnConfirmBrilliant);
        phoneNoET = (EditText) findViewById(R.id.brilliantPhoneNo);
        amountET = (EditText) findViewById(R.id.brilliantAmount);
        brilliantTopUpInquiry=findViewById(R.id.brilliantInquiryBtn);
        brilliantTrxLog=findViewById(R.id.brilliantTransLogBtn);
        phoneNoET.setText("");
        phoneNoET.append(BRILLIANT_PREFIX);


        btnConfirmBrilliant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentTopupLog();
            }
        });

        brilliantTrxLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLimitPrompt(new Intent(BrilliantTopupActivity.this,BrilliantTransactionLogActivity.class));
            }
        });
        brilliantTopUpInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnquiryPrompt();
            }
        });

        phoneNoET.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(phoneNoET) {
            @Override
            public boolean onDrawableClick() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ContextCompat.checkSelfPermission(BrilliantTopupActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BrilliantTopupActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent,CONTACT_REQ_CODE);
                }
                return true;
            }
        });
    }

    private void showLimitPrompt(final Intent intent) {
        // custom dialog
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setTitle(R.string.log_limit_title_msg);
        dialog.setContentView(R.layout.dialog_trx_limit);

        Button btn_okay = (Button) dialog.findViewById(R.id.buttonOk);
        Button btn_cancel = (Button) dialog.findViewById(R.id.cancelBtn);

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

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (selectedLimit.isEmpty()) {
                    selectedLimit = "5";
                }
                int limit = Integer.parseInt(selectedLimit);
                if (cd.isConnectingToInternet()) {
                    startActivity(intent.putExtra(LIMIT_STRING,selectedLimit));
                } else {
                    Snackbar snackbar = Snackbar.make(topUpLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 333:
                if (resultCode == Activity.RESULT_OK) {
                    String phoneNumOne = null;
                    if (phoneNoET != null) {
                        Cursor phones = getContentResolver()
                                .query(data.getData(),
                                        null,
                                        null,
                                        null,
                                        null);
                        while (phones.moveToNext()) {
                            phoneNumOne = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                        if (!phoneNumOne.isEmpty() && !phoneNumOne.contains("+") && !phoneNumOne.contains("-")
                                && !phoneNumOne.contains(" ") && !phoneNumOne.startsWith("88")) {
                            if (phoneNumOne.startsWith(BRILLIANT_PREFIX)){
                                phoneNoET.setText(phoneNumOne);
                            }else {
                                Log.d("TEST","NUMBER-"+phoneNumOne);
                                phoneNoET.setText("");
                                Snackbar snackbar = Snackbar.make(topUpLayout, "Please, Select Brilliant number only", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                                snackbar.show();
                            }
                        } else {
                            if (phoneNumOne.contains("+")) {
                                phoneNumOne = phoneNumOne.replace("+", "");
                            }
                            if (phoneNumOne.contains("-")) {
                                phoneNumOne = phoneNumOne.replace("-", "");
                            }
                            if (phoneNumOne.contains(" ")) {
                                phoneNumOne = phoneNumOne.replace(" ", "");
                            }
                            if (phoneNumOne.startsWith(COUNTRY_CODE)){
                               phoneNumOne.replace(COUNTRY_CODE,"");
                            }
                            if (phoneNumOne.startsWith(BRILLIANT_PREFIX)){
                                phoneNoET.setText(phoneNumOne);
                            }else {
                                Log.d("TEST","NUMBER-"+phoneNumOne);
                                phoneNoET.setText("");
                                Snackbar snackbar = Snackbar.make(topUpLayout, "Please, Select Brilliant number only", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                                snackbar.show();
                            }

                        }
                    }
                }
        }
    }

    private void showCurrentTopupLog() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        StringBuilder reqStrBuilder=new StringBuilder();
        String phoneStr = phoneNoET.getText().toString();
        String amountStr = amountET.getText().toString();

        if (phoneStr.startsWith(BRILLIANT_PREFIX)){
            if (phoneStr.length() < 11) {
                phoneNoET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                return;
            } else if (amountStr.length() < 1) {
                amountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                return;
            }
            reqStrBuilder.append( getString(R.string.phone_no_des) + " " + phoneStr
                    + "\n " + getString(R.string.amount_des) + " " + amountStr + getString(R.string.tk)
                    +"\n\n");


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.conf_topup_title_msg);
            builder.setMessage(reqStrBuilder);
            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.dismiss();
                    askForPin();
                }
            });
            builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();



        }else{
            Snackbar snackbar = Snackbar.make(topUpLayout, "It's not a 'Brilliant' number, Enter a valid number", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }



    }

    private void askForPin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    dialogInterface.dismiss();
                    PIN_NO = pinNoET.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        new TopUpAsync().execute();
                    } else {
                        Snackbar snackbar = Snackbar.make(topUpLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(topUpLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class TopUpAsync extends AsyncTask<Object, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BrilliantTopupActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {

            return generateRequestURL();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result != null) {
                if (result.startsWith("313")) {
//                    showTransactionLog(result, false);

                    showResponse(result);

                } else {
                    if (result.startsWith("200")) {
                        statusCode = true;
                    }
                    showResponse(result);
                }
            } else {
                Snackbar snackbar = Snackbar.make(topUpLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
        private String generateRequestURL() {
            String requestString = null;

            // Single
            EditText phoneNoET, amountET;
            RadioGroup prePostSelector;


            phoneNoET = (EditText) findViewById(R.id.brilliantPhoneNo);
            amountET = (EditText) findViewById(R.id.brilliantAmount);

            phoneStr = phoneNoET.getText().toString();
            amountStr = amountET.getText().toString();

            requestString = "https://api.paywellonline.com/PayWellBrilliantSystem/addBalance?"
                    + "username=" + mAppHandler.getImeiNo()
                    + "&password=" + PIN_NO
                    + "&amount=" + amountStr
                    + "&brilliantNumber=" +COUNTRY_CODE+phoneStr;

            Log.d("Brilliant_test","username=" + mAppHandler.getImeiNo()
                    + "&password=" + PIN_NO
                    + "&amount=" + amountStr
                    + "&brilliantNumber=" + phoneStr);

            return sendingHTTPSTopupRequest(requestString);
        }
    }

    private void showResponse(String result) {

        try {
            JSONObject json = new JSONObject(result);
            String statusCode=json.getString("status_code");
//            String message=json.getString("message");
            String userResponse=new String();


            if (statusCode.equalsIgnoreCase("200")){

                userResponse=getString(R.string.brilliant_num) + " " + json.getString("brilliantNumber")
                        + "\n" + getString(R.string.amount_des) + " " + json.getString("amount") + " " + getString(R.string.tk_des)
                        + "\n\n" + getString(R.string.paywell_trx_id) + " " + json.getString("paywell_trx_id")
                        + "\n" + getString(R.string.brilliant_trx_id) + " " + json.getString("brilliant_trx_id")
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + " " + json.getString("pwContact");

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
                builder.setMessage(userResponse);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
//                        startActivity(new Intent(BrilliantTopupActivity.this,TopupMainActivity.class));
                        phoneNoET.setText("");
                        amountET.setText("");

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }else {

                userResponse=json.getString("message")
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + " " + json.getString("pwContact");


                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
                builder.setMessage(userResponse);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }



//            Snackbar snackbar = Snackbar.make(topUpLayout, message, Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//            snackbar.show();

//            if (statusCode.equalsIgnoreCase("200")){
//                startActivity(new Intent(BrilliantTopupActivity.this,TopupMainActivity.class));
//            }

        } catch (JSONException e) {
            e.printStackTrace();

            Snackbar snackbar = Snackbar.make(topUpLayout, "Unknown Exception!!!", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();

//            startActivity(new Intent(BrilliantTopupActivity.this,TopupMainActivity.class));

        }


    }

    @SuppressWarnings("deprecation")
    private String sendingHTTPSTopupRequest(String requestString) {
        String responseTxt = null;
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is
        // established.
        int timeoutConnection = 1000000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // Instantiate the custom HttpClient
        HttpClient httpClient = new MyHttpClient(httpParameters, getApplicationContext());
        // HttpClient client = AppController.getInstance().getTrustedHttpClient();
        HttpGet request = new HttpGet(requestString);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            responseTxt = httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseTxt;

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
        finish();
    }


//    private void showTransactionLog(String responseTxt, final boolean isAuthorized) {
//        // code for topup receipt dialog
//        if (isAuthorized) {
//            StringBuilder receiptBuilder = new StringBuilder();
//            final StringBuilder topupBuilder = new StringBuilder();
//            EditText multiplePhoneNoET, multipleAmountET, singlePhoneNoET, singleAmountET;
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(BrilliantTopupActivity.this);
//             // code for single topup
//
//                    singlePhoneNoET = (EditText) findViewById(R.id.brilliantPhoneNo);
//                    singleAmountET = (EditText) findViewById(R.id.brilliantAmount);
//            String mPhnNo = singlePhoneNoET.getText().toString();
//            String mAmount = singleAmountET.getText().toString();
//                    String[] splitSingleAt = responseTxt.split("@");
//
//                String mStatusMessage = splitSingleAt[1];
//                String mTrxId = splitSingleAt[2];
//                String mHotLine = splitSingleAt[5];
//                if (splitSingleAt[0].equals("200")) {
//                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhnNo
//                            + "\n" + getString(R.string.amount_des) + " " + mAmount + " " + getString(R.string.tk_des)
//                            + "\n" + getString(R.string.trx_id_des) + " " + mTrxId
//                            + "\n\n" + getString(R.string.using_paywell_des)
//                            + "\n" + getString(R.string.hotline_des) + " " + mHotLine);
//                } else {
//                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhnNo
//                            + "\n" + getString(R.string.amount_des) + " " + mAmount + " " + getString(R.string.tk_des)
//                            + "\n\n" + getString(R.string.status_des) + " " + mStatusMessage
//                            + "\n\n" + getString(R.string.trx_id_des) + " " + mTrxId
//                            + "\n\n" + getString(R.string.using_paywell_des)
//                            + "\n" + getString(R.string.hotline_des) + " " + mHotLine);
//                }
//                if (splitSingleAt[0].equals("200")) {
//                    builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
//                } else {
//                    builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
//                }
//
//            builder.setMessage(receiptBuilder.toString());
//
//            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int id) {
//                    dialogInterface.dismiss();
//                    if (statusCode) {
//                        startActivity(new Intent(BrilliantTopupActivity.this, BrilliantTopupActivity.class));
//                        finish();
//                    }
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        } else {
//            // 313@AuthenticationError
//            AlertDialog.Builder builder = new AlertDialog.Builder(BrilliantTopupActivity.this);
//            builder.setMessage(R.string.services_alert_msg);
//            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int id) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//    }

    private void showEnquiryPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BrilliantTopupActivity.this);
        builder.setTitle(R.string.phone_no_title_msg);

        final EditText enqNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        enqNoET.setLayoutParams(lp);
        enqNoET.setInputType(InputType.TYPE_CLASS_NUMBER);
        enqNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(14);
        enqNoET.setFilters(FilterArray);
        builder.setView(enqNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (enqNoET.getText().toString().length() < 11) {
                    Snackbar snackbar = Snackbar.make(topUpLayout, R.string.phone_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    showEnquiryPrompt();
                } else {
                    if (!cd.isConnectingToInternet()) {
                        AppHandler.showDialog(getSupportFragmentManager());
                    } else {
                        getTopUpInquiry(mAppHandler.getImeiNo(),enqNoET.getText().toString());
                    }
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getTopUpInquiry(final String userName, String number){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.get("https://api.paywellonline.com/PayWellBrilliantSystem/transactionEnquiry?")
                .addQueryParameter("username",userName)
                .addQueryParameter("brilliant_number", number)
                .setPriority(Priority.HIGH)
                .build().getAsObject(BrilliantTopUpInquiry.class, new ParsedRequestListener<BrilliantTopUpInquiry>() {
            @Override
            public void onResponse(BrilliantTopUpInquiry o) {
                if (o.getStatusCode()==200){
                    showEnquiryResult(o.getData().getBrilliantMobileNumber(),o.getData().getAmount(),o.getData().getBriliantTrxId(),o.getData().getPaywellTrxId(),String.valueOf(o.getStatusCode()),o.getData().getStatusName(),o.getData().getAddDatetime(),o.getPwContact());
                }else {
                    showEnquiryResult(null,null,null,null,String.valueOf(o.getStatusCode()),o.getMessage(),null,o.getPwContact());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void showEnquiryResult(String phone, String amount, String brilliant_trx_id, String trxId, String status,String statusMsg, String datetime, String hotline) {

        String inquiryReceipt=new String();
        if (status.equalsIgnoreCase("200")) {
            inquiryReceipt = getString(R.string.phone_no_des) + " " + phone
                    + "\n" + getString(R.string.brilliant_trx_id) + " " + brilliant_trx_id
                    + "\n" + getString(R.string.trx_id_des) + " " + trxId
                    + "\n" + getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                    + "\n" + getString(R.string.date_des) + " " + datetime
                    + "\n\n" + getString(R.string.using_paywell_des)
                    + "\n" + getString(R.string.hotline_des) + " " + hotline;
        } else {
            inquiryReceipt = getString(R.string.status_des) + " " + statusMsg
                    + "\n\n" + getString(R.string.using_paywell_des)
                    + "\n" + getString(R.string.hotline_des) + " " + hotline;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(BrilliantTopupActivity.this);
        if (status.equalsIgnoreCase("200")) {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
        }
        builder.setMessage(inquiryReceipt);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){

            case CONTACT_PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent,CONTACT_REQ_CODE);

                }else {
                    Snackbar snackbar = Snackbar.make(topUpLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
//                    checkPermission();
                }
            }

        }
    }
}