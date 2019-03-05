package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Airline : Parcelable {

    @SerializedName("AirlineCode")
    var airlineCode: String? = null
    @SerializedName("AirlineName")
    var airlineName: String? = null
    @SerializedName("BookingClass")
    var bookingClass: String? = null
    @SerializedName("CabinClass")
    var cabinClass: String? = null
    @SerializedName("FlightNumber")
    var flightNumber: String? = null
    @SerializedName("OperatingCarrier")
    var operatingCarrier: String? = null

}
