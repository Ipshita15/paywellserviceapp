package com.cloudwell.paywell.services.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.TelephonyInfo;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AppLoadingActivity extends AppCompatActivity {
    private static final String TAG = AppLoadingActivity.class.getName();
    private static final int REQUEST_PHONE_STATE = 1;
    private ProgressBar mPBAppLoading;
    private static List<String> mTopupServices;
    private static List<String> mUtilityServices;
    private static List<String> mETicketServices;
    private static List<String> mPaymentsServices;
    private static List<String> mRefillServices;
    private RelativeLayout mRelativeLayout;
    private TextView mConErrorMsg;
    private Button mBtnRetry;
    private AppHandler mAppHandler;
    private String mImeiNo;
    private ConnectionDetector mCd;
    public static String versionName = null;
    public static String androidVersionName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        mConErrorMsg = (TextView) findViewById(R.id.connErrorMsg);
        mBtnRetry = (Button) findViewById(R.id.btnRetry);
        mPBAppLoading = (ProgressBar) findViewById(R.id.pbAppLoading);

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(getApplicationContext());
        if (mAppHandler.getAppStatus().equalsIgnoreCase("registered") && mCd.isConnectingToInternet()) {
            RegisteredUserLogin();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(AppLoadingActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 100);
        } else {
            registerUser();
        }
    }

    private void registerUser() {

        if (!mCd.isConnectingToInternet()) {
            mConErrorMsg.setVisibility(View.VISIBLE);
            mBtnRetry.setVisibility(View.VISIBLE);
            mPBAppLoading.setVisibility(View.GONE);
        } else {
            // Check if the Phone permission is already available.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Phone permission has not been granted.
                requestPhonePermission();
            } else {
                // Phone permissions is already available.
                Field[] fields = Build.VERSION_CODES.class.getFields();
                for (Field field : fields) {
                    androidVersionName = field.getName();
                }
                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
                mImeiNo = telephonyInfo.getImeiSIM1();
                mAppHandler.setImeiNo(mImeiNo);
                new AuthAsync().execute(getResources().getString(R.string.pw_auth),
                        "imei_no=" + mAppHandler.getImeiNo(),
                        "&version_no=" + getVersionName(),
                        "&version_name=" + androidVersionName);//imei_no=a10000289bbb5d
            }
        }
    }

    private void RegisteredUserLogin() {

        if (!mCd.isConnectingToInternet()) {
            mConErrorMsg.setVisibility(View.VISIBLE);
            mBtnRetry.setVisibility(View.VISIBLE);
            mPBAppLoading.setVisibility(View.GONE);
        } else {
            // Check if the Phone permission is already available.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Phone permission has not been granted.
                requestPhonePermission();
            } else {
                // Phone permissions is already available.
                Field[] fields = Build.VERSION_CODES.class.getFields();
                for (Field field : fields) {
                    androidVersionName = field.getName();
                }
                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
                mImeiNo = telephonyInfo.getImeiSIM1();
                mAppHandler.setImeiNo(mImeiNo);
                new AuthenticationAsync().execute(getResources().getString(R.string.pw_auth),
                        "imei_no=" + mAppHandler.getImeiNo(),
                        "&version_no=" + getVersionName(),
                        "&version_name=" + androidVersionName);
            }
        }
    }

    private void requestPhonePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE) {
            // Check if the only required permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Phone permission has been granted
                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
                mImeiNo = telephonyInfo.getImeiSIM1();
                mAppHandler.setImeiNo(mImeiNo);
                if (!mCd.isConnectingToInternet()) {
                    mAppHandler.showDialog(getSupportFragmentManager());
                } else {
                    new AuthAsync().execute(getResources().getString(R.string.pw_auth),
                            "imei_no=" + mAppHandler.getImeiNo(),
                            "&version_no=" + getVersionName(),
                            "&version_name=" + androidVersionName);
                }

            } else {
                ActivityCompat.requestPermissions(AppLoadingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private float getVersionName() {
        String _versionName = null;
        try {
            _versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionName = _versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Version Code not available"); // There was a problem with the code retrieval
        } catch (NullPointerException e) {
            Log.e(TAG, "Context is null");
        }
        return Float.parseFloat(_versionName); // Found the code!
    }

    private class AuthAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            mTopupServices = new ArrayList<>();
            mUtilityServices = new ArrayList<>();
            mETicketServices = new ArrayList<>();
            mPaymentsServices = new ArrayList<>();
            mRefillServices = new ArrayList<>();
            try {
                if (result != null && result.contains("@")) {
                    String[] spiltArray = result.split("@@@");
                    String[] serviceCode = spiltArray[0].split("@");
                    if (serviceCode[0].equalsIgnoreCase("200")) {

                        if (!mAppHandler.getAppStatus().equalsIgnoreCase("registered")) {
                            mAppHandler.setAppStatus("registered");
                        }
                        for (int i = 0; i < spiltArray.length; i++) {
                            String[] subSpiltArray = spiltArray[i].split("@");
                            if (subSpiltArray[0].equalsIgnoreCase("1")) {
                                mTopupServices.add(subSpiltArray[2]);
                            } else if (subSpiltArray[0].equalsIgnoreCase("4") ||
                                    subSpiltArray[0].equalsIgnoreCase("5") ||
                                    subSpiltArray[0].equalsIgnoreCase("24") ||
                                    subSpiltArray[0].equalsIgnoreCase("25") ||
                                    subSpiltArray[0].equalsIgnoreCase("26")) {
                                mUtilityServices.add(subSpiltArray[2]);
                            } else if (subSpiltArray[0].equalsIgnoreCase("7") || subSpiltArray[0].equalsIgnoreCase("19")) {
                                mETicketServices.add(subSpiltArray[2]);
                            } else if (subSpiltArray[0].equalsIgnoreCase("11") || subSpiltArray[0].equalsIgnoreCase("36")) {
                                mPaymentsServices.add(subSpiltArray[2]);
                            }
                        }
                        checkPayWellBalance();
                    } else if (serviceCode[0].equalsIgnoreCase("300")) {
                        Intent intent = new Intent(AppLoadingActivity.this, EntryMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    mConErrorMsg.setText(R.string.conn_timeout_msg);
                    mConErrorMsg.setVisibility(View.VISIBLE);
                    mBtnRetry.setVisibility(View.VISIBLE);
                    mPBAppLoading.setVisibility(View.GONE);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }

    private class AuthenticationAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            mTopupServices = new ArrayList<>();
            mUtilityServices = new ArrayList<>();
            mETicketServices = new ArrayList<>();
            mPaymentsServices = new ArrayList<>();
            mRefillServices = new ArrayList<>();
            try {
                if (result != null && result.contains("@")) {
                    String[] spiltArray = result.split("@@@");
                    String[] serviceCode = spiltArray[0].split("@");
                    if (serviceCode[0].equalsIgnoreCase("200")) {

                        if (!mAppHandler.getAppStatus().equalsIgnoreCase("registered")) {
                            mAppHandler.setAppStatus("registered");
                        }
                        for (int i = 0; i < spiltArray.length; i++) {
                            String[] subSpiltArray = spiltArray[i].split("@");
                            if (subSpiltArray[0].equalsIgnoreCase("1")) {
                                mTopupServices.add(subSpiltArray[2]);
                            } else if (subSpiltArray[0].equalsIgnoreCase("4") ||
                                    subSpiltArray[0].equalsIgnoreCase("5") ||
                                    subSpiltArray[0].equalsIgnoreCase("24") ||
                                    subSpiltArray[0].equalsIgnoreCase("25") ||
                                    subSpiltArray[0].equalsIgnoreCase("26")) {
                                mUtilityServices.add(subSpiltArray[2]);
                            } else if (subSpiltArray[0].equalsIgnoreCase("7") || subSpiltArray[0].equalsIgnoreCase("19")) {
                                mETicketServices.add(subSpiltArray[2]);
                            } else if (subSpiltArray[0].equalsIgnoreCase("11") || subSpiltArray[0].equalsIgnoreCase("36")) {
                                mPaymentsServices.add(subSpiltArray[2]);
                            }
                        }
                        CheckPWBalance();
                    } else if (serviceCode[0].equalsIgnoreCase("300")) {
                        Intent intent = new Intent(AppLoadingActivity.this, EntryMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    mConErrorMsg.setText(R.string.conn_timeout_msg);
                    mConErrorMsg.setVisibility(View.VISIBLE);
                    mBtnRetry.setVisibility(View.VISIBLE);
                    mPBAppLoading.setVisibility(View.GONE);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }

    public static List<String> getServices() {
        return mTopupServices;
    }

    public static List<String> getUtilityService() {
        return mUtilityServices;
    }

    public static List<String> getETicketService() {
        return mETicketServices;
    }

    public static List<String> getPaymentsService() {
        return mPaymentsServices;
    }

    public static List<String> getRefillService() {
        return mRefillServices;
    }

    private void checkPayWellBalance() {
        new PayWellBalanceAsync().execute(
                getResources().getString(R.string.pw_bal),
                "imei_no=" + mAppHandler.getImeiNo());
    }

    private class PayWellBalanceAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null && result.contains("@")) {
                    String splitArray[] = result.split("@");
                    if (splitArray.length > 0) {
                        if ((splitArray[0].equalsIgnoreCase("\r\n200")) || (splitArray[0].equalsIgnoreCase("200"))) {

                            StringBuilder stringBuilder = new StringBuilder();
                            String[] balance = splitArray[1].split("[.]");
                            String round = balance[0];
                            String fraction = balance[1];
                            fraction = (String) fraction.subSequence(0, 2);
                            stringBuilder.append(round).append(".").append(fraction);
                            mAppHandler.setPWBalance(stringBuilder.toString());
                            mAppHandler.setRID(splitArray[3]);

                            mPBAppLoading.setVisibility(View.GONE);
                            startActivity(new Intent(AppLoadingActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Snackbar snackbar = Snackbar.make(mRelativeLayout, splitArray[1], Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }

    private void CheckPWBalance() {
        new PWBalanceAsync().execute(
                getResources().getString(R.string.pw_bal),
                "imei_no=" + mAppHandler.getImeiNo());
    }

    private class PWBalanceAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {

        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null && result.contains("@")) {
                    String splitArray[] = result.split("@");
                    if (splitArray.length > 0) {
                        if ((splitArray[0].equalsIgnoreCase("\r\n200")) || (splitArray[0].equalsIgnoreCase("200"))) {

                            StringBuilder stringBuilder = new StringBuilder();
                            String[] balance = splitArray[1].split("[.]");
                            String round = balance[0];
                            String fraction = balance[1];
                            fraction = (String) fraction.subSequence(0, 2);
                            stringBuilder.append(round).append(".").append(fraction);
                            mAppHandler.setPWBalance(stringBuilder.toString());
                            mAppHandler.setRID(splitArray[3]);

                            mPBAppLoading.setVisibility(View.GONE);
                        } else {
                            Snackbar snackbar = Snackbar.make(mRelativeLayout, splitArray[1], Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }

    public void onClickRetry(View v) {
        startActivity(new Intent(AppLoadingActivity.this, AppLoadingActivity.class));
        finish();
    }
}
