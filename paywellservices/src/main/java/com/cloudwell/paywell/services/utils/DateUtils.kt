package com.cloudwell.paywell.services.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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

    fun getTimeInformationdifferenceDate(startDate: Date, endDate: Date): String {
        //milliseconds
        var different = endDate.time - startDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli


        val elapsedSeconds = different / secondsInMilli

        var date = "";
        if (elapsedDays != 0L) {
            date = "$elapsedDays d "
        }

        if (elapsedSeconds != 0L) {
            date = "$elapsedHours h "
        }


        if (elapsedMinutes != 0L) {
            date = "$elapsedMinutes m "
        }

        if (elapsedSeconds != 0L) {
            date = "$elapsedSeconds s "
        }

        return date;
    }

    fun getDurtingJounaryTime(millis: Long): String {
        var localMillis = millis
        if (localMillis < 0) {
//            throw IllegalArgumentException("Duration must be greater than zero!")
            localMillis = 0
        }

        val days = TimeUnit.MILLISECONDS.toDays(localMillis)
        localMillis -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(localMillis)
        localMillis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(localMillis)
        localMillis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(localMillis)

        val sb = StringBuilder(64)

        if (days != 0L) {
            sb.append(days)
            sb.append("d ")
        }

        if (hours != 0L) {
            sb.append(hours)
            sb.append("h ")
        }

        if (minutes != 0L) {
            sb.append(minutes)
            sb.append("m ")
        }

        if (seconds != 0L) {
            sb.append(seconds)
            sb.append("s")
        }

        return sb.toString()
    }


    fun getDurtingJounaryTimeNew(dateStart: Date, dateStop: Date): String {
        //HH converts hour in 24 hours format (0-23), day calculation
        val sb = StringBuilder(64)

        try {


            //in milliseconds
            val diff = dateStop.time - dateStart.time

            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)

            if (diffDays != 0L) {
                sb.append(diffDays)
                sb.append(" day ")
            }

            if (diffHours != 0L) {
                sb.append(diffHours)
                sb.append(" hr ")
            }

//            if (diffMinutes != 0L) {
            sb.append(diffMinutes)
            sb.append(" min ")
//            }

            if (diffSeconds != 0L) {
                sb.append(diffSeconds)
                sb.append(" s")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sb.toString()

    }

    fun differenceMilliSecond(startDate: Date, endDate: Date): Long {
        return endDate.time - startDate.time
    }

    fun getDynamicTwoYear(): MutableList<String> {
        val years = mutableListOf<String>()

        val year = Calendar.getInstance().get(Calendar.YEAR)
        years.add("" + year)

        val today = Calendar.getInstance()
        today.add(Calendar.YEAR, 1)
        val nextYear = today.get(Calendar.YEAR)
        years.add("" + nextYear)
        return years
    }

    fun getDifferenceDays(dateOne: String, dateTwo: String): Int {
        val d1 = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(dateOne)
        val d2 = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(dateTwo)

        var daysdiff = 0
        val diff = d2.time - d1.time
        val diffDays = diff / (24 * 60 * 60 * 1000) + 1
        daysdiff = diffDays.toInt()
        return daysdiff
    }


}
