package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import com.google.gson.annotations.SerializedName


class ResPaymentBookingAPI {

    @SerializedName("meassage")
    var meassage: String? = null
    @SerializedName("status")
    var status: Long? = null
    @SerializedName("ticketInfo")
    var ticketInfo: TicketInfoResPaymentBookingAPI? = null
    @SerializedName("trans_id")
    var transId: String? = null

}
