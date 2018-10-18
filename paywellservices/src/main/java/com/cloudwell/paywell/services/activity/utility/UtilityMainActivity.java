package com.cloudwell.paywell.services.activity.utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBMainActivity;
import com.cloudwell.paywell.services.activity.utility.qubee.QubeeMainActivity;
import com.cloudwell.paywell.services.activity.utility.realvu.BeximcoMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class UtilityMainActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mAppHandler = new AppHandler(this);

        Button btnDesco = findViewById(R.id.homeBtnDESCO);
        Button btnDpdc = findViewById(R.id.homeBtnDPDC);
        Button btnWasa = findViewById(R.id.homeBtnWasa);
        Button btnWzpdcl = findViewById(R.id.homeBtnWestZone);
        Button btnPolli = findViewById(R.id.homeBtnPolliBiddut);
        Button btnQubee = findViewById(R.id.homeBtnQubee);
        Button btnReal = findViewById(R.id.homeBtnRealVU);
        Button btnIvac = findViewById(R.id.homeBtnIvac);
        Button btnBanglalion = findViewById(R.id.homeBtnBanglalion);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnDesco.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnDpdc.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnWasa.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnWzpdcl.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnPolli.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnQubee.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnReal.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnIvac.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBanglalion.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnDesco.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnDpdc.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnWasa.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnWzpdcl.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnPolli.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnQubee.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnReal.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnIvac.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBanglalion.setTypeface(AppController.getInstance().getAponaLohitFont());
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
        Intent intent = new Intent(UtilityMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnDESCO:
                startActivity(new Intent(this, DESCOMainActivity.class));
                break;
            case R.id.homeBtnDPDC:
                startActivity(new Intent(this, DPDCMainActivity.class));
                break;
            case R.id.homeBtnWasa:
                startActivity(new Intent(this, WASAMainActivity.class));
                break;
            case R.id.homeBtnWestZone:
                startActivity(new Intent(this, WZPDCLMainActivity.class));
                break;
            case R.id.homeBtnPolliBiddut:
                startActivity(new Intent(this, PBMainActivity.class));
                finish();
                break;
            case R.id.homeBtnQubee:
                startActivity(new Intent(this, QubeeMainActivity.class));
                break;
            case R.id.homeBtnRealVU:
                startActivity(new Intent(this, BeximcoMainActivity.class));
                finish();
                break;
            case R.id.homeBtnIvac:
                startActivity(new Intent(this, IvacMainActivity.class));
                break;
            case R.id.homeBtnBanglalion:
                startActivity(new Intent(this, BanglalionMainActivity.class));
                break;
            default:
                break;
        }
    }
}
