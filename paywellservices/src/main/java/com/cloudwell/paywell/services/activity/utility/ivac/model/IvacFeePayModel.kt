package com.cloudwell.paywell.services.activity.utility.ivac.model

import com.google.gson.annotations.SerializedName

data class IvacFeePayModel(
        @field:SerializedName("amount")
    var amount: String="",
        @field:SerializedName("center_no")
    var center_no: String="",
        @field:SerializedName("cust_mobile")
    var cust_mobile: String="",
        @field:SerializedName("passport_no")
    var passport_no: String="",
        @field:SerializedName("password")
    var password: String="",
        @field:SerializedName("username")
    var username: String="",
        @field:SerializedName("web_file_no")
    var web_file_no: String=""
)