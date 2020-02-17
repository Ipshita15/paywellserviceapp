package com.cloudwell.paywell.services.service.model

import com.google.gson.annotations.SerializedName

class RequestUserFCMToken {
    @SerializedName("token")
    var token: String = ""
    @SerializedName("username")
    var username: String = ""
    @SerializedName("usertype")
    var usertype: String = ""
    @SerializedName("ref_id")
    var ref_id: String = ""

}