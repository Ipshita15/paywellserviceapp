package com.cloudwell.paywell.services.activity.home.model

import com.google.gson.annotations.SerializedName

class Token {
    @SerializedName("ack_timestamp")
    var ackTimestamp: String = ""
    @SerializedName("security_token")
    var securityToken: String = ""
    @SerializedName("token_exp_time")
    var tokenExpTime: String = ""
    @SerializedName("token_type")
    var tokenType: String = ""

}