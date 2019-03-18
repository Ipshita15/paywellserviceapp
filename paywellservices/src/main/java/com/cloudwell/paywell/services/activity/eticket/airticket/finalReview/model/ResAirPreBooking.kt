package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model


import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Data
import com.google.gson.annotations.SerializedName


class ResAirPreBooking(var throwable: Throwable) {

    @SerializedName("data")
    var data: Data? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: Int = 0

}
