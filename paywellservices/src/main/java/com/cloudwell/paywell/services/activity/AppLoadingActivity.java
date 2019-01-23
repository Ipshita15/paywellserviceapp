package com.cloudwell.paywell.services.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity;
import com.cloudwell.paywell.services.activity.reg.missing.MissingMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.TelephonyInfo;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AppLoadingActivity extends AppCompatActivity {
    private static final String TAG = AppLoadingActivity.class.getName();
    private static final int REQUEST_PHONE_STATE = 1;
    private ProgressBar mPBAppLoading;
    private RelativeLayout mRelativeLayout;
    private TextView mConErrorMsg;
    private Button mBtnRetry;
    private AppHandler mAppHandler;
    private String mImeiNo;
    private ConnectionDetector mCd;
    public static String versionName = null;
    public static String androidVersionName = null;
    public static String pendingStr;
    boolean flag = true;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        mRelativeLayout = findViewById(R.id.linearLayout);
        mConErrorMsg = findViewById(R.id.connErrorMsg);
        mBtnRetry = findViewById(R.id.btnRetry);
        mPBAppLoading = findViewById(R.id.pbAppLoading);
        Button btnClose = findViewById(R.id.btnClose);

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());


        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mConErrorMsg.setTypeface(AppController.getInstance().getOxygenLightFont());
            mBtnRetry.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnClose.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mConErrorMsg.setTypeface(AppController.getInstance().getAponaLohitFont());
            mBtnRetry.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnClose.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            if (mAppHandler.getAppStatus().equalsIgnoreCase("registered")) {
                RegisteredUserLogin();
                Handler handler = new Handler();
                Runnable myRunnable = new Runnable() {
                    public void run() {
                        Intent i = new Intent(AppLoadingActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                };
                handler.postDelayed(myRunnable, 100);
            } else {
                RegisterUser();
            }
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
                if (mAppHandler.getAppStatus().equalsIgnoreCase("registered")) {
                    RegisteredUserLogin();
                    Handler handler = new Handler();
                    Runnable myRunnable = new Runnable() {
                        public void run() {
                            Intent i = new Intent(AppLoadingActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    };
                    handler.postDelayed(myRunnable, 100);
                } else {
                    RegisterUser();
                }
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int phnStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int accessFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
//        int readSmsPermission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_SMS);
//        int receiveSmsPermission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECEIVE_SMS);
        int cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int callPhnPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        int readContactsPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (phnStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (accessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
//        if (readSmsPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
//        }
//        if (receiveSmsPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
//        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (callPhnPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (readContactsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        //below
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        return true;
    }

    private void RegisterUser() {
        if (!mCd.isConnectingToInternet()) {
            mConErrorMsg.setVisibility(View.VISIBLE);
            mBtnRetry.setVisibility(View.VISIBLE);
            mPBAppLoading.setVisibility(View.GONE);
        } else {
            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                androidVersionName = field.getName();
            }
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
            mImeiNo = telephonyInfo.getImeiSIM1();
            mAppHandler.setImeiNo(mImeiNo);
            new AuthAsync().execute(getResources().getString(R.string.pw_auth), "" + getVersionName());
        }
    }

    private void RegisteredUserLogin() {
        if (!mCd.isConnectingToInternet()) {
            mConErrorMsg.setVisibility(View.VISIBLE);
            mBtnRetry.setVisibility(View.VISIBLE);
            mPBAppLoading.setVisibility(View.GONE);
        } else {
            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                androidVersionName = field.getName();
            }
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
            mImeiNo = telephonyInfo.getImeiSIM1();
            mAppHandler.setImeiNo(mImeiNo);
            new AuthenticationAsync().execute(getResources().getString(R.string.pw_auth), "" + getVersionName());
        }
    }

    private void requestPhonePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
    }

    private String getVersionName() {
        String _versionName = null;
        try {
            _versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionName = _versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Version Code not available"); // There was a problem with the code retrieval
        } catch (NullPointerException e) {
            Log.e(TAG, "Context is null");
        }
        return versionName; // Found the code!
    }

    @SuppressWarnings("deprecation")
    private class AuthAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("imei_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("version_no", params[1]));
                nameValuePairs.add(new BasicNameValuePair("version_name", androidVersionName));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
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
            try {
                if (result != null) {
                    ArrayList<String> imgLink = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {

                        mAppHandler.setAppStatus("registered");
                        String rid = jsonObject.getString("retailer_code");
                        mAppHandler.setRID(rid);

                        String sme = jsonObject.getString("SME");
                        String changePinStatus = jsonObject.getString("changePinStatus");
                        String changePhnStatus = jsonObject.getString("isPhoneVerfied");
                        String changeBTypeStatus = jsonObject.getString("businessTypeStatus");
                        String displayPictureCount = jsonObject.getString("displayPictureCount");
                        mAppHandler.setDisplayPictureCount(Integer.parseInt(displayPictureCount));

                        String mobileNumber = jsonObject.getString("mobile_number");
                        mAppHandler.setMobileNumber(mobileNumber);


                        mAppHandler.displayPictureArray = new String[Integer.parseInt(displayPictureCount)];
                        JSONArray pictureArrayJson = jsonObject.getJSONArray("imageLink");
                        for(int i = 0; i < pictureArrayJson.length(); i++) {
                            String disImglink =  pictureArrayJson.getString(i);
                            if(disImglink.isEmpty()) {
                                mAppHandler.displayPictureArray[i] = "";
                                imgLink.add("");
                            } else {
                                mAppHandler.displayPictureArray[i] = disImglink;
                                imgLink.add(disImglink);
                            }
                        }
                        mAppHandler.setDisplayPictureArrayList(imgLink);

                        if (sme.equalsIgnoreCase("0")) {
                            mAppHandler.setGatewayId("1");
                        } else if (sme.equalsIgnoreCase("1")) {
                            mAppHandler.setGatewayId("4");
                        }

                        if (!changePinStatus.equalsIgnoreCase("0")) {
                            mAppHandler.setInitialChangePinStatus("true");
                        }

                        if (changePhnStatus.equalsIgnoreCase("0")) {
                            mAppHandler.setPhnNumberVerificationStatus("false");
                        } else {
                            mAppHandler.setPhnNumberVerificationStatus("verified");
                        }

                        if (changeBTypeStatus.equalsIgnoreCase("0")) {
                            mAppHandler.setMerchantTypeVerificationStatus("false");
                        } else {
                            mAppHandler.setMerchantTypeVerificationStatus("verified");
                        }
                        mPBAppLoading.setVisibility(View.GONE);
                        Intent i = new Intent(AppLoadingActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (status.equalsIgnoreCase("502")) {
                        mAppHandler.setAppStatus("unregistered");
                        mPBAppLoading.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), EntryMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equalsIgnoreCase("100")) {
                        mAppHandler.setAppStatus("pending");
                        mPBAppLoading.setVisibility(View.GONE);
                        MissingMainActivity.RESPONSE_DETAILS = result;
                        Intent intent = new Intent(getApplicationContext(), MissingMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equalsIgnoreCase("309")
                            || status.equalsIgnoreCase("360")
                            || status.equalsIgnoreCase("400")) {

                        pendingStr = jsonObject.getString("message");
                        mPBAppLoading.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), PendingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActivityCompat.finishAffinity(AppLoadingActivity.this);
                        startActivity(intent);
                        finish();
                    } else {
                        mConErrorMsg.setText(R.string.try_again_msg);
                        mConErrorMsg.setVisibility(View.VISIBLE);
                        mBtnRetry.setVisibility(View.VISIBLE);
                        mPBAppLoading.setVisibility(View.GONE);
                    }
                } else {
                    mConErrorMsg.setText(R.string.conn_timeout_msg);
                    mConErrorMsg.setVisibility(View.VISIBLE);
                    mBtnRetry.setVisibility(View.VISIBLE);
                    mPBAppLoading.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                mConErrorMsg.setText(R.string.try_again_msg);
                mConErrorMsg.setVisibility(View.VISIBLE);
                mBtnRetry.setVisibility(View.VISIBLE);
                mPBAppLoading.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private class AuthenticationAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("imei_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("version_no", params[1]));
                nameValuePairs.add(new BasicNameValuePair("version_name", androidVersionName));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
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
            try {
                if (result != null) {
                    ArrayList<String> imgLink = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {

                        mAppHandler.setAppStatus("registered");
                        String rid = jsonObject.getString("retailer_code");
                        mAppHandler.setRID(rid);

                        String sme = jsonObject.getString("SME");
                        String changePinStatus = jsonObject.getString("changePinStatus");
                        String changePhnStatus = jsonObject.getString("isPhoneVerfied");
                        String changeBTypeStatus = jsonObject.getString("businessTypeStatus");
                        String displayPictureCount = jsonObject.getString("displayPictureCount");
                        mAppHandler.setDisplayPictureCount(Integer.parseInt(displayPictureCount));

                        mAppHandler.displayPictureArray = new String[Integer.parseInt(displayPictureCount)];
                        JSONArray pictureArrayJson = jsonObject.getJSONArray("imageLink");
                        for(int i = 0; i < pictureArrayJson.length(); i++) {
                            String disImglink =  pictureArrayJson.getString(i);
                            if(disImglink.isEmpty()) {
                                mAppHandler.displayPictureArray[i] = "";
                                imgLink.add("");
                            } else {
                                mAppHandler.displayPictureArray[i] = disImglink;
                                imgLink.add(disImglink);
                            }
                        }
                        mAppHandler.setDisplayPictureArrayList(imgLink);

                        if (sme.equalsIgnoreCase("0")) {
                            mAppHandler.setGatewayId("1");
                        } else if (sme.equalsIgnoreCase("1")) {
                            mAppHandler.setGatewayId("4");
                        }

                        if (!changePinStatus.equalsIgnoreCase("0")) {
                            mAppHandler.setInitialChangePinStatus("true");
                        }

                        if (changePhnStatus.equalsIgnoreCase("0")) {
                            mAppHandler.setPhnNumberVerificationStatus("false");
                        } else {
                            mAppHandler.setPhnNumberVerificationStatus("verified");
                        }

                        if (changeBTypeStatus.equalsIgnoreCase("0")) {
                            mAppHandler.setMerchantTypeVerificationStatus("false");
                        } else {
                            mAppHandler.setMerchantTypeVerificationStatus("verified");
                        }
                        mPBAppLoading.setVisibility(View.GONE);
                    } else if (status.equalsIgnoreCase("502")) {
                        mAppHandler.setAppStatus("unregistered");
                        mPBAppLoading.setVisibility(View.GONE);
                        Intent intent = new Intent(AppLoadingActivity.this, EntryMainActivity.class);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(AppLoadingActivity.this);
                        finish();
                    } else if (status.equalsIgnoreCase("100")) {
                        mAppHandler.setAppStatus("pending");
                        mPBAppLoading.setVisibility(View.GONE);
                        MissingMainActivity.RESPONSE_DETAILS = result;
                        Intent intent = new Intent(AppLoadingActivity.this, MissingMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equalsIgnoreCase("309")
                            || status.equalsIgnoreCase("360")
                            || status.equalsIgnoreCase("400")) {
                        mAppHandler.setAppStatus("pending");
                        mPBAppLoading.setVisibility(View.GONE);
                        pendingStr = jsonObject.getString("message");
                        Intent intent = new Intent(getApplicationContext(), PendingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActivityCompat.finishAffinity(AppLoadingActivity.this);
                        startActivity(intent);
                        finish();
                    } else {
                        mConErrorMsg.setText(R.string.try_again_msg);
                        mConErrorMsg.setVisibility(View.VISIBLE);
                        mBtnRetry.setVisibility(View.VISIBLE);
                        mPBAppLoading.setVisibility(View.GONE);
                    }
                } else {
                    mConErrorMsg.setText(R.string.conn_timeout_msg);
                    mConErrorMsg.setVisibility(View.VISIBLE);
                    mBtnRetry.setVisibility(View.VISIBLE);
                    mPBAppLoading.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mConErrorMsg.setText(R.string.try_again_msg);
                mConErrorMsg.setVisibility(View.VISIBLE);
                mBtnRetry.setVisibility(View.VISIBLE);
                mPBAppLoading.setVisibility(View.GONE);
            }
        }
    }

    public void onClickRetry(View v) {
        startActivity(new Intent(AppLoadingActivity.this, AppLoadingActivity.class));
        finish();
    }

    public void onClickClose(View v) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
                    mImeiNo = telephonyInfo.getImeiSIM1();
                    mAppHandler.setImeiNo(mImeiNo);
                    if (!mCd.isConnectingToInternet()) {
                        mAppHandler.showDialog(getSupportFragmentManager());
                    } else {
                        new AuthAsync().execute(getResources().getString(R.string.pw_auth), "" + getVersionName());
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, "Permission denied", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    requestPhonePermission();
                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    RegisterUser();
                } else {
                    mConErrorMsg.setText(R.string.permission_error_msg);
                    mConErrorMsg.setVisibility(View.VISIBLE);
                    mBtnRetry.setVisibility(View.VISIBLE);
                    mPBAppLoading.setVisibility(View.GONE);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
