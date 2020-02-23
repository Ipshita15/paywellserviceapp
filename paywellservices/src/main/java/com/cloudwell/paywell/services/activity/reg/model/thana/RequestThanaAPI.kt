package com.cloudwell.paywell.services.activity.reg.model.thana

import com.google.gson.annotations.SerializedName

class RequestThanaAPI {
    @SerializedName("channel")
    var channel: String = ""
    @SerializedName("deviceId")
    var deviceId: String = ""
    @SerializedName("distriID")
    var distriID: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("timestamp")
    var timestamp: String = ""
    @SerializedName("username")
    var username: String = ""

}