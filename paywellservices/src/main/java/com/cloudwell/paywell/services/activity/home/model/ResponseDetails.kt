package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class ResponseDetails {
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Long = 0

}