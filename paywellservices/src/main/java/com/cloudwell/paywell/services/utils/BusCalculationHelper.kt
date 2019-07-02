package com.cloudwell.paywell.services.utils

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/4/19.
 */
object BusCalculationHelper {

    fun getPrices(ticketPrice: String?, userDate: String): String {
        val jsonObject = JSONObject(ticketPrice)
        val dateKey = jsonObject.keys()

        dateKey.forEach {
            val price = jsonObject.get(it).toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val userDate = sdf.parse(userDate)
            val date2 = sdf.parse(it)
            if (userDate.before(date2) || userDate.equals(date2)) {
                return price
            }
        }
        return ""
    }
}




