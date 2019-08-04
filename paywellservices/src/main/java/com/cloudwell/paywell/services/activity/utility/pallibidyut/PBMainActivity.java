package com.cloudwell.paywell.services.activity.utility.pallibidyut;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.WebViewActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBInquiryBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.PBBillStatusActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.PBBillStatusInquiryActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.MobileNumberChangeActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.PBInquiryMobileNumberChangeActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.PBInquiryRegActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.PBRegistrationActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

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

import androidx.appcompat.app.AppCompatDialog;

public class PBMainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RelativeLayout mRelativeLayout;
    private RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    private String selectedLimit = "";
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;
    private static String serviceName;
    private String packageNameYoutube = "com.google.android.youtube";
    private String linkPolliBillPay = "https://www.youtube.com/watch?v=SAuIFcUclvs&t=1s";
    private static String TAG_SERVICE_REGISTRATION_INQUIRY = "POLLI_REG";
    private static String TAG_SERVICE_BILL_INQUIRY = "POLLI_BILL";
    private static String TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY = "POLLI_RREG";
    private static String TAG_SERVICE_PHONE_NUMBER_BILL_STATUS = "POLLI_STATUS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbmain);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_pollibiddut);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Button btnReg = findViewById(R.id.homeBtnRegistration);
        Button btnBill = findViewById(R.id.homeBtnBillPay);
        Button btnRegInq = findViewById(R.id.homeBtnInquiryReg);
        Button btnBillInq = findViewById(R.id.homeBtnInquiryBillPay);

        Button btRequestInquiry = findViewById(R.id.btRequestInquiry);
        Button btBillStatusInquiry = findViewById(R.id.btBillStatusInquiry);

        Button btChangeMobileNumber = findViewById(R.id.btChangeMobileNumber);
        Button btChangeMobileNumbEnquiry = findViewById(R.id.btChangeMobileNumbeEnquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnReg.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnRegInq.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBillInq.setTypeface(AppController.getInstance().getOxygenLightFont());
            btRequestInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
            btBillStatusInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
            btChangeMobileNumber.setTypeface(AppController.getInstance().getOxygenLightFont());
            btChangeMobileNumbEnquiry.setTypeface(AppController.getInstance().getOxygenLightFont());

        } else {
            btnReg.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnRegInq.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBillInq.setTypeface(AppController.getInstance().getAponaLohitFont());
            btRequestInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
            btBillStatusInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
            btChangeMobileNumber.setTypeface(AppController.getInstance().getAponaLohitFont());
            btChangeMobileNumbEnquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        checkIsComeFromFav(getIntent());

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU);


    }

    private void checkIsComeFromFav(Intent intent) {
        boolean isFav = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE, false);
        if (isFav) {
            boolean booleanExtra = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_RG_INQUERY, false);
            if (booleanExtra) {
                shwTheLimitedPrompt(TAG_SERVICE_REGISTRATION_INQUIRY);
            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_BILL_INQUERY, false)) {
                shwTheLimitedPrompt(TAG_SERVICE_BILL_INQUIRY);
            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_REQUEST_BILL_INQUIRY, false)) {
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_BILL_STATUS);
            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_MOBILE_NUMBER_CHANGE_INQUIRY, false)) {
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_video) {
            if (isAppInstalled(packageNameYoutube)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPolliBillPay));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(packageNameYoutube);
                startActivity(intent);
            } else {
                WebViewActivity.TAG_LINK = linkPolliBillPay;
                startActivity(new Intent(this, WebViewActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnRegistration:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION);
                startActivity(new Intent(this, PBRegistrationActivity.class));
                break;
            case R.id.homeBtnBillPay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_PAY);
                startActivity(new Intent(this, PBBillPayActivity.class));
                break;
            case R.id.homeBtnInquiryReg:
                shwTheLimitedPrompt(TAG_SERVICE_REGISTRATION_INQUIRY);
                break;
            case R.id.homeBtnInquiryBillPay:
                shwTheLimitedPrompt(TAG_SERVICE_BILL_INQUIRY);
                break;

            case R.id.btRequestInquiry:
                startActivity(new Intent(this, PBBillStatusActivity.class));

                break;
            case R.id.btBillStatusInquiry:
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_BILL_STATUS);
                break;

            case R.id.btChangeMobileNumber:
                startActivity(new Intent(this, MobileNumberChangeActivity.class));
                break;

            case R.id.btChangeMobileNumbeEnquiry:
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY);
                break;
            default:
                break;
        }
    }

    private void shwTheLimitedPrompt(String tagServiceBillInquiry) {
        serviceName = tagServiceBillInquiry;
        showLimitPrompt();
    }

    private void showLimitPrompt() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        if (serviceName.equalsIgnoreCase(TAG_SERVICE_REGISTRATION_INQUIRY)) {
            dialog.setTitle(R.string.reg_log_limit_title_msg);
        } else {
            dialog.setTitle(R.string.log_limit_title_msg);
        }
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
                    new TransactionLogAsync().execute(getString(R.string.utility_multi_trx_inq),
                            "username=" + mAppHandler.getImeiNo(),
                            "&service=" + serviceName,
                            "&limit=" + selectedLimit);
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("service", serviceName));
                nameValuePairs.add(new BasicNameValuePair("limit", selectedLimit));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
//                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("logTag", "" + result);


            dismissProgressDialog();
            if (result != null) {
                if (serviceName.equalsIgnoreCase(TAG_SERVICE_REGISTRATION_INQUIRY)) {
                    PBInquiryRegActivity.TRANSLOG_TAG = result;
                    startActivity(new Intent(PBMainActivity.this, PBInquiryRegActivity.class));
                } else if (serviceName.equalsIgnoreCase(TAG_SERVICE_BILL_INQUIRY)) {
                    PBInquiryBillPayActivity.TRANSLOG_TAG = result;
                    startActivity(new Intent(PBMainActivity.this, PBInquiryBillPayActivity.class));

                } else if (serviceName.equalsIgnoreCase(TAG_SERVICE_PHONE_NUMBER_BILL_STATUS)) {
                    PBBillStatusInquiryActivity.TRANSLOG_TAG = result;
                    startActivity(new Intent(PBMainActivity.this, PBBillStatusInquiryActivity.class));

                } else if (serviceName.equalsIgnoreCase(TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY)) {
                    PBInquiryMobileNumberChangeActivity.Companion.setTRANSLOG_TAG(result);
                    startActivity(new Intent(PBMainActivity.this, PBInquiryMobileNumberChangeActivity.class));
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

    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }
}
