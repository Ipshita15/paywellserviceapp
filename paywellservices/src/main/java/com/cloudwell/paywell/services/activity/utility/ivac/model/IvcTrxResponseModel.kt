package com.cloudwell.paywell.services.activity.utility.ivac.model

data class IvcTrxResponseModel(
    var amount: String = "",
    var center_name: String = "",
    var message: String = "",
    var passport_no: String = "",
    var phone_no: String = "",
    var status: Int,
    var trx_id: String = "",
    var trx_request_date_time: String = "",
    var trx_response_date_time: String = "",
    var web_file_no: String = ""
)