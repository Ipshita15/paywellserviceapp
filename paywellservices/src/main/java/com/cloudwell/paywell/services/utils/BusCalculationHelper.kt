package com.cloudwell.paywell.services.utils

import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/4/19.
 */
object BusCalculationHelper {

    fun getPrices(ticketPrice: String?, userDate: String, transport: Transport): Double {
        var totalPrices = ""

        val jsonObject = JSONObject(ticketPrice)
        val dateKey = jsonObject.keys()
        val dateKeyDefalt = jsonObject.keys()

        dateKey.forEach {
            val price = jsonObject.get(it).toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val userDate = sdf.parse(userDate)
            val date2 = sdf.parse(it)
            if (userDate.before(date2) || userDate.equals(date2)) {
                totalPrices = price
            }

        }

        var i = 0
        var next = "";
        while (dateKeyDefalt.hasNext()) {
            i++
            next = dateKeyDefalt.next()
        }
        if (i == 1) {
            totalPrices = jsonObject.get(next).toString()
        }

        var toatlPriceDouble = totalPrices.toDouble() + transport.extraCharge

        return toatlPriceDouble
    }

    public fun getACType(model: TripScheduleInfoAndBusSchedule): String {
        var isAc = ""
        if (model.busLocalDB?.busIsAc.equals("1")) {
            isAc = "AC"
        } else {
            isAc = "NON AC"
        }
        return isAc
    }


}




