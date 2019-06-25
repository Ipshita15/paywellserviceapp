package com.cloudwell.paywell.services.activity.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity;
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationFeeFragment;
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment;
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.google.gson.JsonObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
public class AirTricketBaseActivity extends MVVMBaseActivity {
    AppHandler mAppHandler;

    public static String KEY_CANCEL = "Cancel Request";
    public static String KEY_ReIssue = "ReIssue Request";
    public static String KEY_ReSchedule = "ReSchedule Request";
    public Double mCancellationFee = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchToCzLocale(Locale.ENGLISH);
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        changeAppTheme();

    }

    private void changeAppTheme() {
        boolean isEnglish = mAppHandler.getAppLanguage().equalsIgnoreCase("en");
        if (isEnglish) {
            setTheme(R.style.EnglishAppTheme);
        } else {
            setTheme(R.style.EnglishAppTheme);
        }
    }


    public void changeAppBaseTheme() {
        setTheme(R.style.AppTheme);
    }


    public void switchToCzLocale(Locale locale) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void callCancelMapping(String userName, String bookingId, String reason, String typeOfRequest) {

        showProgressDialog();

        ApiUtils.getAPIService().getCancelMap(userName, bookingId).enqueue(new Callback<ResCancellationMapping>() {
            @Override
            public void onResponse(Call<ResCancellationMapping> call, Response<ResCancellationMapping> response) {
                dismissProgressDialog();
                assert response.body() != null;
                if (response.body().getStatus() == 200) {
                    showUserCancelData(bookingId, reason, response.body(), typeOfRequest);
                } else {
                    showSnackMessageWithTextMessage(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResCancellationMapping> call, Throwable t) {
                dismissProgressDialog();
                showSnackMessageWithTextMessage(getString(R.string.please_try_again));

            }


        });
    }

    private void showUserCancelData(String bookingId, String reason, ResCancellationMapping r, String typeOfRequest) {

        CancellationFeeFragment priceChangeFragment = new CancellationFeeFragment();
        CancellationFeeFragment.resCencelMaping = r;
        CancellationFeeFragment.type = typeOfRequest;

        priceChangeFragment.setOnClickHandlerTest((cancellationFee, type) -> {
            mCancellationFee = cancellationFee;
            hiddenSoftKeyboard();
            if (typeOfRequest.equals(KEY_CANCEL)) {
                askForPin(bookingId, reason);
            }
        });

        priceChangeFragment.show(getSupportFragmentManager(), "dialog");


    }


    public void askForPin(String bookingId, String cancelReason) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    dialogInterface.dismiss();
                    String PIN_NO = pinNoET.getText().toString();
                    if (isInternetConnection()) {

                        String userName = mAppHandler.getImeiNo();
                        submitCancelRequest(userName, PIN_NO, bookingId, cancelReason, "json");
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void submitCancelRequest(String userName, String pass, String bookingId, String cancelReason, String apiFormat) {

        showProgressDialog();

        ApiUtils.getAPIService().cancelBooking(userName, pass, bookingId, cancelReason, apiFormat).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    if (response.isSuccessful()) {
                        JsonObject jsonObject = response.body();
                        String message = jsonObject.get("message_details").getAsString();
                        if (jsonObject.get("status").getAsInt() == 200) {
                            showMsg(message);
                        } else {
                            showMsg(message);
                        }

                    }
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network error!!!", Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });
    }

    private void showMsg(String msg) {

        CancellationStatusMessageFragment priceChangeFragment = new CancellationStatusMessageFragment();
        CancellationStatusMessageFragment.message = msg;

        priceChangeFragment.show(getSupportFragmentManager(), "dialog");

    }


}
