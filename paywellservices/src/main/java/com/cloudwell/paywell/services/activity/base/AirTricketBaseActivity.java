package com.cloudwell.paywell.services.activity.base;

import android.os.Bundle;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity;
import com.cloudwell.paywell.services.app.AppHandler;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
public class AirTricketBaseActivity extends MVVMBaseActivity {
    AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        changeAppTheme();

    }

    private void changeAppTheme() {
        boolean isEnglish = mAppHandler.getAppLanguage().equalsIgnoreCase("en");
        if (isEnglish) {
            setTheme(R.style.EnglishAppTheme);
        } else {
            setTheme(R.style.EnglishAppTheme);
        }
    }


    public void changeAppBaseTheme() {
        setTheme(R.style.AppTheme);
    }
}
