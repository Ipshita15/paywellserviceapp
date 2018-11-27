package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

@SuppressWarnings("ALL")
public class BKashBalanceActivity extends AppCompatActivity {

    public static final String RESPONSE = "response";
    private TextView tvMainBalance;
    private RelativeLayout mRelativeLayout;
    private Button mButtonPurposeDeclare;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_balance);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_bkash_balance);
        }

        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        tvMainBalance = (TextView) findViewById(R.id.bKashBalance);
        mButtonPurposeDeclare = (Button) findViewById(R.id.btnPurposeDeclare);

        mAppHandler = new AppHandler(getApplicationContext());
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            tvMainBalance.setTypeface(AppController.getInstance().getOxygenLightFont());
            mButtonPurposeDeclare.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            tvMainBalance.setTypeface(AppController.getInstance().getAponaLohitFont());
            mButtonPurposeDeclare.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        String response = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            response = bundle.getString(RESPONSE);
        }

        String[] splittedArray = response.split("@");
        tvMainBalance.setText(getString(R.string.balance_des) + " " + String.valueOf(splittedArray[1]) + getString(R.string.tk)
                + "\n" + getString(R.string.bkash_frozen_balance_des) + " " + String.valueOf(splittedArray[2]) + getString(R.string.tk)
                + "\n" + getString(R.string.bkash_declare_balance_des) + " " + String.valueOf(splittedArray[3] + getString(R.string.tk))
                + "\n" + getString(R.string.bkash_verification_balance_des) + " " + String.valueOf(splittedArray[4] + getString(R.string.tk)));

        if (!String.valueOf(splittedArray[3]).equalsIgnoreCase("0.00")) {
            mButtonPurposeDeclare.setVisibility(View.VISIBLE);
        }
    }

    public void onClickDeclarePurpose(View view) {
        if (view == mButtonPurposeDeclare) {
            Intent intent = new Intent(BKashBalanceActivity.this, DeclarePurposeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BKashBalanceActivity.this, BKashMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
