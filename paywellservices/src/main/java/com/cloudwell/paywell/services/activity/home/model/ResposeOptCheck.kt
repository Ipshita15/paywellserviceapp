package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class ResposeOptCheck {
    @SerializedName("ApiStatus")
    var apiStatus: Long = 0
    @SerializedName("ApiStatusName")
    var apiStatusName: String = ""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetails = ResponseDetails()

}