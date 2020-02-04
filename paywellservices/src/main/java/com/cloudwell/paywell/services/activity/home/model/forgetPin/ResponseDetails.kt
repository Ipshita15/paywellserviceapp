package com.cloudwell.paywell.services.activity.home.model.forgetPin

import com.google.gson.annotations.SerializedName

class ResponseDetails (){
    @SerializedName("StatusName")
    var statusName: String = ""
    @SerializedName("Status")
    var status: Int = 0
}