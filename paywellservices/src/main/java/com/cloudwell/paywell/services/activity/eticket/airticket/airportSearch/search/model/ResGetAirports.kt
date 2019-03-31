package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model

import com.google.gson.annotations.SerializedName


class ResGetAirports(var throwable: Throwable) {

    @SerializedName("airports")
    var airports: List<Airport> = mutableListOf()
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0

}
