package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import com.google.gson.annotations.SerializedName


class Destination {

    @SerializedName("Airport")
    var airport: Airport = Airport()
    @SerializedName("ArrTime")
    var arrTime: String = ""

}
