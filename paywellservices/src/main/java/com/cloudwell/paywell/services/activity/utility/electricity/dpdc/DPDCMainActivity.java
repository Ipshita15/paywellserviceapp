package com.cloudwell.paywell.services.activity.utility.electricity.dpdc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.ElectricityHelpActivity;

public class DPDCMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpdcmain);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_mfs_dpdc);
        }
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBillPay:
                startActivity(new Intent(this, DPDCBillPayActivity.class));
                finish();
                break;
            case R.id.homeBtnInquiry:
                startActivity(new Intent(this, DPDCInquiryActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.help_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_help) {
            Intent intent = new Intent(DPDCMainActivity.this, ElectricityHelpActivity.class);
            intent.putExtra("serviceName", "DPDC");
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DPDCMainActivity.this, UtilityMainActivity.class);
        startActivity(intent);
        finish();
    }
}
