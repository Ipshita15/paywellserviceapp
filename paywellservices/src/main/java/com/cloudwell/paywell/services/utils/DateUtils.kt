package com.cloudwell.paywell.services.utils

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.OutputSegment
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Segment
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


    fun getDurtingJounaryTimeNewTest(duration: List<OutputSegment>): String {

        val first = duration.first()
        val depTimeAPIFirst = first.origin?.depTime?.split("T")
        val arrTimeAPIFirst = first.destination?.arrTime?.split("T")
        val date = "yyyy-MM-dd HH:mm:ss"
        val depTimeFirst = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPIFirst?.get(0) + " " + depTimeAPIFirst?.get(1)) as Date
        val arrTimeFirst = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPIFirst?.get(0) + " " + arrTimeAPIFirst?.get(1)) as Date


        val last = duration.last()

        val depTimeAPILast = last.origin?.depTime?.split("T")
        val arrTimeAPILast = last.destination?.arrTime?.split("T")
        val depTimeFirstLast = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPILast?.get(0) + " " + depTimeAPILast?.get(1)) as Date
        val arrTimeFirstLast = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPILast?.get(0) + " " + arrTimeAPILast?.get(1)) as Date

        var totalDiffent = arrTimeFirstLast.time - depTimeFirst.time
        val durationBreakdown = getDurationBreakdown(totalDiffent)


        return durationBreakdown

    }

    fun getDurationBreakdown(millis: Long): String {
        var millis = millis
        if (millis < 0) {
            throw IllegalArgumentException("Duration must be greater than zero!")
        }

        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)


        val sb = StringBuilder(64)

        if (hours != 0L) {
            sb.append(hours)
            sb.append("h ")
        }

        if (minutes != 0L) {
            sb.append(minutes)
            sb.append("m ")
        }

        val toString = sb.toString();
        return sb.toString()
    }


    fun getDartingJanuaryTimeNewTest(duration: OutputSegment): String {
        //HH converts hour in 24 hours format (0-23), day calculation

        val SECOND = 1000
        val MINUTE = 60 * SECOND;
        val HOUR = 60 * MINUTE;
        val DAY = 24 * HOUR;


        val sb = StringBuilder(64)


        var diffMinutes = 0
        var diffHours = 0

        var totalDiffent = 0L


        val depTimeAPI = duration.origin?.depTime?.split("T")
        val arrTimeAPI = duration.destination?.arrTime?.split("T")
        val date = "yyyy-MM-dd HH:mm:ss"
        val depTime = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPI?.get(0) + " " + depTimeAPI?.get(1)) as Date
        val arrTime = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPI?.get(0) + " " + arrTimeAPI?.get(1)) as Date
        val diff = arrTime.time - depTime.time

        totalDiffent += diff



        diffMinutes = (totalDiffent / (1000 * 60) % 60).toInt()
        diffHours = (totalDiffent / (1000 * 60 * 60) % 24).toInt()


        if (diffHours != 0) {
            sb.append(diffHours)
            sb.append("h ")
        }

        if (diffMinutes != 0) {
            sb.append(diffMinutes)
            sb.append("m")
        }

        return sb.toString()

    }

    fun getDartingJanuaryTimeNew(duration: Segment): String {
        val sb = StringBuilder(64)


        val diffMinutes: Int
        val diffHours: Int

        var durationInt = 0


        durationInt = durationInt + duration.journeyDuration.toInt()



        diffHours = (durationInt / 60)
        diffMinutes = (durationInt % 60)


        if (diffHours != 0) {
            sb.append(diffHours)
            sb.append("h ")
        }

        if (diffMinutes != 0) {
            sb.append(diffMinutes)
            sb.append("m")
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

    fun getFormatDepTime(date: String): String {
        val APIDateString = date.split("T").get(0)
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(APIDateString) as Date
        val nameOfDayOfWeek = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).format(fdepTimeFormatDate)
        return nameOfDayOfWeek
    }

    fun getFormatDate(date: String): String {
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date) as Date
        val nameOfDayOfWeek = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(fdepTimeFormatDate)
        return nameOfDayOfWeek
    }

    fun getFormatTime(date: String): String {
        val APIDateString = date.split("T").get(1)
        return APIDateString.substring(0, APIDateString.length - 3)
    }


}
