package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm

import com.google.gson.annotations.SerializedName

data class TicketDataItem(

	@field:SerializedName("journey_route")
	val journeyRoute: String? = null,

	@field:SerializedName("transport_name")
	val transportName: String? = null,

	@field:SerializedName("total_amount")
	val totalAmount: String? = null,

	@field:SerializedName("ticket_no")
	val ticketNo: String? = null,

	@field:SerializedName("coach_no")
	val coachNo: String? = null,

	@field:SerializedName("departure_date")
	val departureDate: String? = null,

	@field:SerializedName("seat_lbls")
	val seatLbls: String? = null,

	@field:SerializedName("boarding_point_name")
	val boardingPointName: Any? = null,

	@field:SerializedName("departure_time")
	val departureTime: String? = null,

	@field:SerializedName("statusMessage")
	val statusMessage: String? = null
)