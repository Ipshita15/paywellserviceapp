package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import java.util.Locale;

public class EntryMainActivity extends AppCompatActivity{

    CheckBox checkBox;
    private LinearLayout mLinearLayout;
    private ConnectionDetector mCd;
    private boolean isInternetPresent = false;
    private AppHandler mAppHandler;
    TextView textViewTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_main);

        Configuration config = new Configuration();
        config.locale = Locale.FRANCE;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        mLinearLayout = (LinearLayout) findViewById(R.id.layout);
        checkBox = (CheckBox) findViewById(R.id.item_check);
        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(EntryMainActivity.this);

        textViewTerms = (TextView) findViewById(R.id.textViewTerms);
        textViewTerms.setPaintFlags(textViewTerms.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        textViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntryMainActivity.this, TermsAndConditionsActivity.class));
                finish();
            }
        });
    }

    public void nextOnClick(View view) {
        if (checkBox.isChecked()) {
            startActivity(new Intent(EntryMainActivity.this, EntryFirstActivity.class));
            finish();
        } else {
            Snackbar snackbar = Snackbar.make(mLinearLayout, "চুক্তি ও শর্তাদি দয়া করে চেক করুন", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void itemClicked(View view) {
        checkBox = (CheckBox) view;
    }
}
