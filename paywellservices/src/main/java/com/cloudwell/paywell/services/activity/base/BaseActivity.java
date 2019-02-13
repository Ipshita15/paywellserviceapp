package com.cloudwell.paywell.services.activity.base;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import java.util.Locale;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    boolean isFlowFromFavorite;
    private ConnectionDetector mCd;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        isFlowFromFavorite = getIntent().getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE, false);
        mCd = new ConnectionDetector(AppController.getContext());
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

    public void switchToCzLocale(Locale locale) {
        Locale mLocale = locale;
        Configuration config = getBaseContext().getResources()
                .getConfiguration();
        Locale.setDefault(mLocale);
        config.setLocale(mLocale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
