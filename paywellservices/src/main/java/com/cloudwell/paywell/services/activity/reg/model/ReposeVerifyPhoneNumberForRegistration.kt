package com.cloudwell.paywell.services.activity.reg.model

import com.google.gson.annotations.SerializedName

class ReposeVerifyPhoneNumberForRegistration {
    @SerializedName("message")
    var message: String = ""

    @SerializedName("status")
    var status: Int = 0

}