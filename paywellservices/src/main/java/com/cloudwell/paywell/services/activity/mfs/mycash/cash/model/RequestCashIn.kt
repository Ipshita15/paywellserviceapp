package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName

class RequestCashIn {
    @SerializedName("agentOTP")
    var agentOTP: String = ""
    @SerializedName("amount")
    var amount: String = ""
    @SerializedName("customerWallet")
    var customerWallet: String = ""
    @SerializedName("format")
    var format: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("service_type")
    var serviceType: String = ""
    @SerializedName("username")
    var username: String = ""



}