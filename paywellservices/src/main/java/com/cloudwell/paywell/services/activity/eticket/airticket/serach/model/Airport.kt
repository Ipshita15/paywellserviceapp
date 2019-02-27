package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import com.google.gson.annotations.SerializedName


class Airport {

    @SerializedName("AirportCode")
    var airportCode: String = ""
    @SerializedName("AirportName")
    var airportName: String = ""
    @SerializedName("CityCode")
    var cityCode: String = ""
    @SerializedName("CityName")
    var cityName: String = ""
    @SerializedName("CountryCode")
    var countryCode: String = ""
    @SerializedName("CountryName")
    var countryName: String = ""
    @SerializedName("Terminal")
    var terminal: String = ""

}
