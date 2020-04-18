package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata

import com.google.gson.annotations.SerializedName

data class ScheduleDataItem(

	@field:SerializedName("departure_id")
	val departureId: Int? = null,

	@field:SerializedName("seat_types")
	val seatTypes: List<String?>? = null,

	@field:SerializedName("route_name")
	val routeName: String? = null,

	@field:SerializedName("departure_date")
	val departureDate: String? = null,

	@field:SerializedName("transport_id")
	val transportId: String? = null,

	@field:SerializedName("coach_type")
	val coachType: String? = null,

	@field:SerializedName("fares")
	val fares: Fares? = null,

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("end_counter")
	val endCounter: String? = null,

	@field:SerializedName("bus_service_type")
	val busServiceType: String? = null,

	@field:SerializedName("transport_type")
	val transportType: String? = null,

	@field:SerializedName("coach_no")
	val coachNo: String? = null,

	@field:SerializedName("company_name")
	val companyName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("bus_id")
	val busId: Int? = null,

	@field:SerializedName("sku")
	val sku: String? = null,

	@field:SerializedName("is_ticket_cancelable")
	val isTicketCancelable: Int? = null,

	@field:SerializedName("departure_time")
	val departureTime: String? = null,

	@field:SerializedName("start_counter")
	val startCounter: String? = null
)