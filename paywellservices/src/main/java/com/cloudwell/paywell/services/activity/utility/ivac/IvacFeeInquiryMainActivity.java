package com.cloudwell.paywell.services.activity.utility.ivac;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IvacFeeInquiryMainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ConstraintLayout mConstraintLayout;
    private RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    private String selectedLimit = "";
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;
    private EditText editTextWebFile;
    private ImageView imageViewTrxLog;
    private Button buttonSubmit;

    private static final int PERMISSION_OPEN_CAMERA = 100;

    private int requestType;
    private static int TAG_REQUEST_IVAC_WEB_FILE_SEARCH = 1;
    private static int TAG_REQUEST_IVAC_FEE_INQUIRY = 2;

    private static String TAG_RESPONSE_IVAC_STATUS = "status";
    private static String TAG_RESPONSE_IVAC_MSG = "message";
    private static String TAG_RESPONSE_IVAC_TRX_DETAILS = "trxDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivac_fee_inquiry_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_ivac_inquiry_title);
        }

        mConstraintLayout = findViewById(R.id.constrainLayoutIvacInq);

        TextView _mWeb = findViewById(R.id.tvIvacWebFileNo);

        editTextWebFile = findViewById(R.id.etWebFile);
        imageViewTrxLog = findViewById(R.id.btnIvacTrxLog);
        buttonSubmit = findViewById(R.id.btnIvacInqSubmit);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        editTextWebFile.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editTextWebFile) {
            @Override
            public boolean onDrawableClick() {
                if (ActivityCompat.checkSelfPermission(IvacFeeInquiryMainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // permission has not been granted.
                    ActivityCompat.requestPermissions(IvacFeeInquiryMainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_OPEN_CAMERA);
                } else {
                    //initiate scan with our custom scan activity
                    new IntentIntegrator(IvacFeeInquiryMainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }
                return true;
            }
        });

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _mWeb.setTypeface(AppController.getInstance().getOxygenLightFont());
            editTextWebFile.setTypeface(AppController.getInstance().getOxygenLightFont());
            buttonSubmit.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _mWeb.setTypeface(AppController.getInstance().getAponaLohitFont());
            editTextWebFile.setTypeface(AppController.getInstance().getAponaLohitFont());
            buttonSubmit.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        imageViewTrxLog.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        refreshLanguage();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_IVAC_BILL_INQUIRY_MENU);

    }

    private void refreshLanguage() {

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            imageViewTrxLog.setBackgroundResource(R.drawable.transaction_log_en);
        } else {
            imageViewTrxLog.setBackgroundResource(R.drawable.transaction_log_bn);
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
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_OPEN_CAMERA: {
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    new IntentIntegrator(IvacFeeInquiryMainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                } else {
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                editTextWebFile.setText("");
                editTextWebFile.clearFocus();
                editTextWebFile.setText(result.getContents());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnIvacTrxLog) {
            requestType = TAG_REQUEST_IVAC_FEE_INQUIRY;
            showLimitPromptIvacTrxInq();
        }
        if (v.getId() == R.id.btnIvacInqSubmit) {
            requestType = TAG_REQUEST_IVAC_WEB_FILE_SEARCH;
            String web = editTextWebFile.getText().toString().trim();
            if ((web.length() <= 3) || (!web.startsWith("BGD"))) {
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.input_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            } else {
                if (!cd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    new TransactionLogAsync().execute(getString(R.string.utility_ivac_fee_pay_inq_with_web_file_no), web);
                }
            }
        }
    }

    private void showLimitPromptIvacTrxInq() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setTitle(R.string.log_limit_title_msg);
        dialog.setContentView(R.layout.dialog_trx_limit);

        Button btn_okay = dialog.findViewById(R.id.buttonOk);
        Button btn_cancel = dialog.findViewById(R.id.cancelBtn);

        radioButton_five = dialog.findViewById(R.id.radio_five);
        radioButton_ten = dialog.findViewById(R.id.radio_ten);
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty);
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty);
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred);
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred);

        radioButton_five.setOnCheckedChangeListener(this);
        radioButton_ten.setOnCheckedChangeListener(this);
        radioButton_twenty.setOnCheckedChangeListener(this);
        radioButton_fifty.setOnCheckedChangeListener(this);
        radioButton_hundred.setOnCheckedChangeListener(this);
        radioButton_twoHundred.setOnCheckedChangeListener(this);

        assert btn_okay != null;
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (selectedLimit.isEmpty()) {
                    selectedLimit = "5";
                }
                if (cd.isConnectingToInternet()) {
                    new TransactionLogAsync().execute(getResources().getString(R.string.utility_ivac_fee_pay_inq));
                } else {
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        assert btn_cancel != null;
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
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

    @SuppressWarnings("deprecation")
    private class TransactionLogAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                if (requestType == TAG_REQUEST_IVAC_FEE_INQUIRY) {
                    List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                    nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                    nameValuePairs.add(new BasicNameValuePair("password", "1234"));
                    nameValuePairs.add(new BasicNameValuePair("limit", selectedLimit));
                    nameValuePairs.add(new BasicNameValuePair("format", "json"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } else if (requestType == TAG_REQUEST_IVAC_WEB_FILE_SEARCH) {
                    List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                    nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                    nameValuePairs.add(new BasicNameValuePair("password", "1234"));
                    nameValuePairs.add(new BasicNameValuePair("web_file_no", params[1]));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                }
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    if (requestType == TAG_REQUEST_IVAC_FEE_INQUIRY) {
                        JSONObject object = new JSONObject(result);

                        String status = object.getString(TAG_RESPONSE_IVAC_STATUS);
                        String msg = object.getString(TAG_RESPONSE_IVAC_MSG);

                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArray = object.getJSONArray(TAG_RESPONSE_IVAC_TRX_DETAILS);
                            IvacFeeInquiryActivity.TRANSLOG_TAG = jsonArray.toString();
                            startActivity(new Intent(IvacFeeInquiryMainActivity.this, IvacFeeInquiryActivity.class));
                            finish();
                        } else {
                            Snackbar snackbar = Snackbar.make(mConstraintLayout, msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    } else if (requestType == TAG_REQUEST_IVAC_WEB_FILE_SEARCH) {
                        JSONObject object = new JSONObject(result);
                        String web_file = object.getString("web_file_no");
                        String passport = object.getString("passport_no");
                        String amount = object.getString("amount");
                        String center = object.getString("center_name");
                        String phone = object.getString("phone_no");
                        String trx_response_date_time = object.getString("trx_response_date_time");
                        String trx_id = object.getString("trx_id");
                        String status = object.getString("status");
                        String message = object.getString("message");
                        showFullInfo(web_file, passport, amount, center, phone, trx_response_date_time, trx_id, status, message);
                    }
                } catch (Exception ex) {
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void showFullInfo(String web_file, String passport, String amount, String center, String phone, String trx_response_date_time,
                              String trx_id, String status, String message) {
        String msg = "Web File: " + web_file + "\nPassport No: " + passport
                + "\nAmount: " + getString(R.string.tk_des) + " " + amount + "\nCenter Name: " + center
                + "\nPhone Number: " + phone + "\nDate: " + trx_response_date_time
                + "\nTrx ID: " + trx_id;

        AlertDialog.Builder builder = new AlertDialog.Builder(IvacFeeInquiryMainActivity.this);
        if (status.equalsIgnoreCase("200")) {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
            msg = msg + "\n\nStatus: " + message;
        }
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
