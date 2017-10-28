package com.cloudwell.paywell.services.activity.payments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.payments.bkash.BKashMainActivity;
import com.cloudwell.paywell.services.activity.payments.rocket.RocketMainActivity;
import com.cloudwell.paywell.services.activity.payments.statement.MyTransactionsActivity;

public class PaymentsMainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS_TRX = 100;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 101;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS_ROCKET = 102;
    CoordinatorLayout coordinateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_payments);
        }
        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBkash:
                startActivity(new Intent(this, BKashMainActivity.class));
                finish();
                break;
            case R.id.homeBtnRocket:
                checkRocketPermission();
                break;
            case R.id.homeBtnTrx:
                checkTrxPermission();
                break;
            default:
                break;
        }
    }

    private void checkRocketPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS_ROCKET);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            startActivity(new Intent(this, RocketMainActivity.class));
            finish();
        }
    }

    private void checkTrxPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS_TRX);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            startActivity(new Intent(this, MyTransactionsActivity.class));
            finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS_ROCKET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
                    } else {
                        // Android version is lesser than 6.0 or the permission is already granted.
                        startActivity(new Intent(this, RocketMainActivity.class));
                        finish();
                    }
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(coordinateLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case PERMISSIONS_REQUEST_READ_CONTACTS_TRX:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
                    } else {
                        // Android version is lesser than 6.0 or the permission is already granted.
                        startActivity(new Intent(this, MyTransactionsActivity.class));
                        finish();
                    }
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(coordinateLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case PERMISSIONS_REQUEST_PHONE_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS_TRX);
                    } else {
                        // Android version is lesser than 6.0 or the permission is already granted.
                        startActivity(new Intent(this, MyTransactionsActivity.class));
                        finish();
                    }
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(coordinateLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            default:
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
        Intent intent = new Intent(PaymentsMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
