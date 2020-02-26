package com.cloudwell.paywell.services.activity.utility.electricity.westzone.model

import com.google.gson.annotations.SerializedName

data class WZPDCLBillPayModel (
        @field: SerializedName("username")
        var username : String   = "",
        @field: SerializedName("password")
        var password : String = "",

        @field: SerializedName("payerMobileNo")
        var  payerMobileNo : String = "",

        @field: SerializedName("billNo")
        var billNo : String = "",

        @field: SerializedName("billYear")
        var billYear : String = "",

        @field: SerializedName("billMonth")
        var billMonth : String = "",

        @field: SerializedName("totalAmount")
        var totalAmount : String = "",

        @field: SerializedName("transId")
        var transId : String = ""

)