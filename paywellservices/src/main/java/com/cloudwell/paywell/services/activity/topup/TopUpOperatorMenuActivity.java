package com.cloudwell.paywell.services.activity.topup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudwell.paywell.services.BaseActivity;
import com.cloudwell.paywell.services.R;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class TopUpOperatorMenuActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_topup_menu);
        setToolbar(getString(R.string.home_topup));
    }

    public void onButtonClicker(View v) {
        OperatorType operatorType;
        switch (v.getId()) {
            case R.id.homeBtnGp:
                operatorType = OperatorType.GP;
                startTopUpMianactivity(operatorType);
                break;

            case R.id.homeBtnSkitto:
                operatorType = OperatorType.Skitto;
                startTopUpMianactivity(operatorType);
                break;
            case R.id.homeBtnRb:

                operatorType = OperatorType.ROBI;
                startTopUpMianactivity(operatorType);
                break;
            case R.id.homeBtnBl:
                operatorType = OperatorType.BANGLALINK;
                startTopUpMianactivity(operatorType);
                break;
            case R.id.homeBtnTt:
                operatorType = OperatorType.TELETALK;
                startTopUpMianactivity(operatorType);
                break;
            case R.id.homeBtnAt:
                operatorType = OperatorType.AIRTEL;
                startTopUpMianactivity(operatorType);
                break;
            default:
                break;

        }
    }

    private void startTopUpMianactivity(OperatorType gp) {
        Intent intent = new Intent(TopUpOperatorMenuActivity.this, TopupMainActivity.class);
        intent.putExtra("key", gp.getText());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TopUpOperatorMenuActivity.this, MainTopUpActivity.class);
        startActivity(intent);
        finish();
    }


}
