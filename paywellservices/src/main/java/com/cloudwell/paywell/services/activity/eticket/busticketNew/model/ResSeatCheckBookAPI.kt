package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import com.google.gson.annotations.SerializedName


class ResSeatCheckBookAPI {

    @SerializedName("message")
    var message: String = ""
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("ticketInfo")
    var ticketInfoSeatBookAndCheck: TicketInfoSeatBookAndCheck? = null
    @SerializedName("trans_id")
    var transId: String = ""

}
