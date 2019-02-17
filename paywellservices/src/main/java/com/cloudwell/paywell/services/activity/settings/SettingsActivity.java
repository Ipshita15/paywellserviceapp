package com.cloudwell.paywell.services.activity.settings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.location.LocationActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.UpdateChecker;

import java.util.Locale;

import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA;
import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_ENGLISH;

public class SettingsActivity extends BaseActivity {

    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;
    private UpdateChecker mUpdateChecker;
    private Button home_change_pin, home_upgrade, home_password_reset, home_change_language, home_help;
    private String selectedOption = "";
    private int flag = 0;

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRelativeLayout = findViewById(R.id.linearLayout);
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        /*Buttons Initialization*/
        home_change_pin = findViewById(R.id.homeBtnChangPin);
        home_upgrade = findViewById(R.id.homeBtnUpgrade);
        home_password_reset = findViewById(R.id.homeBtnPasswordReset);
        home_change_language = findViewById(R.id.homeBtnChangeLanguage);
        home_help = findViewById(R.id.homeBtnHelp);

        RefreshStringsOfButton();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_SETTINGS_HELP_MENU);
    }

    public void onSettingsButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnChangPin:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_SETTINGS_MENU, AnalyticsParameters.KEY_SETTINGS_CHANGE_PIN_MENU);
                startActivity(new Intent(this, ChangePinActivity.class));
                break;
            case R.id.homeBtnUpgrade:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_SETTINGS_MENU, AnalyticsParameters.KEY_SETTINGS_UPGRADE_MENU);
                checkPermission();
                break;
            case R.id.homeBtnPasswordReset:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_SETTINGS_MENU, AnalyticsParameters.KEY_SETTINGS_RESET_PIN_MENU);
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
                break;
            case R.id.homeBtnChangeLanguage:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_SETTINGS_MENU, AnalyticsParameters.KEY_SETTINGS_CHANGE_LANGUAGE_MENU);
                ShowLanguagePrompt();
                break;
            case R.id.homeBtnHelp:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_SETTINGS_MENU, AnalyticsParameters.KEY_SETTINGS_HELP_MENU);
                startHelpMenu();
                break;
//            case R.id.homeBtnLocation:
//                checkLocationPermission();
//                break;
            default:
                break;
        }
    }

    private void startHelpMenu() {
        startActivity(new Intent(SettingsActivity.this, HelpMainActivity.class));
        finish();
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
        builder.setPositiveButton(R.string.play_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                mUpdateChecker.launchMarketDetails();
            }
        });
        builder.setNeutralButton(R.string.pw_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
    }

    private void checkPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            checkForUpgrade();
        }
    }

    private void checkLocationPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            startActivity(new Intent(SettingsActivity.this, LocationActivity.class));
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
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
                break;
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    startActivity(new Intent(SettingsActivity.this, LocationActivity.class));
                    finish();
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    checkLocationPermission();
                }
                break;
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
        if (flag == 1) {
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
                selectedOption = languageTypes[position];
            }

        });

        // Button OK
        builder.setPositiveButton(R.string.okay_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (selectedOption.isEmpty()) {
                            selectedOption = languageTypes[option];
                        }
                        if (selectedOption.equalsIgnoreCase("English")) {
                            mAppHandler.setAppLanguage("en");
                            switchToCzLocale(new Locale(KEY_ENGLISH, ""));
                        } else {
                            mAppHandler.setAppLanguage("bn");
                            switchToCzLocale(new Locale(KEY_BANGLA, ""));
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
        home_help.setText(R.string.home_settings_help);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_change_pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_upgrade.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_password_reset.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_change_language.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_help.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_change_pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_upgrade.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_password_reset.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_change_language.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_help.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }
}
