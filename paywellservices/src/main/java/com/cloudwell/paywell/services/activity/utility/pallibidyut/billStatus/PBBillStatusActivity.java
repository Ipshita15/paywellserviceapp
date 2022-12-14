package com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.model.ResBIllStatus;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.model.ResponseDetails;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.RequestBillStatus;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PBBillStatusActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private EditText etPin, etAcc;
    private Spinner spnr_month, spnr_year;
    private Button btnConfirm;
    private int month = 0, year = 0;
    private String mPin, mAcc, mMonth, mYear;
    ImageView imageViewInfo;

    private static String KEY_TAG = PBBillStatusActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbrequest_bill_status);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_polli_home_bill_status);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_STATUS);

        addRecentUsedList();

    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pb_request_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_to_know_bill_status, 0, 14);
        addItemToRecentListInDB(recentUsedMenu);
    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        etPin = findViewById(R.id.etPBPin);
        etAcc = findViewById(R.id.etPBAccount);
        btnConfirm = findViewById(R.id.btnPBConfirm);

        spnr_month = findViewById(R.id.monthSpinner);
        spnr_year = findViewById(R.id.yearSpinner);

        imageViewInfo = findViewById(R.id.imageView_info);
        imageViewInfo.setOnClickListener(this);


        ArrayAdapter<CharSequence> month_adapter = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_month.setAdapter(month_adapter);
        spnr_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month = 0;
            }
        });

        List<String> dynamicTwoYear = DateUtils.INSTANCE.getDynamicTwoYear();
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter year_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dynamicTwoYear);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_year.setAdapter(year_adapter);
        spnr_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String year_str = String.valueOf(spnr_year.getSelectedItem());
                year = Integer.parseInt(year_str);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = 0;
            }
        });
        spnr_year.setSelection(1);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPBConfirm) {
            mPin = etPin.getText().toString().trim();
            mAcc = etAcc.getText().toString().trim();

            if (mPin.length() == 0) {
                etPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else if (etAcc.getText().toString().trim().length() < 8) {
                etAcc.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_error_msg) + "</font>"));
            } else if (month == 0 || year == 0) {
                if (month == 0) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.select_month_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.select_year_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                if (month < 10) {
                    mMonth = "0" + month;
                } else {
                    mMonth = String.valueOf(month);
                }
                mYear = String.valueOf(year);
                mMonth = spnr_month.getSelectedItem().toString().trim().substring(0, 3).toUpperCase();
                handleBillStatusCheckRequest();
            }
        } else if (v.getId() == R.id.imageView_info) {
            showBillImageGobal(R.drawable.ic_polli_biddut_sms_acc_no);

        }
    }

    private void handleBillStatusCheckRequest() {
        showProgressDialog();

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).getRID());

        final RequestBillStatus requestBillStatus = new RequestBillStatus();
        requestBillStatus.setmUsername("" + mAppHandler.getUserName());
        requestBillStatus.setmPassword("" + mPin);
        requestBillStatus.setmAccountNo("" + mAcc);
        requestBillStatus.setmMonth("" + mMonth);
        requestBillStatus.setmYear("" + mYear);
        requestBillStatus.setRefId( ""+ uniqueKey);
        requestBillStatus.setmFormat("json");

        Call<ResBIllStatus> responseBodyCall = ApiUtils.getAPIServiceV2().callBillStatusRequestAPI(requestBillStatus);

        responseBodyCall.enqueue(new Callback<ResBIllStatus>() {
            @Override
            public void onResponse(Call<ResBIllStatus> call, Response<ResBIllStatus> response) {
                dismissProgressDialog();

                ResBIllStatus m = response.body();

                if (response.code() == 200){
                    if (m.getApiStatus() == 200){
                        showReposeUI(m);
                    }  else {
                        showErrorMessagev1(m.getApiStatusName());
                    }
                }  else {
                    showErrorMessagev1(m.getApiStatusName());
                }

            }

            @Override
            public void onFailure(Call<ResBIllStatus> call, Throwable t) {
                dismissProgressDialog();
                showTryAgainDialog();
            }
        });
    }

    private void showReposeUI(ResBIllStatus response) {
        final ResponseDetails data = response.getResponseDetails();
        AlertDialog.Builder builder = new AlertDialog.Builder(PBBillStatusActivity.this);
        builder.setTitle("Message");
        builder.setMessage(getString(R.string.trx_id_des) + " " + data.getTrxId() + "\n\n" + getString(R.string.status_des) + " " + data.getMessage());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                if (data.getStatus() == 200 || data.getStatus() == 100) {
                    onBackPressed();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
}
