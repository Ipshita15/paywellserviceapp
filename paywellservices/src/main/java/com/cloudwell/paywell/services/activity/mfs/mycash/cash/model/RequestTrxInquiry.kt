package com.cloudwell.paywell.services.activity.mfs.mycash.cash.model

import com.google.gson.annotations.SerializedName

class RequestTrxInquiry {
    @SerializedName("limit")
    var limit: String = ""
    @SerializedName("username")
    var username: String = ""

}