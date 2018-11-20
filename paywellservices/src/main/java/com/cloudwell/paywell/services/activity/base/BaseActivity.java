package com.cloudwell.paywell.services.activity.base;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cloudwell.paywell.services.R;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

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
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
}
