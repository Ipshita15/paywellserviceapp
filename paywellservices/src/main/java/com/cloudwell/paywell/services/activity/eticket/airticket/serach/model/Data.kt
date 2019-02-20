package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import com.google.gson.annotations.SerializedName


class Data {

    @SerializedName("Error")
    var error: Any? = null
    @SerializedName("Results")
    var results: List<Result> = listOf()
    @SerializedName("SearchId")
    var searchId: String = ""

}
