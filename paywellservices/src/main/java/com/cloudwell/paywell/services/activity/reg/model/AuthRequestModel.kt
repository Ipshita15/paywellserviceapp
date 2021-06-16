package com.cloudwell.paywell.services.activity.reg.model

import com.google.gson.annotations.SerializedName

class AuthRequestModel {
    @SerializedName("appVersionName")
    var appVersionName: String = ""

    @SerializedName("appVersionNo")
    var appVersionNo: String = ""

    @SerializedName("username")
    var username: String = ""

    @SerializedName("osName")
    var osName: String = ""

    @SerializedName("osVersion")
    var osVersion: String = ""

}