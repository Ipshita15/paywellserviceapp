package com.cloudwell.paywell.services.activity.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;
import com.cloudwell.paywell.services.activity.utility.nid.NidMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBMainActivity;
import com.cloudwell.paywell.services.activity.utility.passport.PassportMainActivity;
import com.cloudwell.paywell.services.activity.utility.qubee.QubeeMainActivity;
import com.cloudwell.paywell.services.activity.utility.realvu.BeximcoMainActivity;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.List;

@SuppressWarnings("ALL")
public class UtilityMainActivity extends AppCompatActivity {

    private static final String BEXIMCO = "Beximco Real VU";
    private String utilityService;
    private AppHandler mAppHandler;
    private String mImeiNo;
    boolean hasBeximcoService = false;
    private RelativeLayout mRelativeLayout;
    private Button homeBtnNID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_utility);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mAppHandler = new AppHandler(this);
        initView();
    }

    private void initView() {
        homeBtnNID = (Button) findViewById(R.id.homeBtnNID);
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn")) {
            setMargins(homeBtnNID, 0, 20, 0, 0);
        }
        List<String> serviceList = AppLoadingActivity.getUtilityService();
        for (int i = 0; i < serviceList.size(); i++) {
            if (BEXIMCO.equalsIgnoreCase(serviceList.get(i))) {
                hasBeximcoService = true;
                break;
            }
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
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
            case R.id.homeBtnPassport:
                startActivity(new Intent(this, PassportMainActivity.class));
                break;
            case R.id.homeBtnNID:
                startActivity(new Intent(this, NidMainActivity.class));
                break;
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
                break;
            case R.id.homeBtnQubee:
                startActivity(new Intent(this, QubeeMainActivity.class));
                break;
            case R.id.homeBtnRealVU:
                if (hasBeximcoService) {
                    startActivity(new Intent(this, BeximcoMainActivity.class));
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

    private void AskForPin(final String service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    dialogInterface.dismiss();
                    if (service.equals("desco")) {
                        startActivity(new Intent(UtilityMainActivity.this, DESCOBillPayActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(UtilityMainActivity.this, DPDCBillPayActivity.class));
                        finish();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
