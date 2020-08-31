package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import com.google.gson.annotations.SerializedName

class RequestRenerateOtpForCancelTicket {
    @SerializedName("deviceId")
    var deviceId: String = ""

    @SerializedName("password")
    var password: String = ""

    @SerializedName("ticketNo")
    var ticketNo: String = ""

    @SerializedName("trxId")
    var trxId: String = ""

    @SerializedName("username")
    var username: String? = ""
}