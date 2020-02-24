package com.cloudwell.paywell.services.activity.utility.karnaphuli.model.requestPojo

import com.google.gson.annotations.SerializedName

data class SubmitBillRequestPojo (
        @field: SerializedName("username")
        var username : String = "",

        @field: SerializedName("password")
        var password : String ="",

        @field: SerializedName("payerMobileNo")
        var payerMobileNo : String = "",

        @field: SerializedName("billNo")
        var billNo : String = "",

        @field: SerializedName("totalAmount")
        var totalAmount : String = "",

        @field: SerializedName("transId")
        var transId : String = ""
)