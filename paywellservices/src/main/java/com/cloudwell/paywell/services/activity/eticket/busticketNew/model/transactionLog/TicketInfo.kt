package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog

import com.google.gson.annotations.SerializedName


class TicketInfo {

    @SerializedName("boardingPointName")
    var boardingPointName: String = ""
    @SerializedName("bookingInfoId")
    var bookingInfoId: String = ""
    @SerializedName("departureDate")
    var departureDate: String = ""
    @SerializedName("departureTime")
    var departureTime: String = ""
    @SerializedName("journeyRoute")
    var journeyRoute: String? = "Bogachari-Chittagong"
    @SerializedName("noOfSeat")
    var noOfSeat: String = ""
    @SerializedName("seatLbls")
    var seatLbls: String = ""
    @SerializedName("ticketNo")
    var ticketNo: String = ""
    @SerializedName("totalAmount")
    var totalAmount: String = ""
    @SerializedName("webBookingInfoId")
    var webBookingInfoId: String = ""

}
