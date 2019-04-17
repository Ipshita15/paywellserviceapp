package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model


import com.google.gson.annotations.SerializedName

class ResIssueTicket(var throwable: Throwable) {

    @SerializedName("api_requested")
    var apiRequested: Any? = null

    @SerializedName("IsPriceChanged")
    var isPriceChanged: Boolean? = null

    @SerializedName("message")
    var message: String = ""

    @SerializedName("status")
    var status: Int = 0

    @SerializedName("total_fare_calculated")
    var total_fare_calculated: Double? = null


    @SerializedName("total_fare_calculated_new")
    var total_fare_calculated_new: Double? = null

}
