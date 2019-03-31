package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Airport : Parcelable {

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
