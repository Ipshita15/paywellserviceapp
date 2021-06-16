package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class ReposeUserProfile {
    @SerializedName("ApiStatus")
    val apiStatus: Int = 0
    @SerializedName("ApiStatusName")
    val apiStatusName: String = ""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetailsUserProfile? = null

}