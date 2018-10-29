package com.cloudwell.paywell.services.activity.topup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.topup.brilliant.BrilliantTopupActivity;
import com.cloudwell.paywell.services.app.authentication.AuthenticationManager;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import java.text.SimpleDateFormat;

public class MainTopUpActivity extends AppCompatActivity {

    Button mobileOperatorBtn, brilliantBtn;
    private ConnectionDetector mCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topup);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mobileOperatorBtn = (Button) findViewById(R.id.btnMobileOperators);
        brilliantBtn = (Button) findViewById(R.id.btnBrilliant);

        mobileOperatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainTopUpActivity.this, TopUpOperatorMenuActivity.class));
            }
        });

        brilliantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainTopUpActivity.this, BrilliantTopupActivity.class));

            }
        });


        try {
            APIResposeGenerateToken apiResposeGenerateToken = (APIResposeGenerateToken) AppStorageBox.get(getApplicationContext(), AppStorageBox.Key.AUTHORIZATION_DATA);
            if (apiResposeGenerateToken == null) {

                mCd = new ConnectionDetector(this);

                if (mCd.isConnectingToInternet()) {
                    AuthenticationManager.basicAuthentication(getApplicationContext());
                } else {
                    showErrorMessage();
                }


            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ackTimestamp = apiResposeGenerateToken.getAckTimestamp();
                long currentTimeMillis = System.currentTimeMillis();
                long parse = formatter.parse(ackTimestamp).getTime();

                if (parse < currentTimeMillis) {
                    mCd = new ConnectionDetector(this);

                    if (mCd.isConnectingToInternet()) {
                        Toast.makeText(getApplicationContext(), "Call", Toast.LENGTH_LONG).show();

                        AuthenticationManager.basicAuthentication(getApplicationContext());
                    } else {
                        showErrorMessage();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Already update", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            showErrorMessage();
        }


    }

    private void showErrorMessage() {
        View mRelativeLayout = findViewById(R.id.relativeLayout);
        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainTopUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
