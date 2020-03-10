package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName

class RequestFundManagment {
    @SerializedName("AgentOTP")
    var agentOTP: String = ""
    @SerializedName("amount")
    var amount: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("service_type")
    var serviceType: String = ""
    @SerializedName("trx_type")
    var trxType: String = ""
    @SerializedName("username")
    var username: String = ""

}