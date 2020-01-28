package com.cloudwell.paywell.services.activity.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.LanuageConstant;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;
    boolean isFlowFromFavorite;
    private ConnectionDetector mCd;
    private String selectedPhnNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFlowFromFavorite = getIntent().getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE, false);
        mCd = new ConnectionDetector(AppController.getContext());

        changeAppTheme();
    }


    private void changeAppTheme() {
        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());
        if (mAppHandler.getAppLanguage().equals("unknown")) {
            mAppHandler.setAppLanguage("bn");
            setTheme(R.style.BanglaAppTheme);
            switchToCzLocale(new Locale(LanuageConstant.KEY_ENGLISH, ""));
        } else if (mAppHandler.getAppLanguage().equals("en")) {
            mAppHandler.setAppLanguage("en");
            setTheme(R.style.EnglishAppTheme);
            switchToCzLocale(new Locale(LanuageConstant.KEY_ENGLISH, ""));
        } else if (mAppHandler.getAppLanguage().equals("bn")) {
            mAppHandler.setAppLanguage("bn");
            setTheme(R.style.BanglaAppTheme);
            switchToCzLocale(new Locale(LanuageConstant.KEY_BANGLA, ""));
        }

    }

    public void switchLanguage() {
        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());
        if (mAppHandler.getAppLanguage().equals("unknown")) {
            mAppHandler.setAppLanguage("bn");
            switchToCzLocale(new Locale(LanuageConstant.KEY_BANGLA, ""));
        } else if (mAppHandler.getAppLanguage().equals("en")) {
            mAppHandler.setAppLanguage("bn");
            switchToCzLocale(new Locale(LanuageConstant.KEY_BANGLA, ""));
        } else if (mAppHandler.getAppLanguage().equals("bn")) {
            mAppHandler.setAppLanguage("en");
            switchToCzLocale(new Locale(LanuageConstant.KEY_ENGLISH, ""));
        }
    }



    public void setToolbar(String title, int color) {
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            SpannableString spannablerTitle = new SpannableString(title);
            spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(spannablerTitle);
        }
    }

    public void setToolbar(String title) {
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }else if (item.getItemId() == R.id.item_air_ticket_call){
            callPreviewAirticket(true);

        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", this.getString(R.string.loading_msg), true);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (isFlowFromFavorite){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            return;
//        }
    }

    public boolean isInternetConnection() {
        return mCd.isConnectingToInternet();
    }


    public void showSnackMessageWithTextMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();

    }

    public void showServerErrorMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();

    }

    public void showNoInternetConnectionFound() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();

    }


    public void switchToCzLocale(Locale locale) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void hideUserKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hiddenSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builder.show();

    }

    public void showdialogMessageWithFinishedActivity(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });


        builder.show();

    }

    public void showDialogMesssageWithFinished(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });


        builder.show();

    }

    public void callPreview(boolean isAirTicket) {
        final String[] numbers = {"09610116566", "09666773333", "09666716566", "09638016566"};
        selectedPhnNo = "09610116566";
        AlertDialog dialog;
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        if (isAirTicket){
            builder.setTitle(getString(R.string.select_phn_title_air_ticket));
        }else {
            builder.setTitle(getString(R.string.select_phn_title_msg));
        }


        builder.setSingleChoiceItems(numbers, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectedPhnNo = numbers[arg1];
            }

        }).create();
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                call();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void callPreviewAirticket(boolean isAirTicket) {
        final String[] numbers = {"01787679660","09610116566", "09666773333", "09666716566", "09638016566"};
        selectedPhnNo = "01787679660";
        AlertDialog dialog;
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        if (isAirTicket){
            builder.setTitle(getString(R.string.select_phn_title_air_ticket));
        }else {
            builder.setTitle(getString(R.string.select_phn_title_msg));
        }


        builder.setSingleChoiceItems(numbers, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectedPhnNo = numbers[arg1];
            }

        }).create();
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                call();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void call() {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + selectedPhnNo));
        startActivity(intent);

    }

}
