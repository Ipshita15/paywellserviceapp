package com.cloudwell.paywell.services

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.OutputSegment
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants terms_and_conditions_format.
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {


        val data = "[{\"Airline\":{\"AirlineCode\":\"QR\",\"AirlineName\":\"Qatar Airways\",\"BookingClass\":\"K\",\"CabinClass\":\"M\",\"FlightNumber\":\"641\",\"OperatingCarrier\":\"QR\"},\"Baggage\":\"30 K\",\"Destination\":{\"Airport\":{\"AirportCode\":\"DOH\",\"AirportName\":\"Doha\",\"CityCode\":\"DOH\",\"CityName\":\"Doha\",\"CountryCode\":\"QA\",\"CountryName\":\"Qatar\"},\"ArrTime\":\"2019-09-01T13:50:00\"},\"Equipment\":\"77W\",\"JourneyDuration\":\"260\",\"Origin\":{\"Airport\":{\"AirportCode\":\"DAC\",\"AirportName\":\"Shahjalal intl. Airport\",\"CityCode\":\"DAC\",\"CityName\":\"Dhaka\",\"CountryCode\":\"BD\",\"CountryName\":\"Bangladesh\"},\"DepTime\":\"2019-09-01T11:30:00\"},\"StopQuantity\":\"0\",\"TripIndicator\":\"OutBound\"},{\"Airline\":{\"AirlineCode\":\"QR\",\"AirlineName\":\"Qatar Airways\",\"BookingClass\":\"K\",\"CabinClass\":\"M\",\"FlightNumber\":\"127\",\"OperatingCarrier\":\"QR\"},\"Baggage\":\"30 K\",\"Destination\":{\"Airport\":{\"AirportCode\":\"MXP\",\"AirportName\":\"Malpensa\",\"CityCode\":\"MIL\",\"CityName\":\"Milan\",\"CountryCode\":\"IT\",\"CountryName\":\"Italy\",\"Terminal\":\"1\"},\"ArrTime\":\"2019-09-02T13:10:00\"},\"Equipment\":\"77W\",\"JourneyDuration\":\"490\",\"Origin\":{\"Airport\":{\"AirportCode\":\"DOH\",\"AirportName\":\"Doha\",\"CityCode\":\"DOH\",\"CityName\":\"Doha\",\"CountryCode\":\"QA\",\"CountryName\":\"Qatar\"},\"DepTime\":\"2019-09-02T08:00:00\"},\"StopQuantity\":\"0\",\"TripIndicator\":\"OutBound\"}]"

        val countries = Gson().fromJson(data, Array<OutputSegment>::class.java)
        getTotalJounaryTime(countries)
        getTransitiveCalculation(countries)


    }

    private fun getTransitiveCalculation(data: Array<OutputSegment>) {

        if (data.size > 1) {

            var totalTransiteTime = 0L
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

            val durationBreakdown = getDurationBreakdown(totalTransiteTime)
            Logger.v("")


        }

    }

    private fun getTotalJounaryTime(toList: Array<OutputSegment>) {

        val first = toList.first()
        val depTimeAPIFirst = first.origin?.depTime?.split("T")
        val arrTimeAPIFirst = first.destination?.arrTime?.split("T")
        val date = "yyyy-MM-dd HH:mm:ss"
        val depTimeFirst = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPIFirst?.get(0) + " " + depTimeAPIFirst?.get(1)) as Date
        val arrTimeFirst = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPIFirst?.get(0) + " " + arrTimeAPIFirst?.get(1)) as Date


        val last = toList.last()

        val depTimeAPILast = last.origin?.depTime?.split("T")
        val arrTimeAPILast = last.destination?.arrTime?.split("T")
        val depTimeFirstLast = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPILast?.get(0) + " " + depTimeAPILast?.get(1)) as Date
        val arrTimeFirstLast = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPILast?.get(0) + " " + arrTimeAPILast?.get(1)) as Date

        var diff = arrTimeFirstLast.time - depTimeFirst.time

        getDurationBreakdown(diff)


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


    fun getDurtingJounaryTimeNewTest(duration: List<OutputSegment>): String {
        //HH converts hour in 24 hours format (0-23), day calculation

        val SECOND = 1000
        val MINUTE = 60 * SECOND;
        val HOUR = 60 * MINUTE;
        val DAY = 24 * HOUR;


        val sb = StringBuilder(64)


        var diffMinutes = 0
        var diffHours = 0

        var totalDiffent = 0L


        duration.forEachIndexed { index, it ->
            val depTimeAPI = it.origin?.depTime?.split("T")
            val arrTimeAPI = it.destination?.arrTime?.split("T")
            val date = "yyyy-MM-dd HH:mm:ss"
            val depTime = SimpleDateFormat(date, Locale.ENGLISH).parse(depTimeAPI?.get(0) + " " + depTimeAPI?.get(1)) as Date
            val arrTime = SimpleDateFormat(date, Locale.ENGLISH).parse(arrTimeAPI?.get(0) + " " + arrTimeAPI?.get(1)) as Date
            val diff = arrTime.time - depTime.time

            totalDiffent += diff

            if (index != duration.lastIndex) {
                val nextDepTimeAPI = duration.get(index + 1).origin?.depTime?.split("T")
                val nextDepTime = SimpleDateFormat(date, Locale.ENGLISH).parse(nextDepTimeAPI?.get(0) + " " + nextDepTimeAPI?.get(1)) as Date
                val transtionTimeDiff = nextDepTime.time - arrTime.time

                totalDiffent += transtionTimeDiff

            }

            ////

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

            val toString = sb.toString()
            Logger.v("test123: ")


        }


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

        val toString = sb.toString()
        Logger.v("test123: " + sb.toString())

        return sb.toString()

    }
}