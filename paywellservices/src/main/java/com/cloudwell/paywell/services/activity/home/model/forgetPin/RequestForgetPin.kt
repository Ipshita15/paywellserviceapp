package com.cloudwell.paywell.services.activity.home.model.forgetPin

import com.google.gson.annotations.SerializedName

class RequestForgetPin {
    @SerializedName("deviceId")
    var deviceId: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("timestamp")
    var timestamp: String = ""
    @SerializedName("username")
    var username: String = ""

}