package com.cloudwell.paywell.services.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 18/4/19.
 */
public class FormatHelper {


    public static Spanned formatText(String data) {
        Spanned spanned = Html.fromHtml(data);
        return spanned;

    }
}
