package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model

import com.google.gson.annotations.SerializedName


class BookingList(var throwable: Throwable) {

    @SerializedName("data")
    var data: List<Datum> = mutableListOf()
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0

}
