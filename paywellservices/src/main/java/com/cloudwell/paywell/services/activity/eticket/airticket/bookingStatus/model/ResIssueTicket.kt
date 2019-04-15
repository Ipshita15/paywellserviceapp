package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model


import com.google.gson.annotations.SerializedName

class ResIssueTicket(var throwable: Throwable) {

    @SerializedName("api_requested")
    var apiRequested: Any? = null
    @SerializedName("data")
    var data: List<Datum>? = null
    @SerializedName("IsPriceChanged")
    var isPriceChanged: Boolean? = null
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0

}
