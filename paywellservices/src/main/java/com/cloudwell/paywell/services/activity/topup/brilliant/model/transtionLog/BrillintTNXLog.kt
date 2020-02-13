package com.cloudwell.paywell.services.activity.topup.brilliant.model.transtionLog

import com.google.gson.annotations.SerializedName

class BrillintTNXLog {
    @field:SerializedName("number")
    var number: String = ""

    @field:SerializedName("password")
    var password: String = ""

    @field:SerializedName("format")
    var format: String = ""

    @field:SerializedName("username")
    var username: String = ""
}