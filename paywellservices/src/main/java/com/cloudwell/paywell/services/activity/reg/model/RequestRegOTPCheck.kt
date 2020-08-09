package com.cloudwell.paywell.services.activity.reg.model

import com.google.gson.annotations.SerializedName

class RequestRegOTPCheck {
    @SerializedName("otp")
    var otp: String = ""

    @SerializedName("phoneNUmber")
    var phoneNUmber: String = ""

}