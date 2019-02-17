package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.Locale;

import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA;

public class EntryMainActivity extends BaseActivity {

    private CheckBox checkBox;
    private LinearLayout mLinearLayout;
    public static RegistrationModel regModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_main);


        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mAppHandler.setAppLanguage("bn");
        switchToCzLocale(new Locale(KEY_BANGLA, ""));

        mLinearLayout = findViewById(R.id.layout);
        checkBox = findViewById(R.id.item_check);

        TextView textViewTerms = findViewById(R.id.textViewTerms);

        ((TextView) mLinearLayout.findViewById(R.id.textView_welcome)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mLinearLayout.findViewById(R.id.textView_me)).setTypeface(AppController.getInstance().getAponaLohitFont());
        textViewTerms.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mLinearLayout.findViewById(R.id.textView_confirmText)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mLinearLayout.findViewById(R.id.btn_nextStep)).setTypeface(AppController.getInstance().getAponaLohitFont());

        textViewTerms.setPaintFlags(textViewTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
