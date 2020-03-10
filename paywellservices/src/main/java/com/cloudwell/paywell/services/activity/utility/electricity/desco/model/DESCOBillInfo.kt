package com.cloudwell.paywell.services.activity.utility.electricity.desco.model

import com.google.gson.annotations.SerializedName

data class DESCOBillInfo (
        @field: SerializedName("username")
        var username: String = "",

        @field: SerializedName("password")
        var password:String  = "",

        @field: SerializedName("payerMobileNo")
        var payerMobileNo:String = "",

        @field: SerializedName("billNo")
        var billNo : String = ""

        )


