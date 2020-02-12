package com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.model

import com.google.gson.annotations.SerializedName

class ResBIllStatus {
    @SerializedName("ApiStatus")
    var apiStatus: Int = 0
    @SerializedName("ApiStatusName")
    var apiStatusName: String  = ""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetails? = null
}