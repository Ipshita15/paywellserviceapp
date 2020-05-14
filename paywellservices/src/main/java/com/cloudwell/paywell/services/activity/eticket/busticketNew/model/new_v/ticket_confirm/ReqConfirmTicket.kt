package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm

import com.google.gson.annotations.SerializedName

class ReqConfirmTicket {
    @SerializedName("deviceId")
    var deviceId: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("trxId")
    var trxId: String? = null

    @SerializedName("username")
    var username: String? = null

}