package com.cloudwell.paywell.services

import android.util.Log
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.OutputSegment
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import org.junit.Test
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher


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

    @Test
    fun getDurtingJounaryTimeNewTest(){


        val privateString = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCPvEWyoAS3fjtvanB4YpzYFBvS3yGXiygtyA4br32nrizRNyGntEQjDF8yVc9p66tIFEwUMYPn5p3m0P9aSh2yJRmH2OAxFzPJ2nyQIMs+PFiyzF7vo5bPu8nf37+v/uqY7vTmOB2vV8GczHnbVOFIbsWR29HUT8A0D6J2zX+E1adBD6V5SAQdoq+6H3YjMOUvLt9gfHF0KkKrStL0zwNFoZNn4JS9JB4hg6VPZlqotvhJpy8DCLpVCn91jg0OQEXWX02PD5H9j8jBhmsOQaJlEGM5aTnwqw4UJ8twUY/ubopWKlYsS6tdbznW1lQjhYo+bDEE54BOKWdq95pcUT2NAgMBAAECggEAC9Wgl3h9au5FzoKhCAh2iYP+VnpwtZ2LjVlvb/AfFHNO1VsItlotUgVuwSI3la0FyUWCjhcVmT5vudVzcOexUj2jwH+m1ePnK7OFlghdM56cXvxcxLZfcHMxx/EQQ1llz3m9SEdOimVbV6GuVtTCR8h3E+9Zc3WtiZvP8KAy46jkIBl4faXHHNlvx0fwzJtleclwnxEygVUvls5FBtvdAm+LezzVaZ2LyYvDHJoqlg+46o/LS1idSBeiaIBFq4raonwKpUZijXGbFtrbVtJtbWzSFeWV2Rp/+ZMZBPavsfC77bRYcapk9SALj1fBj3QCLR/fbTfbmX6pHNkeoTfxMQKBgQDJh2amQxUSNvcxwJodTUEFG1IxsdrD5t9hkklLmecFnLdskhvlRoW5NJK/psNZnWeRi7Uvzog4oazzLcKRbrtJ2+mfQ1nRqwejZpcm3AxMJ99qqVrWCmXxPh2CM9rz9I/njzV3oa6u6/T2Wrho661Rs0NfqTCZt/dlcKb3OUfMzwKBgQC2lehaJ0oPkgGeM5qY87i+PgrePUChefNZ+1BKkznJvlUXVOY+bxPLomH4DJXCOC0Z92l2zRTgjV4wPEgPFVeGownWdu293Q56WbQkze0FyJDT/3eI94XF6vV6Yj3WNK2WIvtoydyf27OKrX1Uk/+doym9kgXOa/WJfzztCUu+4wKBgAzEf0RZS6RzxwVn5luk4VGpgXOUiP+QSOatlecsQO6iFxzRxOKprR8mrYVm00mCJ2WZLElzFD5CP+rII2ODWGo9fHeSlMYrx7gab8kOd9j7TbQ8Nn0I+5xlCwmMr3p1LAjHkeOaYq7CVCqnZLeA9uIOMV6GIYYbmZjbojhXcK0RAoGBAJ1tlcCd3bqdHm4Eeojko+bMYdyLHb3dA1kTWoBiftIXHREX78nnRj8vDJ+uYjXq7+BStglr/FM9MPgQEeWHdkctLl9Pfd9VyZTu4WdWcsaoz08rFyrumNY0p2HVcRHPq9gm43TPkD35Vc39lnGlhiGqPGQqkn0QEs5x+ds7R6cXAoGAc6srBeILcZzUz83dUVpCLAzDVi5nnqOqTpaPJD9vIPDQWqzoUI8LvrP5CIMC0bemcrcM75aR3CvauXzHbETXZ0FzryWDWYbaENLS19YgGbbnXVoPai6oPnDljYbmW+n/eGCKVS9BQ4VRVUt248UwFLmRE4y0GYwJnv3CyXQnJrc="

        val decodePrivateKey = java.util.Base64.getDecoder().decode(privateString)
        val keySpec = PKCS8EncodedKeySpec(decodePrivateKey)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val p = kf.generatePrivate(keySpec)


        val apiEnvoleData = "ij253m8HUmvrFgWpQPyVk9pR4TZ7NVQz2wB5ij6AIGJ6YcIY5KmBI5KD4G7aIQ3cFOGzTdbB0uze2g6mKnQnQ1o2fVSfh0PBQtvVq9SYAywpNyP1ceCrVj9vqH9kmBKOs5p61ED4xUUXyeFgY/oxL32rbBWFX1g5g1Gnb/6B6OyJsTFRQrm26DjRxweY3sAfaduaqjwOpa4ZXa5ip5gN+6WneoNjqMVhEMe1iDe0HuzRoB89oHN56kIKA5HHK644v3qHm7CW2jjcqe05I97lKijImpcnZgWnMbG9RrX6j98fwZBtbOCnVqm9ut/uaD7AwEvB6YPtI+a6RmFmL8fMfQ=="

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher?.init(Cipher.DECRYPT_MODE,  p)


        val envlopeDecode = java.util.Base64.getDecoder().decode(apiEnvoleData.toByteArray(Charsets.UTF_8))
        val rowEnvlopeDecrytionKey = cipher.doFinal(envlopeDecode)
        val envlpeDecryptionKey= java.util.Base64.getEncoder().encodeToString(rowEnvlopeDecrytionKey)
        Log.e("data", "data")

    }





}