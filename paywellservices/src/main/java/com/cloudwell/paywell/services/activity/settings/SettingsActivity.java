package com.cloudwell.paywell.services.activity.settings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.UpdateChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by android on 7/19/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    private AppHandler mAppHandler;
    private ConnectionDetector mCd;
    private RelativeLayout mRelativeLayout;
    private UpdateChecker mUpdateChecker;
    private Button home_change_pin, home_upgrade, home_password_reset, home_change_language;
    String selectedOption = "";
    int flag = 0;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.home_settings);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        mCd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        /*Buttons Initialization*/
        home_change_pin = (Button) findViewById(R.id.homeBtnChangPin);
        home_upgrade = (Button) findViewById(R.id.homeBtnUpgrade);
        home_password_reset = (Button) findViewById(R.id.homeBtnPasswordReset);
        home_change_language = (Button) findViewById(R.id.homeBtnChangeLanguage);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn")) {
            home_upgrade.setPadding(0, 35, 0, 0);
        }

        ((Button) mRelativeLayout.findViewById(R.id.homeBtnChangPin)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mRelativeLayout.findViewById(R.id.homeBtnUpgrade)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mRelativeLayout.findViewById(R.id.homeBtnPasswordReset)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mRelativeLayout.findViewById(R.id.homeBtnChangeLanguage)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        RefreshStringsOfButton();
    }


    public void onSettingsButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnChangPin:
                startActivity(new Intent(this, ChangePinActivity.class));
                break;
            case R.id.homeBtnUpgrade:
                checkPermission();
                break;
            case R.id.homeBtnPasswordReset:
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(R.string.home_settings_reset_pin);
                builder.setMessage(R.string.reset_pin_msg);
                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(SettingsActivity.this, ResetPinActivity.class));
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                break;
            case R.id.homeBtnChangeLanguage:
                ShowLanguagePrompt();
                break;
            default:
                break;
        }
    }

    private void checkForUpgrade() {
        mUpdateChecker = new UpdateChecker(SettingsActivity.this);
        new Thread() {
            @Override
            public void run() {
                mUpdateChecker.checkForUpdateByVersionName(getResources().getString(R.string.check_version));
                if (mUpdateChecker.isUpdateAvailable()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            upgradeConfirmationBuilder();
                        }

                    });
                }
                if (!mUpdateChecker.isUpdateAvailable()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.no_update_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    });
                }
                mAppHandler.setUpdateCheck(System.currentTimeMillis() / 1000);
            }

        }.start();
    }

    private void upgradeConfirmationBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle(R.string.version_upgrade);
        builder.setMessage(R.string.upgrade_msg);
        builder.setPositiveButton(R.string.upgrade_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                mUpdateChecker.downloadAndInstall(getResources().getString(R.string.update_check));
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    private void checkPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            return;
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            checkForUpgrade();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    checkForUpgrade();
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    checkPermission();
                }
                return;
            }
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
        if(flag == 1) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void ShowLanguagePrompt() {
        final int option;
        final String[] languageTypes = {"English", "বাংলা"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.language_title_msg);
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")) {
            option = 1;
        } else {
            option = 0;
        }
        builder.setSingleChoiceItems(languageTypes, option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int position) {
                selectedOption = languageTypes[position].toString();
            }

        });

        // Button OK
        builder.setPositiveButton(R.string.okay_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (selectedOption.isEmpty()) {
                            selectedOption = languageTypes[option].toString();
                        }
                        if (selectedOption.equalsIgnoreCase("English")) {
                            mAppHandler.setAppLanguage("en");
                            Configuration config = new Configuration();
                            config.locale = Locale.ENGLISH;
                            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                        } else {
                            mAppHandler.setAppLanguage("bn");
                            Configuration config = new Configuration();
                            config.locale = Locale.FRANCE;
                            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                        }
                        invalidateOptionsMenu();
                        RefreshStringsOfButton();
                        flag = 1;
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void RefreshStringsOfButton() {
        getSupportActionBar().setTitle(R.string.home_settings);
        home_change_pin.setText(R.string.home_settings_change_pin);
        home_upgrade.setText(R.string.home_settings_upgrade);
        home_password_reset.setText(R.string.home_settings_reset_pin);
        home_change_language.setText(R.string.home_settings_change_language);
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn")) {
            home_upgrade.setPadding(0, 35, 0, 0);
        }
    }
}
