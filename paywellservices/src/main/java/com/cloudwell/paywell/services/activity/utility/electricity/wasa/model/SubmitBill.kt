package com.cloudwell.paywell.services.activity.utility.electricity.wasa.model

import com.google.gson.annotations.SerializedName

data class SubmitBill (
        @field: SerializedName("username")
        var username : String = "",

        @field: SerializedName("password")
        var password : String = "",

        @field: SerializedName("payerMobileNo")
        var  payerMobileNo : String ="",

        @field: SerializedName ("billNo")
        var billNo : String = "",

        @field: SerializedName("totalAmount")
        var totalAmount : String = "",

        @field: SerializedName("transId")
        var transId : String = ""
)