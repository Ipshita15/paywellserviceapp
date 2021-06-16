package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName

class RequestBalanceTransferRequest {
    @SerializedName("agentOTP")
    var agentOTP: String = ""
    @SerializedName("amount")
    var amount: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("serviceType")
    var serviceType: String = ""
    @SerializedName("username")
    var username: String = ""

}