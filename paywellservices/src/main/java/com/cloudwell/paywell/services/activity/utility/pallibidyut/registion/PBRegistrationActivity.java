package com.cloudwell.paywell.services.activity.utility.pallibidyut.registion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.REBNotification;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.model.RequestPBRegistioin;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.model.ResposePBReg;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.gson.Gson;

import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PBRegistrationActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPinNumber, mAccount, mConfirmAccount, mCustomerName, mPhone;
    private Button mSubmitButton;
    ImageView imageViewInfo;
    private ConnectionDetector cd;
    private String _pinNo, _accountNo, _customerName, _phone, dialogHeader;
    private int otp_check = 0;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private String transitionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pb_registration);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_pb_reg_title);
        }
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION_INQUIRY);


        addRecentUsedList();

    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pollibiddut_registion, StringConstant.KEY_home_utility, IconConstant.KEY_ic_registration,  0, 10);
        addItemToRecentListInDB(recentUsedMenu);
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.linearLayout);

        TextView _pin = findViewById(R.id.tvPBPin);
        TextView _accountNo = findViewById(R.id.tvPBAccount);
        TextView _accountConfirm = findViewById(R.id.tvPBAccountConfirm);
        TextView _customerName = findViewById(R.id.tvFullName);
        TextView _phone = findViewById(R.id.tvPhone);

        mPinNumber = findViewById(R.id.etPBPin);
        mAccount = findViewById(R.id.etPBAccount);
        mConfirmAccount = findViewById(R.id.etPBConfirmAccount);
        mCustomerName = findViewById(R.id.etFullName);
        mPhone = findViewById(R.id.etPhone);
        mSubmitButton = findViewById(R.id.btnPBConfirm);

        imageViewInfo = findViewById(R.id.imageView_info);
        imageViewInfo.setOnClickListener(this);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPinNumber.setTypeface(AppController.getInstance().getOxygenLightFont());
            _accountNo.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _accountConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirmAccount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _customerName.setTypeface(AppController.getInstance().getOxygenLightFont());
            mCustomerName.setTypeface(AppController.getInstance().getOxygenLightFont());
            _phone.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPhone.setTypeface(AppController.getInstance().getOxygenLightFont());
            mSubmitButton.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPinNumber.setTypeface(AppController.getInstance().getAponaLohitFont());
            _accountNo.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _accountConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirmAccount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _customerName.setTypeface(AppController.getInstance().getAponaLohitFont());
            mCustomerName.setTypeface(AppController.getInstance().getAponaLohitFont());
            _phone.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPhone.setTypeface(AppController.getInstance().getAponaLohitFont());
            mSubmitButton.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mSubmitButton.setOnClickListener(this);


        try {
            String data = getIntent().getStringExtra("REBNotification");
            if (!data.equals("")) {
                Gson gson = new Gson();
                REBNotification notificationDetailMessage = gson.fromJson(data, REBNotification.class);
                mAccount.setText("" + notificationDetailMessage.getTrxData().getAccountNo());
                mConfirmAccount.setText("" + notificationDetailMessage.getTrxData().getAccountNo());
                mCustomerName.setText("" + notificationDetailMessage.getTrxData().getCustomerName());
                mPhone.setText("" + notificationDetailMessage.getTrxData().getCustomerPhone());
                mSubmitButton.setText("" + getString(R.string.re_submit_reb));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        if (v == mSubmitButton) {
            if (!cd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                _pinNo = mPinNumber.getText().toString().trim();
                if (_pinNo.length() == 0) {
                    mPinNumber.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
                    return;
                }
                if (mAccount.getText().toString().trim().length() < 8) {
                    mAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_error_msg) + "</font>"));
                    return;
                }
                if (mAccount.getText().toString().trim().matches(mConfirmAccount.getText().toString().trim())) {
                    _accountNo = mAccount.getText().toString().trim();
                } else {
                    mConfirmAccount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_acc_mismatch_error_msg) + "</font>"));
                    return;
                }
                _customerName = mCustomerName.getText().toString().trim();
                if (_customerName.length() == 0) {
                    mCustomerName.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pb_full_name_error_msg) + "</font>"));
                    return;
                }
                _phone = mPhone.getText().toString().trim();
                if (_phone.length() < 11) {
                    mPhone.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                    return;
                }
                //new CheckOTPAsync().execute(getString(R.string.pb_reg));
                String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).getRID());

                request(_pinNo, _accountNo, _customerName, _phone, uniqueKey);
            }
        } else if (v.getId() == R.id.imageView_info) {
            showBillImageGobal(R.drawable.ic_polli_biddut_sms_acc_no);

        }
    }

    private void request(String _pinNo, String _accountNo, String _customerName, String _phone, String uniqueKey) {

        RequestPBRegistioin m = new RequestPBRegistioin();
        m.setUsername(AppHandler.getmInstance(getApplicationContext()).getUserName());
        m.setPassword(_pinNo);
        m.setAccoUntNo(_accountNo);
        m.setCustName(_customerName);
        m.setCustPHn(_phone);
        m.setRefId(uniqueKey);

        showProgressDialog();
        ApiUtils.getAPIServiceV2().pollybuddutRegistration(m).enqueue(new Callback<ResposePBReg>() {

            @Override
            public void onResponse(Call<ResposePBReg> call, Response<ResposePBReg> response) {
                dismissProgressDialog();
                try {
                    ResposePBReg m = response.body();
                    int statusCode = m.getApiStatus();
                    if (statusCode == 200) {
                        if (m.getApiStatus() == 200) {
                            showStatusDialogForRegtionSuccessfull(m.getResponseDetails().getMessage());
                        }else {
                            showStatusDialogForRegtionSuccessfull(m.getResponseDetails().getMessage());
                        }
                    } else {
                        showErrorMessagev1(m.getApiStatusName());
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(PBRegistrationActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResposePBReg> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(PBRegistrationActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void showStatusDialogForRegtionSuccessfull(String message) {

        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + _accountNo
                + "\n" + getString(R.string.cust_name_des) + " " + _customerName
                + "\n" + getString(R.string.phone_no_des) + " " + _phone);
        AlertDialog.Builder builder = new AlertDialog.Builder(PBRegistrationActivity.this);
        builder.setTitle("Result :" + message);
        builder.setMessage(reqStrBuilder.toString());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                finish();

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
