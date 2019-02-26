package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import com.google.gson.annotations.SerializedName


class Origin {

    @SerializedName("Airport")
    var airport: Airport? = null
    @SerializedName("DepTime")
    var depTime: String? = null

}
