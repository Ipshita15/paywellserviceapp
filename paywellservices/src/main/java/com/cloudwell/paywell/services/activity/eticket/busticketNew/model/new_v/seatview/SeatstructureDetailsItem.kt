package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview

import com.google.gson.annotations.SerializedName

data class SeatstructureDetailsItem(

		@field:SerializedName("fare")
		var fare: Double = 0.0,

		@field:SerializedName("y_axis")
		var yAxis: Int? = null,

		@field:SerializedName("x_axis")
		var xAxis: Int? = null,

		@field:SerializedName("seat_no")
		var seatNo: String? = null,

		@field:SerializedName("seatid")
		var seatid: Int? = null,

		@field:SerializedName("status")
		var status: String? = null
) {
	var isUserSeleted: Boolean = false
}