package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import com.google.gson.annotations.SerializedName


class Airline {

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
