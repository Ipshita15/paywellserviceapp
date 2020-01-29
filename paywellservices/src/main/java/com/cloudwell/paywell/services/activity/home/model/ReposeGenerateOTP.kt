package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class ReposeGenerateOTP {
    @SerializedName("ApiStatus")
    var apiStatus: Int =0
    @SerializedName("ApiStatusName")
    var apiStatusName: String =""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetails? = null

}