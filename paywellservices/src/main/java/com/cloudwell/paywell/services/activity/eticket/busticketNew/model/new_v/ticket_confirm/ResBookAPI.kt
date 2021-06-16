package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm

import com.google.gson.annotations.SerializedName

class ResBookAPI {
    @SerializedName("message")
    var message: String = ""

    @SerializedName("status_code")
    var statusCode: String = ""

    @SerializedName("trxId")
    var trxId: String = ""

}