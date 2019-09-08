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


//        val data = "[{\"Airline\":{\"AirlineCode\":\"QR\",\"AirlineName\":\"Qatar Airways\",\"BookingClass\":\"K\",\"CabinClass\":\"M\",\"FlightNumber\":\"641\",\"OperatingCarrier\":\"QR\"},\"Baggage\":\"30 K\",\"Destination\":{\"Airport\":{\"AirportCode\":\"DOH\",\"AirportName\":\"Doha\",\"CityCode\":\"DOH\",\"CityName\":\"Doha\",\"CountryCode\":\"QA\",\"CountryName\":\"Qatar\"},\"ArrTime\":\"2019-09-01T13:50:00\"},\"Equipment\":\"77W\",\"JourneyDuration\":\"260\",\"Origin\":{\"Airport\":{\"AirportCode\":\"DAC\",\"AirportName\":\"Shahjalal intl. Airport\",\"CityCode\":\"DAC\",\"CityName\":\"Dhaka\",\"CountryCode\":\"BD\",\"CountryName\":\"Bangladesh\"},\"DepTime\":\"2019-09-01T11:30:00\"},\"StopQuantity\":\"0\",\"TripIndicator\":\"OutBound\"},{\"Airline\":{\"AirlineCode\":\"QR\",\"AirlineName\":\"Qatar Airways\",\"BookingClass\":\"K\",\"CabinClass\":\"M\",\"FlightNumber\":\"127\",\"OperatingCarrier\":\"QR\"},\"Baggage\":\"30 K\",\"Destination\":{\"Airport\":{\"AirportCode\":\"MXP\",\"AirportName\":\"Malpensa\",\"CityCode\":\"MIL\",\"CityName\":\"Milan\",\"CountryCode\":\"IT\",\"CountryName\":\"Italy\",\"Terminal\":\"1\"},\"ArrTime\":\"2019-09-02T13:10:00\"},\"Equipment\":\"77W\",\"JourneyDuration\":\"490\",\"Origin\":{\"Airport\":{\"AirportCode\":\"DOH\",\"AirportName\":\"Doha\",\"CityCode\":\"DOH\",\"CityName\":\"Doha\",\"CountryCode\":\"QA\",\"CountryName\":\"Qatar\"},\"DepTime\":\"2019-09-02T08:00:00\"},\"StopQuantity\":\"0\",\"TripIndicator\":\"OutBound\"}]"

        val data = "[{\"TripIndicator\":\"OutBound\",\"Origin\":{\"Airport\":{\"AirportCode\":\"DAC\",\"AirportName\":\"Shahjalal intl. Airport\",\"Terminal\":null,\"CityCode\":\"DAC\",\"CityName\":\"Dhaka\",\"CountryCode\":\"BD\",\"CountryName\":\"Bangladesh\"},\"DepTime\":\"2019-09-01T15:00:00\"},\"Destination\":{\"Airport\":{\"AirportCode\":\"CXB\",\"AirportName\":\"Cox's Bazar Airport\",\"Terminal\":null,\"CityCode\":\"CXB\",\"CityName\":\"Coxs Bazar\",\"CountryCode\":\"BD\",\"CountryName\":\"Bangladesh\"},\"ArrTime\":\"2019-09-01T16:05:00\"},\"Airline\":{\"AirlineCode\":\"VQ\",\"AirlineName\":\"Novoair\",\"FlightNumber\":\"939\",\"BookingClass\":\"\",\"CabinClass\":\"Y\",\"OperatingCarrier\":\"VQ\"},\"Baggage\":\"20K\",\"JourneyDuration\":\"65\",\"StopQuantity\":\"0\",\"Equipment\":null}]"
        val countries = Gson().fromJson(data, Array<OutputSegment>::class.java)
        val transitiveCalculation = getTransitiveCalculation(countries.toList())
        val transitiveString = getDurationBreakdown(transitiveCalculation)


        val totalDurationTime = getTotalDurationTime(countries.toList())
        val totalDurationString = getDurationBreakdown(totalDurationTime)


        var total = transitiveCalculation + totalDurationTime
        val durationBreakdown = getDurationBreakdown(total)

        Logger.v("", "")


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