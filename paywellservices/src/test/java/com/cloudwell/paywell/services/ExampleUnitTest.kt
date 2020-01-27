package com.cloudwell.paywell.services

import android.util.Log
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.OutputSegment
import com.cloudwell.paywell.services.activity.home.model.RequestOtpCheck
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import org.junit.Test
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.security.spec.PKCS8EncodedKeySpec
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


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


        val m = RequestOtpCheck()
        m.format = "json"
        m.otp = "1234"
        m.username = "01675349882"
        val toJson = Gson().toJson(m)


        val privateString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDBVkqCh5gT2d2EtIXtJSE1CNmC9k1n2HfHxmLbf8iprnD2AitxKXdCQrKYeNyye8yH1x011OpEmMx+F8zX+I5u5j4t9XugrI82FGjX/skxybjM5vWaU532NWt6zMQb4TjAR3aYv76j+ZmKhrsnGUQeGuR4AA3H/L+06Qu0WdZAvK7/VeDsPbqeMaKXDv0f0fgHhERVCyuNfQbf1zfCn4JQeuCzdG11UseAZlAqwwWddEkaNhTF6Uz7zOxi/0pkkZNC/M1WwBNSEeqEHjtWKSLA4k9a7MPxMAchvSSif/rDVdXcyaIICy0wGF7zpxoFubNDxUilBAGElKEzEBOFZrp5AgMBAAECggEAM9q6kkbe5Zgd/01RzFiUjv5oJGV+PleDTNwrQJcF0WjdmEXds/S8rVNpRlbITsDAi0CJb5pDGRHoavtkMBrUzO7JB5ebSG1v3b/cnO4TtVxWyfI6NmGt8M7EHIQWJyohiATCzNZEwgMciNh5EtQGfpKU65CMIbLrEhEdWApuWh8o3TytQlmaCb5V6Az4ZctdvRaytxQ/YvXTgRo/o+X+OaNDw8m250GkXd8iddfubenBse2BB9MaOfl1xP2+hPJkolz0fLZ93xT4r99TpcbIN7YsoDiXBFJXYaoWWjXRCGiPK7QQFm+8td65MTyKIJN7/WxQ/MwFZNNLQlU6x7LHDwKBgQD+mX7iWpDfRWMldJDHQiW77xXaHlX7VOge28RAidQOnbfQxH0HWIo/o8OYwqfkG0aFzW2JPEQXdGQ54OfIDOqvR4WiO7gF1pUmFTC3hPDxfQBZtDHzkcsaxmS9WDPIf/Hy9hVXzk+pn4R3U0841qzSc2lq0jU7/AEp9ptjFbKn3wKBgQDCZofw0BlPUMp5ZpTz9anLdv8riltonC6o5XExFTDf1ao3pEKiU/kkz7eeSo6a/zsAA86kB5fww3fvX0TY0jH/hOuAR/geS6dqDugapCyBPm4TWzKDOl/aHX/beIWEWbGXf8ipLQ6XsNSzDtKtuE7LUZTXqyKSDT6mkUqtb+vIpwKBgQDwxQYqV41zp/HYJQEZfuOic7qNmGyljykorZpNkkpZPCvuITM/9CphfqRl3YfafzTVKm1w/+5A5BA4cAmhtR1nf1LQFnu0Abbw2c9FblJRfW4MZw1qEzEo8/+m1De8X8rWgoOykufhOHqUQdPEo7eyGfTSUVKIlIwhPBa8wHNArwKBgAwqKFWMYBkTgCgKoEWH2OEviBYsaT3pkA3nlaaxocZP83/Z2oWX5Z5FFUNlfPj8AbAljNe81eguAyJKft9mf9Ryd94mIsOajlZXqnSIU3Se+HpjmYyWqYrXj7mnGAvJJRDK4T6c4C3j1duCkPJn9x5H851vSxGCnKoFq3ug5ks/AoGBAPONkaK8o/VIVc5BeV6f4jcXOc+B1ieFx+X2LO7pwRvEV0I1ti2aN0hUWm4U9AJ8Ox45bz/zEgGPXEvc+K1W93ZfIYAABYGM4/zcAAC3yMXTTqRhKoMjsc3p3E/A+fSuxw1naZyy8lPgtASV1pXpBOsGv5nmW+BuxpAlnCEtYsXw"

        val decodePrivateKey = java.util.Base64.getDecoder().decode(privateString)
        val keySpec = PKCS8EncodedKeySpec(decodePrivateKey)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val p = kf.generatePrivate(keySpec)


        val apiEnvoleData = "Q/TUBaTy3Bt8G7hnP00LqVS2rIl5tlgwaq9T4UDVCc3HdfluRIcnFZCT7TJOTepTSjzM0nF3HmXj2zEJRuUXtsi7yOf4xWHKuinhCbQQEc7c+/883jjaav6h7bo9ECKxDCyQvB3v384kE2aNUwO5qE2fgt469UeBZrxxDF/M0NomuP3pF/4EKBuIuneP9Vz476mL8n/SaZtA9Wow7Iypt8MQR7eDW1zcQUzCD/COoUifDKn5i1xkH9iM9lVq+a1spyNFTrvDnFFcFp6uGz8Jx/iEAntdxBs/LE1yfuulR6WfugHkWE7rKcI4rNUNa8eP/yCKhlHUgiLZmsyAglUyFw=="

        val cipher = Cipher.getInstance("RSA")
        cipher?.init(Cipher.DECRYPT_MODE,  p)


        val envlopeDecode = java.util.Base64.getDecoder().decode(apiEnvoleData.toByteArray(Charsets.UTF_8))
        val rowEnvlopeDecrytionKey = cipher.doFinal(envlopeDecode)
        val envlpeDecryptionKey= java.util.Base64.getEncoder().encodeToString(rowEnvlopeDecrytionKey)




        val sealAPI = "mcqZXxeKKaNRuijv83xqcG2ZwoJgMqWwoukEAYUTWV4oySR/E4RFq4s1f3zmUwdjR+yBEdngMmJS/zLt0Oev5QgRVzmPAwtwrPThnjJh7gX90tQJbVIPWd9IMp+hSjbhH//PJgnR/ePcDSk3GG/jSpNPjClqlnYJngZ0AVTx9eI="

        val sealDatadecode = java.util.Base64.getDecoder().decode(sealAPI.toByteArray(Charsets.UTF_8))


        val secretKeySpec = SecretKeySpec(rowEnvlopeDecrytionKey, "RC4")
        val cipherRC4 = Cipher.getInstance("RC4") // Transformation of the algorithm
        cipherRC4.init(Cipher.DECRYPT_MODE,secretKeySpec)
        val rowDecryptionKey = cipherRC4.doFinal(sealDatadecode)
        val sealDecryptionKey= java.util.Base64.getEncoder().encodeToString(rowDecryptionKey)



        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1ODAxMDM4NTcsImp0aSI6Im5tUCt1Z3pBUmFlOXV5eDlhSzhXNWdUQWZ2c3hzbWdhdkRBTHREYVh2aDA9IiwiaXNzIjoiaHR0cHM6XC9cL2FnZW50YXBpLnBheXdlbGxvbmxpbmUuY29tXC9QYXl3ZWxsQXV0aFwvZ2V0VG9rZW4iLCJuYmYiOjE1ODAxMDM4NjcsImV4cCI6MTU4MDEzMjY2NywiZGF0YSI6eyJ1c2VyX3R5cGUiOiJSZXRhaWxlciJ9fQ.C18KxvI0ZkQfRse0vaDk_UQxj063-5Ud6TjO5PTRxixHPpF9kzaaM-LHo4mo_kmfNPZbMHvMP72NqxQaQLggfQ"
        val encodeToken =java.util.Base64.getEncoder().encodeToString(token.toByteArray(Charsets.UTF_8))

        val hmac: String? = calculateHMAC(toJson, sealDecryptionKey)


        val authString = "$encodeToken:$hmac"

        Log.e("data", "data")



    }


    @Throws(SignatureException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun calculateHMAC(data: String, key: String): String? {
        val HMAC_SHA512 = "HmacSHA512"
        val secretKeySpec = SecretKeySpec(key.toByteArray(), HMAC_SHA512)
        val mac: Mac = Mac.getInstance(HMAC_SHA512)
        mac.init(secretKeySpec)
        return toHexString(mac.doFinal(data.toByteArray()))
    }


    private fun toHexString(bytes: ByteArray): String? {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }






}