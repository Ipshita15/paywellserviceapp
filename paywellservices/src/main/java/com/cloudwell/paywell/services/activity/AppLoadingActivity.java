package com.cloudwell.paywell.services.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.home.HomeActivity;
import com.cloudwell.paywell.services.activity.home.model.ReposeUserProfile;
import com.cloudwell.paywell.services.activity.home.model.ResponseDetailsUserProfile;
import com.cloudwell.paywell.services.activity.reg.missing.MissingMainActivity;
import com.cloudwell.paywell.services.activity.reg.model.AuthRequestModel;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;
import com.cloudwell.paywell.services.activity.settings.ChangePinActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.APIService;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.TelephonyInfo;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppLoadingActivity extends BaseActivity {
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
    private RegistrationModel regModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_loading);

        mRelativeLayout = findViewById(R.id.linearLayout);
        mConErrorMsg = findViewById(R.id.connErrorMsg);
        mBtnRetry = findViewById(R.id.btnRetry);
        mPBAppLoading = findViewById(R.id.pbAppLoading);
        Button btnClose = findViewById(R.id.btnClose);

        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mCd = new ConnectionDetector(AppController.getContext());


        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            if (mAppHandler.getAppStatus().equalsIgnoreCase("registered")) {
                //RegisteredUserLogin();
                RegisterUser();
                Handler handler = new Handler();
                Runnable myRunnable = new Runnable() {
                    public void run() {
                        Intent i = new Intent(AppLoadingActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                };
                handler.postDelayed(myRunnable, 100);
            } else if (mAppHandler.getAppStatus().equalsIgnoreCase("registered_pin_not_set")) {
                Intent intent = new Intent(AppLoadingActivity.this, ChangePinActivity.class);
                intent.putExtra("isFirstTime", true);
                startActivity(intent);
                finish();

            } else {
                RegisterUser();
            }
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
                if (mAppHandler.getAppStatus().equalsIgnoreCase("registered")) {
                    //RegisteredUserLogin();
                    RegisterUser();
                    Handler handler = new Handler();
                    Runnable myRunnable = new Runnable() {
                        public void run() {
                            Intent i = new Intent(AppLoadingActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    };
                    handler.postDelayed(myRunnable, 100);
                } else if (mAppHandler.getAppStatus().equalsIgnoreCase("registered_pin_not_set")) {
                    Intent intent = new Intent(AppLoadingActivity.this, ChangePinActivity.class);
                    intent.putExtra("isFirstTime", true);
                    startActivity(intent);
                    finish();

                } else {
                    RegisterUser();
                }
            }
        }

        mBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppLoadingActivity.this, AppLoadingActivity.class));
                finish();
            }
        });

    }

    private boolean checkAndRequestPermissions() {
        int phnStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int accessFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

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
//        if (callPhnPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
//        }
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

//            new AuthAsync().execute(getResources().getString(R.string.pw_auth), "" + getVersionName());

            authAsync();
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
//                        new AuthAsync().execute(getResources().getString(R.string.pw_auth), "" + getVersionName());
                        authAsync();
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


    public void authAsync() {
        AuthRequestModel m = new AuthRequestModel();

        if (AppHandler.getmInstance(getApplicationContext()).isSuccessfulPassAuthenticationFlow()) {
            m.setUsername("" + mAppHandler.getUserName());
        } else if (AppHandler.getmInstance(getApplicationContext()).isSuccessfulPassRegistionFlow()) {
            m.setUsername("" + mAppHandler.getAndroidID());
        }


        m.setAppVersionName(getVersionName());
        m.setAppVersionNo(androidVersionName);


        APIService aPIService = ApiUtils.getAPIServiceV2();
        Call<ReposeUserProfile> responseBodyCall;
        if (AppHandler.getmInstance(getApplicationContext()).isSuccessfulPassRegistionFlow()) {
            responseBodyCall = aPIService.userServiceProfilingWithAuth(m);
        } else {
            responseBodyCall = aPIService.userServiceProfiling(m);
        }

        responseBodyCall.enqueue(new Callback<ReposeUserProfile>() {

            @Override
            public void onResponse(Call<ReposeUserProfile> call, Response<ReposeUserProfile> response) {

                try {
                    ReposeUserProfile result = response.body();

                    if (result.getApiStatus() == 200) {


                        if (result != null) {
                            ArrayList<String> imgLink = new ArrayList<>();

                            ResponseDetailsUserProfile details = result.getResponseDetails();

                            String status = "" + details.getMStatus();
                            if (status.equalsIgnoreCase("200")) {


                                String rid = details.getMRetailerCode();
                                int sme = details.getMSME();
                                String changePinStatus = details.getMChangePinStatus();
                                String changePhnStatus = details.getMIsPhoneVerfied();
                                String changeBTypeStatus = "" + details.getMBusinessTypeStatus();
                                int displayPictureCount = details.getMDisplayPictureCount();

                                mAppHandler.setRID(rid);

                                if (AppHandler.getmInstance(getApplicationContext()).isSuccessfulPassRegistionFlow()) {

                                    mConErrorMsg.setVisibility(View.VISIBLE);
                                    mBtnRetry.setVisibility(View.VISIBLE);
                                    mConErrorMsg.setText("Congratulation on your becoming a paywall merchant, Your RIDE is " + rid + " and your password already send your mobile number, You can login by user username and password, \n\nThank you");
                                    mAppHandler.setAppStatus("pendingLogin");

                                    AppHandler.getmInstance(getApplicationContext()).setIsSuccessfulPassRegistionFlow(false);

                                    mBtnRetry.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(AppLoadingActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                }else  if (changePinStatus.equalsIgnoreCase("0")) {
                                    mAppHandler.setInitialChangePinStatus("false");
                                    mAppHandler.setAppStatus("registered_pin_not_set");

                                    Intent intent = new Intent(AppLoadingActivity.this, ChangePinActivity.class);
                                    intent.putExtra("isFirstTime", true);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    mAppHandler.setAppStatus("registered");

                                    mAppHandler.setDisplayPictureCount(displayPictureCount);

                                    String mobileNumber = details.getMMobileNumber();
                                    mAppHandler.setMobileNumber(mobileNumber);


                                    mAppHandler.displayPictureArray = new String[displayPictureCount];

                                    List<String> pictureArrayJson = details.getMImageLink();
                                    mAppHandler.setPictureArrayImageLink(pictureArrayJson.toString());

                                    for (int i = 0; i < pictureArrayJson.size(); i++) {
                                        String disImglink = pictureArrayJson.get(i);
                                        if (disImglink.isEmpty()) {
                                            mAppHandler.displayPictureArray[i] = "";
                                            imgLink.add("");
                                        } else {
                                            mAppHandler.displayPictureArray[i] = disImglink;
                                            imgLink.add(disImglink);
                                        }
                                    }
                                    mAppHandler.setDisplayPictureArrayList(imgLink);

                                    if (sme == 0) {
                                        mAppHandler.setGatewayId("1");
                                    } else if (sme == 1) {
                                        mAppHandler.setGatewayId("4");
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
                                }



                            } else if (status.equalsIgnoreCase("502")) {
                                mAppHandler.setAppStatus("unregistered");
                                pendingStr = details.getMStatusName();
                                mPBAppLoading.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), PendingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ActivityCompat.finishAffinity(AppLoadingActivity.this);
                                startActivity(intent);
                                finish();
                            } else if (status.equalsIgnoreCase("100")) {
                                mAppHandler.setAppStatus("pending");
                                mPBAppLoading.setVisibility(View.GONE);
                                MissingMainActivity.RESPONSE_DETAILS = new Gson().toJson(details);
                                regModel = new RegistrationModel();
                                Intent intent = new Intent(getApplicationContext(), MissingMainActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (status.equalsIgnoreCase("309")
                                    || status.equalsIgnoreCase("360")
                                    || status.equalsIgnoreCase("400")) {

                                pendingStr = details.getMStatusName();
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


                    } else {
                        mConErrorMsg.setText(result.getApiStatusName());
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

            @Override
            public void onFailure(Call<ReposeUserProfile> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
