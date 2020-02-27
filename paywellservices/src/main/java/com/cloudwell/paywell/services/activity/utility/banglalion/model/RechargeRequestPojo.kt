package com.cloudwell.paywell.services.activity.utility.banglalion.model

import com.google.gson.annotations.SerializedName

data class RechargeRequestPojo (

        @field: SerializedName("username")
        var userName : String = "",

        @field: SerializedName("customerID")
        var customerID : String = "",

        @field: SerializedName("amount")
        var amount : String = "",

        @field: SerializedName("ref_id")
        var ref_id : String = "",

        @field: SerializedName("password")
        var password : String = ""

)