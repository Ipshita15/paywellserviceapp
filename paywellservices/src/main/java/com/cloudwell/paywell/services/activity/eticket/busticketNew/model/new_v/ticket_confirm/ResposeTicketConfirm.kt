package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm

import com.google.gson.annotations.SerializedName

class ResposeTicketConfirm {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("status_code")
    var statusCode: Long? = null

    @SerializedName("ticketData")
    var ticketData: List<TicketDatum>? = null

}