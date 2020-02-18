package com.cloudwell.paywell.services.activity.refill.banktransfer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.model.DistrictData;
import com.cloudwell.paywell.services.activity.refill.model.RequestDistrict;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BankTransferMainActivity extends BaseActivity {

    private static String KEY_TAG = BankTransferMainActivity.class.getName();

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private CoordinatorLayout mCoordinateLayout;
    Button btnBrac, btnDbbl, btnIbbl, btnPbl, btnScb, btnCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_refill_bank);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mCoordinateLayout = findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(AppController.getContext());

        TextView textView = findViewById(R.id.detailsText);
        btnBrac = findViewById(R.id.homeBtnBrac);
        btnDbbl = findViewById(R.id.homeBtnDbbl);
        btnIbbl = findViewById(R.id.homeBtnIbbl);
        btnPbl = findViewById(R.id.homeBtnPbl);
        btnScb = findViewById(R.id.homeBtnScb);
        btnCity = findViewById(R.id.homeBtnCity);

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

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_BALANCE_REFILL_BANK);

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

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnBrac:
                getDistrictList("1", btnBrac.getText().toString());
                break;
            case R.id.homeBtnDbbl:
                getDistrictList("2", btnDbbl.getText().toString());
                break;
            case R.id.homeBtnIbbl:
                getDistrictList("3", btnIbbl.getText().toString());
                break;
            case R.id.homeBtnPbl:
                getDistrictList("4", btnPbl.getText().toString());
                break;
            case R.id.homeBtnScb:
                getDistrictList("5", btnScb.getText().toString());
                break;
            case R.id.homeBtnCity:
                getDistrictList("6", btnCity.getText().toString());
                break;
            default:
                break;
        }
    }

    private void getDistrictList(String bankId, String bankName) {
        showProgressDialog();

        final RequestDistrict requestDistrict = new RequestDistrict();
        requestDistrict.setmUsername("" + mAppHandler.getUserName());
        requestDistrict.setmBankId("" + bankId);

        Call<DistrictData> responseBodyCall = ApiUtils.getAPIService().callDistrictDataAPI(requestDistrict.getmUsername(), requestDistrict.getmBankId());

        responseBodyCall.enqueue(new Callback<DistrictData>() {
            @Override
            public void onResponse(Call<DistrictData> call, Response<DistrictData> response) {
                dismissProgressDialog();
                Bundle bundle = new Bundle();
                bundle.putString("bankId", requestDistrict.getmBankId());
                bundle.putString("bankName", bankName);

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
