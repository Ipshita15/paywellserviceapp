package com.cloudwell.paywell.services.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 13/2/19.
 */
object DateUtils {

    val notificationDateFormat = "yyyy-MM-dd HH:mm:ss"

    val currentDataAndTIme: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val cal = Calendar.getInstance()
            return cal.time.toString()
        }
}
