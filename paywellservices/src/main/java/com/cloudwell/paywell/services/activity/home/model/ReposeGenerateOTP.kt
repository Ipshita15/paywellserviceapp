package com.cloudwell.paywell.services.activity.home.model

import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.ResponseDetail
import com.google.gson.annotations.SerializedName

class ReposeGenerateOTP {
    @SerializedName("ApiStatus")
    var apiStatus: Int =0
    @SerializedName("ApiStatusName")
    var apiStatusName: String =""
    @SerializedName("ResponseDetails")
    var responseDetails: ResponseDetail? = null

}