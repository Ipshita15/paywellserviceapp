package com.cloudwell.paywell.services.activity.utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacMainActivity;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.KarnaphuliMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBMainActivity;
import com.cloudwell.paywell.services.activity.utility.qubee.QubeeMainActivity;
import com.cloudwell.paywell.services.activity.utility.realvu.BeximcoMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class UtilityMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility);
        }
        AppHandler mAppHandler = new AppHandler(this);

        Button btnDesco = findViewById(R.id.homeBtnDESCO);
        Button btnDpdc = findViewById(R.id.homeBtnDPDC);
        Button btnWasa = findViewById(R.id.homeBtnWasa);
        Button btnWzpdcl = findViewById(R.id.homeBtnWestZone);
        Button btnPolli = findViewById(R.id.homeBtnPolliBiddut);
        Button btnQubee = findViewById(R.id.homeBtnQubee);
        Button btnReal = findViewById(R.id.homeBtnRealVU);
        Button btnIvac = findViewById(R.id.homeBtnIvac);
        Button btnBanglalion = findViewById(R.id.homeBtnBanglalion);
        Button btnKarnaphuli = findViewById(R.id.homeBtnKarnaphuli);

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
            btnKarnaphuli.setTypeface(AppController.getInstance().getOxygenLightFont());
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
            btnKarnaphuli.setTypeface(AppController.getInstance().getAponaLohitFont());
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_MENU);
                startActivity(new Intent(this, DESCOMainActivity.class));
                finish();
                break;
            case R.id.homeBtnDPDC:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_MENU);
                startActivity(new Intent(this, DPDCMainActivity.class));
                finish();
                break;
            case R.id.homeBtnWasa:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_WASA_MENU);
                startActivity(new Intent(this, WASAMainActivity.class));
                finish();
                break;
            case R.id.homeBtnWestZone:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_MENU);
                startActivity(new Intent(this, WZPDCLMainActivity.class));
                finish();
                break;
            case R.id.homeBtnPolliBiddut:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU);
                startActivity(new Intent(this, PBMainActivity.class));
                finish();
                break;
            case R.id.homeBtnQubee:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_QUBEE_MENU);
                startActivity(new Intent(this, QubeeMainActivity.class));
                finish();
                break;
            case R.id.homeBtnRealVU:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_REAL_VU_MENU);
                startActivity(new Intent(this, BeximcoMainActivity.class));
                finish();
                break;
            case R.id.homeBtnIvac:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_MENU);
                startActivity(new Intent(this, IvacMainActivity.class));
                finish();
                break;
            case R.id.homeBtnBanglalion:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_MENU);
                startActivity(new Intent(this, BanglalionMainActivity.class));
                break;
            case R.id.homeBtnKarnaphuli:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU);
                startActivity(new Intent(this, KarnaphuliMainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
