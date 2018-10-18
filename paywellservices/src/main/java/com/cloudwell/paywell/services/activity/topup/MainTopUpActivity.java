package com.cloudwell.paywell.services.activity.topup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;

public class MainTopUpActivity extends AppCompatActivity {

    Button mobileOperatorBtn, brilliantBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topup);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mobileOperatorBtn= (Button) findViewById(R.id.btnMobileOperators);
        brilliantBtn= (Button) findViewById(R.id.btnBrilliant);

        mobileOperatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainTopUpActivity.this, TopupMainActivity.class));
            }
        });

        brilliantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainTopUpActivity.this, BrilliantTopupActivity.class));

            }
        });

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
