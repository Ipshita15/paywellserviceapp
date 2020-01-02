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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            val formatDate = dateFormat.format(cal.time)
            return formatDate
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

    fun getTotalDurationWithTransiteTime(duration: List<OutputSegment>): String {
        val transitiveCalculation = getTransitiveCalculation(duration)
//        val transitiveString = getDurationBreakdown(transitiveCalculation)
        val totalDurationTime = getTotalDurationTime(duration)
//        val totalDurationString = getDurationBreakdown(totalDurationTime)

        val total = transitiveCalculation + totalDurationTime
        return getDurationBreakdown(total)

    }

    fun getTotalDurationTime(duration: List<OutputSegment>): Long {


        var totalDuration = 0L
        duration.forEach {
            totalDuration = totalDuration + it.journeyDuration.toString().toInt()
        }

        val toMillis = TimeUnit.MINUTES.toMillis(totalDuration)
        return toMillis

    }

    private fun getTransitiveCalculation(data: List<OutputSegment>): Long {
        var totalTransiteTime = 0L

        if (data.size > 1) {
            var previousArrTime: Date? = null
            data.forEachIndexed { index, it ->
                val depTimeAPI = it.origin?.depTime?.split("T")
                val arrTimeAPI = it.destination?.arrTime?.split("T")
                val date = "yyyy-MM-dd HH:mm:ss"
                val depTime = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPI?.get(0) + " " + depTimeAPI?.get(1)) as Date
                val arrTime = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPI?.get(0) + " " + arrTimeAPI?.get(1)) as Date

                if (previousArrTime != null) {
                    val journeyTransitTime = depTime.time - previousArrTime!!.time
                    totalTransiteTime = totalTransiteTime + journeyTransitTime
                }
                previousArrTime = arrTime

            }
            return totalTransiteTime
        } else {
            return totalTransiteTime
        }

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


    fun getTotalDurationTime(duration: OutputSegment): String {
        val totalDuration = duration.journeyDuration.toString().toLong()

        val toMillis = TimeUnit.MINUTES.toMillis(totalDuration)
        val durationBreakdown = getDurationBreakdown(toMillis)

        return durationBreakdown

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
        val d1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateOne)
        val d2 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateTwo)

        val diff = d2.time - d1.time

        val daysdiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()

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
