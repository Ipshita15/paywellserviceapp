package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;

import java.util.Locale;

public class EntryMainActivity extends AppCompatActivity{

    private CheckBox checkBox;
    private LinearLayout mLinearLayout;
    private TextView textViewTerms;
    public static RegistrationModel regModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_main);

        Configuration config = new Configuration();
        config.locale = Locale.FRANCE;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        mLinearLayout = findViewById(R.id.layout);
        checkBox = findViewById(R.id.item_check);

        textViewTerms = findViewById(R.id.textViewTerms);
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
            regModel = new RegistrationModel();
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

    public void itemClicked(View view) {
        checkBox = (CheckBox) view;
    }
}
