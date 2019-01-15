package com.cloudwell.paywell.services.activity.refill.banktransfer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.activity.refill.model.DistrictData;
import com.cloudwell.paywell.services.activity.refill.model.RequestDistrict;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BankTransferMainActivity extends BaseActivity {

    private static String KEY_TAG = BankTransferMainActivity.class.getName();

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private CoordinatorLayout mCoordinateLayout;
    private String bank_name = null;
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_BANK_NAME = "Bank_Name";
    private static final String TAG_ACCOUNT_NAME = "Account_Name";
    private static final String TAG_ACCOUNT_NO = "accountno";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_MESSAGE = "message";
    private String strImage = "";
    private String bankName;
    private String bankNo;
    private static final int PERMISSION_FOR_GALLERY = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_refill_bank);
        }
        mAppHandler = new AppHandler(this);
        mCoordinateLayout = findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(AppController.getContext());

        TextView textView = findViewById(R.id.detailsText);
        Button btnBrac = findViewById(R.id.homeBtnBrac);
        Button btnDbbl = findViewById(R.id.homeBtnDbbl);
        Button btnIbbl = findViewById(R.id.homeBtnIbbl);
        Button btnPbl = findViewById(R.id.homeBtnPbl);
        Button btnScb = findViewById(R.id.homeBtnScb);
        Button btnCity = findViewById(R.id.homeBtnCity);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            textView.setTypeface(AppController.getInstance().getOxygenLightFont());

            btnBrac.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnDbbl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnIbbl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPbl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnScb.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnCity.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            textView.setTypeface(AppController.getInstance().getAponaLohitFont());

            btnBrac.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnDbbl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnIbbl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPbl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnScb.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnCity.setTypeface(AppController.getInstance().getAponaLohitFont());
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
        Intent intent = new Intent(BankTransferMainActivity.this, RefillBalanceMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnBrac:
                bank_name = "BRAC";
//                startActivity(new Intent(BankTransferMainActivity.this, BankDetailsActivity.class));
//                showInformation(bank_name);
                getDistrictList("1");
                break;
            case R.id.homeBtnDbbl:
//                bank_name = "DBBL";
//                showInformation(bank_name);
                getDistrictList("2");
                break;
            case R.id.homeBtnIbbl:
//                bank_name = "IBBL";
//                showInformation(bank_name);
                getDistrictList("3");
                break;
            case R.id.homeBtnPbl:
//                bank_name = "PBL";
//                showInformation(bank_name);
                getDistrictList("4");
                break;
            case R.id.homeBtnScb:
//                bank_name = "SCB";
//                showInformation(bank_name);
                getDistrictList("5");
                break;
            case R.id.homeBtnCity:
//                bank_name = "City";
//                showInformation(bank_name);
                getDistrictList("6");
                break;
            default:
                break;
        }
    }

    private void getDistrictList(String bankId) {
        showProgressDialog();

        final RequestDistrict requestDistrict = new RequestDistrict();
        requestDistrict.setmUsername("" + mAppHandler.getImeiNo());
        requestDistrict.setmBankId("" + bankId);

        Call<DistrictData> responseBodyCall = ApiUtils.getAPIService().callDistrictDataAPI(requestDistrict.getmUsername(), requestDistrict.getmBankId());

        responseBodyCall.enqueue(new Callback<DistrictData>() {
            @Override
            public void onResponse(Call<DistrictData> call, Response<DistrictData> response) {
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                bundle.putString("bankId", requestDistrict.getmBankId());

                BankDetailsActivity.responseDistrictData = response.body();
                startBankDetailsActivity(bundle);
            }

            @Override
            public void onFailure(Call<DistrictData> call, Throwable t) {
                dismissProgressDialog();
                Log.d(KEY_TAG, "onFailure:");
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });
    }

    private void startBankDetailsActivity(Bundle bundle) {
        Intent intent = new Intent(BankTransferMainActivity.this, BankDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
