package com.cloudwell.paywell.services.activity.utility.ivac.model

import com.google.gson.annotations.SerializedName

data class IvcTrxResponseModel(
        @field:SerializedName("amount")
    var amount: String = "",

        @field:SerializedName("center_name")
    var center_name: String = "",

        @field:SerializedName("message")
    var message: String = "",

        @field:SerializedName("passport_no")
    var passport_no: String = "",

        @field:SerializedName("phone_no")
    var phone_no: String = "",

        @field:SerializedName("status")
    var status: Int,

        @field:SerializedName("trx_id")
    var trx_id: String = "",

        @field:SerializedName("trx_request_date_time")
    var trx_request_date_time: String = "",

        @field:SerializedName("trx_response_date_time")
    var trx_response_date_time: String = "",

        @field:SerializedName("web_file_no")
    var web_file_no: String = ""
)