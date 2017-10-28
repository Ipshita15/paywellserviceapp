package com.cloudwell.paywell.services.activity.utility.nid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;

import java.util.List;

public class NidMainActivity extends AppCompatActivity {

    private static final String NID_CARD = "National ID Card";
    private RelativeLayout mRelativeLayout;
    boolean hasNIDService = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nid_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility_nid_title);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        initView();
    }

    private void initView() {
        List<String> serviceList = AppLoadingActivity.getUtilityService();
        for (int i = 0; i < serviceList.size(); i++) {
            if (NID_CARD.equalsIgnoreCase(serviceList.get(i))) {
                hasNIDService = true;
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NidMainActivity.this, UtilityMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnCorrection:
                if (hasNIDService) {
                    startActivity(new Intent(this, NidActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnInquiry:
                if (hasNIDService) {
                    startActivity(new Intent(this, NidQueryActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
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
}
