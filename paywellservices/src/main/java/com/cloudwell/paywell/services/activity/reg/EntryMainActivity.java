package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;
import com.cloudwell.paywell.services.activity.reg.model.ReposeVerifyPhoneNumberForRegistration;
import com.cloudwell.paywell.services.activity.reg.model.RequestCheckRetailerPhoneNumber;
import com.cloudwell.paywell.services.activity.reg.regOtp.RegistionOtpActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryMainActivity extends BaseActivity {

    private CheckBox checkBox;
    private LinearLayout mLinearLayout;
    public static RegistrationModel regModel;
    private EditText editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_main);

        editTextPhone = findViewById(R.id.editTextPhone);


        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("" + getString(R.string.txt_user_registration));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (regModel == null) {

            regModel = new RegistrationModel();
        }


        mLinearLayout = findViewById(R.id.layout);
        checkBox = findViewById(R.id.item_check);

        TextView textViewTerms = findViewById(R.id.textViewTerms);

        ((TextView) mLinearLayout.findViewById(R.id.textView_welcome)).setTypeface(AppController.getInstance().getAponaLohitFont());
        textViewTerms.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mLinearLayout.findViewById(R.id.btn_nextStep)).setTypeface(AppController.getInstance().getAponaLohitFont());

        textViewTerms.setPaintFlags(textViewTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermsAndConditionsActivity.class));

            }
        });
    }

    public void nextOnClick(View view) {


        if (editTextPhone.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.enter_your_mobile_number), Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        } else if (checkBox.isChecked()) {


            if (isInternetConnection()) {
                checkRetailerPhoneNumber(editTextPhone.getText().toString());

            } else {
                showErrorMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection));
            }

        } else {
            Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.check_roles_, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }


    public void itemClicked(View view) {
        checkBox = (CheckBox) view;
    }


    private void checkRetailerPhoneNumber(String mobileNumber) {
        showProgressDialog();

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).getRID());

        RequestCheckRetailerPhoneNumber reqeust = new RequestCheckRetailerPhoneNumber();
        reqeust.setPhoneNUmber(mobileNumber);

        ApiUtils.getAPIServicePHP7().checkRetailerPhoneNumber(reqeust).enqueue(new Callback<ReposeVerifyPhoneNumberForRegistration>() {
            @Override
            public void onResponse(Call<ReposeVerifyPhoneNumberForRegistration> call, Response<ReposeVerifyPhoneNumberForRegistration> response) {
                dismissProgressDialog();

                ReposeVerifyPhoneNumberForRegistration m = response.body();
                if (m.getStatus() == 205) {

                    regModel = new RegistrationModel();
                    regModel.setPhnNumber(mobileNumber);

                    Intent intent = new Intent(getApplicationContext(), RegistionOtpActivity.class);
                    intent.putExtra("phone_number", mobileNumber);
                    startActivity(intent);
                    finish();


                } else {
                    showErrorMessagev1(m.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ReposeVerifyPhoneNumberForRegistration> call, Throwable t) {
                dismissProgressDialog();

            }
        });


    }
}
