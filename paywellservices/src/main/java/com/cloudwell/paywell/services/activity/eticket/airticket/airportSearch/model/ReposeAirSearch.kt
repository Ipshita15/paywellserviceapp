package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model

import com.google.gson.annotations.SerializedName


class ReposeAirSearch(var throwable: Throwable) {

    @SerializedName("data")
    var data: Data? = null
    @SerializedName("message")
    var message: String? = ""
    @SerializedName("status")
    private var mStatus: Int = 0


    val status: Int?
        get() = mStatus


    fun setStatus(status: Int) {
        mStatus = status
    }

    fun setStatus(status: Int?) {
        mStatus = status!!
    }
}
