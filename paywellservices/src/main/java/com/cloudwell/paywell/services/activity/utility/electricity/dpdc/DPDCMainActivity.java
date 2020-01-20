package com.cloudwell.paywell.services.activity.utility.electricity.dpdc;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASABillPayActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
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

public class DPDCMainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RelativeLayout mRelativeLayout;
    RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    String selectedLimit = "";
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;
    private static int service_type;
    private String packageNameYoutube = "com.google.android.youtube";
    private String linkDpdcBillPay = "https://www.youtube.com/watch?v=EovJfDwrKSc&t=4s";
    private static int TAG_SERVICE_POSTPAID_INQUIRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpdcmain);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_dpdc);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Button btnPostBill = findViewById(R.id.homeBtnPostpaidBillPay);
        Button btnPostInquiry = findViewById(R.id.homeBtnPostpaidInquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnPostBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPostInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnPostBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPostInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        checkIsComeFromFav(getIntent());

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_DPDC_MENU);

    }

    private void checkIsComeFromFav(Intent intent) {
        boolean isFav = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE, false);
        if (isFav) {
            boolean booleanExtra = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DPDC_INQUERY, false);
            if (booleanExtra) {
                service_type = TAG_SERVICE_POSTPAID_INQUIRY;
                showLimitPrompt();
            }

        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnPostpaidBillPay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_DPDC_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY);
                startActivity(new Intent(this, DPDCPostpaidBillPayActivity.class));
                break;
            case R.id.homeBtnPostpaidInquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_DPDC_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY_INQUIRY);
                service_type = TAG_SERVICE_POSTPAID_INQUIRY;
                showLimitPrompt();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_video) {
            if (isAppInstalled(packageNameYoutube)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkDpdcBillPay));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(packageNameYoutube);
                startActivity(intent);
            } else {
                WebViewActivity.TAG_LINK = linkDpdcBillPay;
                startActivity(new Intent(this, WebViewActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // Transaction LOG
    private void showLimitPrompt() {
        // custom dialog
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
                    if (service_type == TAG_SERVICE_POSTPAID_INQUIRY) {
                        new TransactionLogAsync().execute(getResources().getString(R.string.utility_multi_trx_inq));
                    }
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

            String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(DPDCMainActivity.this).getRID());
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("service", "DPDC"));
                nameValuePairs.add(new BasicNameValuePair("limit", selectedLimit));
                nameValuePairs.add(new BasicNameValuePair(ParameterUtility.KEY_REF_ID, uniqueKey));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
                if (service_type == TAG_SERVICE_POSTPAID_INQUIRY) {
                    DPDCPostpaidInquiryActivity.TRANSLOG_TAG = result;
                    startActivity(new Intent(DPDCMainActivity.this, DPDCPostpaidInquiryActivity.class));
                    finish();
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
