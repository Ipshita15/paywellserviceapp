package com.cloudwell.paywell.services.activity.reg.model.postCode

import com.google.gson.annotations.SerializedName

class RequestPostCodeList {
    @SerializedName("channel")
    var channel: String = ""
    @SerializedName("deviceId")
    var deviceId: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("ref_id")
    var refId: String = ""
    @SerializedName("thanaID")
    var thanaID: String = ""
    @SerializedName("timestamp")
    var timestamp: String = ""
    @SerializedName("username")
    var username: String = ""

}