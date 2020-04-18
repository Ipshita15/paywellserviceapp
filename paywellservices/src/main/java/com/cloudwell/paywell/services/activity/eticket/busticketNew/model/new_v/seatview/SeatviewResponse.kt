package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview

import com.google.gson.annotations.SerializedName

data class SeatviewResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("seatViewData")
	val seatViewData: SeatViewData? = null
)