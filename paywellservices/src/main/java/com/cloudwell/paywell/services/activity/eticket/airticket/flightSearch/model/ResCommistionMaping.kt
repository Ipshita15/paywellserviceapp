package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.model

import com.google.gson.annotations.SerializedName

class ResCommistionMaping(throwable: Throwable) {

    @SerializedName("commission")
    var commission: Commission? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("status")
    var status: String = ""

}
