package com.cloudwell.paywell.services.activity.notification.model

import com.google.gson.annotations.SerializedName



class RequestSDABalancceRetrun {
    @SerializedName("amount")
    var amount: String = ""
    @SerializedName("dealerId")
    var dealerId: String = ""
    @SerializedName("marchentId")
    var marchentId: String = ""
    @SerializedName("merchentPassword")
    var merchentPassword: String = ""
    @SerializedName("messageId")
    var messageId: String = ""
    @SerializedName("password")
    var password: String = ""
    @SerializedName("shadowId")
    var shadowId: String = ""
    @SerializedName("username")
    var username: String = ""

}