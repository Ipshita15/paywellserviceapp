package com.cloudwell.paywell.services.activity.eticket.busticketNew.model


import com.google.gson.annotations.SerializedName


class TicketInfoSeatBookAndCheck {

    @SerializedName("charge")
    var charge: Long = 0
    @SerializedName("msg")
    var msg: String = ""
    @SerializedName("paid_amount")
    var paidAmount: Long = 0
    @SerializedName("ticket_gen_id")
    var ticketGenId: String = ""
    @SerializedName("ticket_price")
    var ticketPrice: String = ""

    var seats: String = ""


}
