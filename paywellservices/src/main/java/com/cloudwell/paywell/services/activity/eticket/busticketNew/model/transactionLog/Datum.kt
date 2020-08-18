package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog

import com.google.gson.annotations.SerializedName


class Datum {

    @SerializedName("busInfo")
    var busInfo: BusInfo? = null
    @SerializedName("customerInfo")
    var customerInfo: CustomerInfo? = null
    @SerializedName("statusCode")
    var statusCode: String? = null
    @SerializedName("status_message")
    var statusMessage: String = ""

    @SerializedName("statusMessageForConfirm")
    var statusMessageForConfirm: String? = null

    @SerializedName("ticketInfo")
    var ticketInfo: TicketInfo? = null
    @SerializedName("transactionDateTime")
    var transactionDateTime: String = ""

    @SerializedName("trxId")
    var trxId: String = ""
}
