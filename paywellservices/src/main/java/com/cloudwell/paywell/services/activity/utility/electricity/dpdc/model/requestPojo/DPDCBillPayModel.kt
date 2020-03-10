package com.cloudwell.paywell.services.activity.utility.electricity.dpdc.model.requestPojo

import com.google.gson.annotations.SerializedName

data class DPDCBillPayModel (
        @field: SerializedName("username")
        var username : String = "",


        @field: SerializedName("password")
        var password : String = "",

        @field: SerializedName("payerMobileNo")
        var payerMobileNo : String = "",

        @field: SerializedName("billNo")
        var billNo : String = "",

        @field: SerializedName("location")
        var location : String = "",

        @field:SerializedName("billYear")
        var billYear : String = "",

        @field: SerializedName("billMonth")
        var billMonth : String = "",

        @field: SerializedName("reference_id")
        var reference_id : String = "",

        @field: SerializedName("totalAmount")
        var totalAmount : String = "",

        @field: SerializedName("transId")
        var  transId : String = ""
)