package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName


class RequestBalanceInquray {

    @SerializedName("username")
    var username: String = ""
    @SerializedName("myCashPin")
    var myCashPin: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("service_type")
    var serviceType: String = ""

}