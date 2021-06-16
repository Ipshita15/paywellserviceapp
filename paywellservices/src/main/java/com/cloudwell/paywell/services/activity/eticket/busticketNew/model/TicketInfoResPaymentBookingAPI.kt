package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import com.google.gson.annotations.SerializedName


class TicketInfoResPaymentBookingAPI {

    @SerializedName("boardingPointName")
    var boardingPointName: String? = null
    @SerializedName("bookingInfoId")
    var bookingInfoId: String? = null
    @SerializedName("status_message")
    var statusMessage: String? = null
    @SerializedName("ticketNo")
    var ticketNo: String? = null
    @SerializedName("totalAmount")
    var totalAmount: String? = null
    @SerializedName("transportId")
    var transportId: String? = null
    @SerializedName("webBookingInfoId")
    var webBookingInfoId: String? = null

}
