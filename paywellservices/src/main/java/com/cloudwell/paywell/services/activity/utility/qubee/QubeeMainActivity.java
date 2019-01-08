package com.cloudwell.paywell.services.activity.utility.qubee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class QubeeMainActivity extends AppCompatActivity {

    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qubee_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_qubee);
        }

        mAppHandler = new AppHandler(this);

        Button btnRecharge = findViewById(R.id.homeBtnRecharge);
        Button btnInquiry = findViewById(R.id.homeBtnInquiry);
        Button btnAcc = findViewById(R.id.homeBtnWrongAccount);
        Button btnAmount = findViewById(R.id.homeBtnWrongAmount);
        Button btnPayment = findViewById(R.id.homeBtnPayment);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnRecharge.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnAcc.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPayment.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnRecharge.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnAcc.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPayment.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    @Override
    public void onBackPressed() {
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

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnRecharge:
                startActivity(new Intent(this, QubeeRechargeActivity.class));

                break;
            case R.id.homeBtnInquiry:
                startActivity(new Intent(this, InquiryActivity.class));

                break;
            case R.id.homeBtnWrongAccount:
                startActivity(new Intent(this, ComplainAccountActivity.class));

                break;
            case R.id.homeBtnWrongAmount:
                startActivity(new Intent(this, ComplainAmountActivity.class));

                break;
            case R.id.homeBtnPayment:
                startActivity(new Intent(this, ComplainTrxActivity.class));

                break;
            default:
                break;
        }
    }
}

