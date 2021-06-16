package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm

import com.google.gson.annotations.SerializedName


class TicketDatum {
    @SerializedName("boarding_point_name")
    var boardingPointName: Any? = null

    @SerializedName("coach_no")
    var coachNo: String? = null

    @SerializedName("departure_date")
    var departureDate: String? = null

    @SerializedName("departure_time")
    var departureTime: String? = null

    @SerializedName("journey_route")
    var journeyRoute: String? = null

    @SerializedName("seat_lbls")
    var seatLbls: String? = null

    @SerializedName("statusMessage")
    var statusMessage: String? = null

    @SerializedName("ticket_no")
    var ticketNo: String? = null

    @SerializedName("total_amount")
    var totalAmount: String? = null

    @SerializedName("transport_name")
    var transportName: String? = null

}