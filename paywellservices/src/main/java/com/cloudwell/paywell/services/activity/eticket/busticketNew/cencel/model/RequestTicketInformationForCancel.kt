package com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model

import com.google.gson.annotations.SerializedName

class RequestTicketInformationForCancel {
    @SerializedName("deviceId")
    var deviceId: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("trxId")
    var trxId: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("ticketNo")
    var ticketNo: String? = null

}