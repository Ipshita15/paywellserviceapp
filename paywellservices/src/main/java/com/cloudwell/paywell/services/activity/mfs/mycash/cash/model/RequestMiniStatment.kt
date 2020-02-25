package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName


class RequestMiniStatment {
    @SerializedName("password")
    var password: String = ""
    @SerializedName("myCashPin")
    var pin: String = ""
    @SerializedName("service_type")
    var serviceType: String = ""
    @SerializedName("username")
    var username: String = ""

}