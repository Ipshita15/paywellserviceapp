package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import com.google.gson.annotations.SerializedName

class TicketInfo {

    var seats: String = ""
    @SerializedName("ticket_gen_id")
    var ticketId: String = ""

    @SerializedName("ticket_price")
    var ticketPrice: String = ""

    @SerializedName("paid_amount")
    var paidAmount: String = ""

    @SerializedName("msg")
    var msg: String = ""


}
