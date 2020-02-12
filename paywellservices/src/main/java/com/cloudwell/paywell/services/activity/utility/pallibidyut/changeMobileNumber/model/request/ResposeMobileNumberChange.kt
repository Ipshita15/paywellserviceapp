package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.request

import com.google.gson.annotations.SerializedName

class ResposeMobileNumberChange {
    @SerializedName("ApiStatus")
    var apiStatus: Int = 0
    @SerializedName("ApiStatusName")
    var apiStatusName: String = ""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetails? = null

}