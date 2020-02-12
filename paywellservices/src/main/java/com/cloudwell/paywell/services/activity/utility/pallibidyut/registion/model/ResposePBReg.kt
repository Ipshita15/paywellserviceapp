package com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.model

import com.google.gson.annotations.SerializedName

class ResposePBReg {
    @SerializedName("ApiStatus")
    var apiStatus: Int = 0
    @SerializedName("ApiStatusName")
    var apiStatusName: String = ""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetails? = null

}