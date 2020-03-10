package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName

class ReqeustPaymentConfmation {
    @SerializedName("password")
    var password: String = ""
    @SerializedName("pendingId")
    var pendingId: String = ""
    @SerializedName("username")
    var username: String = ""

}