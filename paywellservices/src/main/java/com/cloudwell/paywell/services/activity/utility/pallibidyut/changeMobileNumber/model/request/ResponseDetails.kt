package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.request

import com.google.gson.annotations.SerializedName

class ResponseDetails {
    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("trx_id")
    var trxId: String = ""

}