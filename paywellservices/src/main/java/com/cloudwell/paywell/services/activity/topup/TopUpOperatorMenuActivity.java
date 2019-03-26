package com.cloudwell.paywell.services.activity.topup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public class TopUpOperatorMenuActivity extends BaseActivity {

    private ScrollView scrollView;
    private Button homeBtnGp;
    private Button homeBtnSkitto;
    private Button homeBtnBl;
    private Button homeBtnRb;
    private Button homeBtnTt;
    private Button homeBtnAt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_topup_menu);
        setToolbar(getString(R.string.home_topup));

        initialization();
        refrashLanguage();

    }

    private void initialization() {
        homeBtnGp = (Button) findViewById(R.id.homeBtnGp);
        homeBtnSkitto = (Button) findViewById(R.id.homeBtnSkitto);
        homeBtnBl = (Button) findViewById(R.id.homeBtnBl);
        homeBtnRb = (Button) findViewById(R.id.homeBtnRb);
        homeBtnTt = (Button) findViewById(R.id.homeBtnTt);
        homeBtnAt = (Button) findViewById(R.id.homeBtnAt);
    }

    private void refrashLanguage() {
        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            homeBtnGp.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeBtnSkitto.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeBtnBl.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeBtnRb.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeBtnTt.setTypeface(AppController.getInstance().getOxygenLightFont());
            homeBtnAt.setTypeface(AppController.getInstance().getOxygenLightFont());


        } else {
            homeBtnGp.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeBtnSkitto.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeBtnBl.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeBtnRb.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeBtnTt.setTypeface(AppController.getInstance().getAponaLohitFont());
            homeBtnAt.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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
        Intent intent = new Intent(TopUpOperatorMenuActivity.this, TopupMenuActivity.class);
        startActivity(intent);
        finish();
    }


}
