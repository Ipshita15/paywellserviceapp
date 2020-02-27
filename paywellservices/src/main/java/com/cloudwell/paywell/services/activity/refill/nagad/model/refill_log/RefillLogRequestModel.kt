package com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log

import com.google.gson.annotations.SerializedName

data class RefillLogRequestModel (

        @field: SerializedName("sec_token")
        var sec_token : String = "",

        @field: SerializedName("username")
        var imei : String = "",

        @field: SerializedName("gateway_id")
        var gateway_id : String = "",

        @field: SerializedName("limit")
        var limit : String = "",

        @field: SerializedName("ref_id")
        var ref_id : String = ""
)