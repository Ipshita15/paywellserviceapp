package com.cloudwell.paywell.services.activity.base;

import android.os.Bundle;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.Locale;

import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA;
import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_ENGLISH;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-11-20.
 */
public class LanguagesBaseActivity extends  BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeAppTheme();
    }

    private void changeAppTheme() {
        boolean isEnglish = AppHandler.getmInstance(getApplicationContext()).getAppLanguage().equals("en");
        if (isEnglish) {
            switchToCzLocale(new Locale(KEY_ENGLISH, ""));
            setTheme(R.style.EnglishAppTheme);
        } else {
            switchToCzLocale(new Locale(KEY_BANGLA, ""));
            setTheme(R.style.BanglaAppTheme);
        }
    }


}
