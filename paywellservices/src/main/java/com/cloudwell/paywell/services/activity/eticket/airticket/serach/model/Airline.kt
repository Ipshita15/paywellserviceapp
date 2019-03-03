package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Airline : Parcelable {

    @SerializedName("AirlineCode")
    var airlineCode: String = ""
    @SerializedName("AirlineName")
    var airlineName: String = ""
    @SerializedName("BookingClass")
    var bookingClass: String = ""
    @SerializedName("CabinClass")
    var cabinClass: String = ""
    @SerializedName("FlightNumber")
    var flightNumber: String = ""
    @SerializedName("OperatingCarrier")
    var operatingCarrier: String = ""

}
