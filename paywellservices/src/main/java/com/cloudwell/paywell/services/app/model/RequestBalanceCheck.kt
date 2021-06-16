package com.cloudwell.paywell.services.app.model

import com.google.gson.annotations.SerializedName

class RequestBalanceCheck {
    @SerializedName("deviceId")
    var deviceId: String = ""
    @SerializedName("username")
    var username: String = ""

}