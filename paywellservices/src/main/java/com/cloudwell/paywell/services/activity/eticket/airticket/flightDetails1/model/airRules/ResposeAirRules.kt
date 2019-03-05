package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules

import com.google.gson.annotations.SerializedName


class ResposeAirRules(var throwable: Throwable) {

    @SerializedName("data")
    var data: List<Datum>? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: Int = 0

}
