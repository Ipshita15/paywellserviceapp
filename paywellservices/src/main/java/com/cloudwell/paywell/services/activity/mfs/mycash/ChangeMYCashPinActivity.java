package com.cloudwell.paywell.services.activity.mfs.mycash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.RequestChangePInNumber;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeMYCashPinActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private EditText mOldPin;
    private EditText mNewPin;
    private Button mConfirm;
    private LinearLayout mLinearLayout;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mycash_pin);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_mycash_change_pin_title);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MYCASH_CHANGE_PIN_NUMBER);


    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.changePinLinearLayout);
        mOldPin = findViewById(R.id.oldPin);
        mNewPin = findViewById(R.id.newPin);
        mConfirm = findViewById(R.id.mycash_confirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mLinearLayout.findViewById(R.id.tvOldPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mOldPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvNewPin)).setTypeface(AppController.getInstance().getOxygenLightFont());
            mNewPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            ((Button) mLinearLayout.findViewById(R.id.mycash_confirm)).setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mLinearLayout.findViewById(R.id.tvOldPin)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mOldPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mLinearLayout.findViewById(R.id.tvNewPin)).setTypeface(AppController.getInstance().getAponaLohitFont());
            mNewPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            ((Button) mLinearLayout.findViewById(R.id.mycash_confirm)).setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        mConfirm.setOnClickListener(this);

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
    }

    public void resetPin(View v) {

    }

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            String _oldPin = mOldPin.getText().toString();
            String _newPin = mNewPin.getText().toString();
            if (_oldPin.length() == 0) {
                mOldPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.old_pin_error_msg) + "</font>"));
                return;
            }
            if (_newPin.length() == 0) {
                mNewPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.new_pin_error_msg) + "</font>"));
                return;
            }
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {

                requestChangePinNumber(_oldPin, _newPin);
            }
        }
    }

    private void requestChangePinNumber(String oldPin, String newPin) {

        showProgressDialog();
        RequestChangePInNumber m = new RequestChangePInNumber();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());
        m.setMyCashPin(oldPin);
        m.setNewMycashPin(newPin);
        m.setServiceType("AccountPINChange");

        ApiUtils.getAPIServiceV2().changePinNumber(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dismissProgressDialog();
                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString(TAG_STATUS);

                        if (status.equals("200")) {
                            String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMYCashPinActivity.this);
                            builder.setTitle("Result");
                            builder.setMessage(msg_text);
                            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    onBackPressed();
                                }
                            });
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.setCanceledOnTouchOutside(true);
                            alert.show();
                        } else {
                            String msg = jsonObject.getString(TAG_MESSAGE);
                            String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMYCashPinActivity.this);
                            builder.setMessage(msg + "\n" + msg_text);
                            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    onBackPressed();
                                }
                            });
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.setCanceledOnTouchOutside(true);
                            alert.show();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });
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
        Intent intent = new Intent(ChangeMYCashPinActivity.this, MYCashMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
