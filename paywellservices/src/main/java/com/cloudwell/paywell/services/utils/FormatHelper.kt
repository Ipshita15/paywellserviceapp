package com.cloudwell.paywell.services.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 18/4/19.
 */
object FormatHelper {


    fun formatText(data: String): Spanned {
        return Html.fromHtml(data)

    }

    public fun getPortLevelText(it: Airport?): String {
        var cityOrStatusName = ""
        if (!it?.city.equals("")) {
            cityOrStatusName = it?.city + "/"
        } else if (!it?.state.equals("")) {
            cityOrStatusName = it?.state + "/"
        }
        return "" + FormatHelper.formatText(cityOrStatusName + it?.airportName)
    }

    fun stripHtml(html: String?): Spanned? {
        return if (!TextUtils.isEmpty(html)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(html)
            }
        } else null
    }


}
