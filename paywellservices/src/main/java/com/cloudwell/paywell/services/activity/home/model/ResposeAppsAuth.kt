package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class ResposeAppsAuth {
    @SerializedName("checkOTP")
    var checkOTP: Long = 0
    @SerializedName("envlope")
    var envlope: String = ""
    @SerializedName("message")
    var message: String = ""
    @SerializedName("sealedData")
    var sealedData: String = ""
    @SerializedName("status")
    var status: Long = 0
    @SerializedName("token")
    var token: Token = Token()

}