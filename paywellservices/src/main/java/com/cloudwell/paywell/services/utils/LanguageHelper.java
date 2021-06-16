package com.cloudwell.paywell.services.utils;

import android.content.Context;

import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.Locale;

import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 14/2/19.
 */
public class LanguageHelper {

    public static void changeLanguage(Context mContext, BaseActivity baseActivity) {
        AppHandler mAppHandler = AppHandler.getmInstance(mContext);
        mAppHandler.setAppLanguage("bn");
        baseActivity.switchToCzLocale(new Locale(KEY_BANGLA, ""));
    }
}
