package com.cloudwell.paywell.services.activity.base;

import com.cloudwell.paywell.services.R;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 30/1/19.
 */
public class AirTricketBaseActivity extends BaseActivity {

    public void changeTheme(String language) {
        if (language.equals("bd")) {
            setTheme(R.style.BanglaAppTheme);
        } else {
            setTheme(R.style.EnglishAppTheme);
        }
    }

    public void changeAppBaseTheme() {
        setTheme(R.style.AppTheme);
    }
}
