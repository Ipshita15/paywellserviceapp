package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview

import com.google.gson.annotations.SerializedName

data class SeatstructureDetailsItem(

	@field:SerializedName("fare")
	val fare: Int? = null,

	@field:SerializedName("y_axis")
	val yAxis: Int? = null,

	@field:SerializedName("x_axis")
	val xAxis: Int? = null,

	@field:SerializedName("seat_no")
	val seatNo: String? = null,

	@field:SerializedName("seatid")
	val seatid: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)