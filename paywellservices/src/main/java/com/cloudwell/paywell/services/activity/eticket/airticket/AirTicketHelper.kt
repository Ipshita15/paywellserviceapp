package com.cloudwell.paywell.services.activity.eticket.airticket

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 9/4/19.
 */
class AirTicketHelper {
    companion object {

        fun getFormatDepTime(date: String): String {
            val APIDateString = date.split("T").get(0)
            val fdepTimeFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(APIDateString) as Date
            val nameOfDayOfWeek = SimpleDateFormat("EEE, dd MMM yy", Locale.ENGLISH).format(fdepTimeFormatDate)
            return nameOfDayOfWeek
        }

        fun getFormatTime(date: String): String {
            val APIDateString = date.split("T").get(1)
            return APIDateString.substring(0, APIDateString.length - 3)
        }
    }
}
