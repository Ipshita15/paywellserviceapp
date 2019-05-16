package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model

import com.google.gson.annotations.SerializedName


data class Airport(

        @SerializedName("AirportCode")
        var airportCode: String? = null,
        @SerializedName("AirportName")
        var airportName: String? = null,
        @SerializedName("CityCode")
        var cityCode: String? = null,
        @SerializedName("CityName")
        var cityName: String? = null,
        @SerializedName("CountryCode")
        var countryCode: String? = null,
        @SerializedName("CountryName")
        var countryName: String? = null,
        @SerializedName("Terminal")
        var terminal: String? = null


)
