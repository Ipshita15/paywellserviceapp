package com.cloudwell.paywell.services.activity.base;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.Locale;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
public class ProductEecommerceBaseActivity extends MVVMBaseActivity {
    AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchToCzLocale(Locale.ENGLISH);
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


    public void switchToCzLocale(Locale locale) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}
