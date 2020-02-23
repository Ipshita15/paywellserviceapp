package com.cloudwell.paywell.services.activity.reg.model

import com.google.gson.annotations.SerializedName

class RequestDistrictList {
    @SerializedName("channel")
    var channel: String = ""
    @SerializedName("deviceId")
    var deviceId: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("timestamp")
    var timestamp: String = ""
    @SerializedName("username")
    var username: String = ""

}