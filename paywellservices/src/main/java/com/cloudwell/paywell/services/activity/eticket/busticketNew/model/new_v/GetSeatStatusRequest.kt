package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class GetSeatStatusRequest(

	@field:SerializedName("toCity")
	var toCity: String = "",

	@field:SerializedName("optionId")
	var optionId: String = "",

	@field:SerializedName("seatData")
	var seatData: String = "",

	@field:SerializedName("departureDate")
	var departureDate: String = "",

	@field:SerializedName("fromCity")
	var fromCity: String = "",

	@field:SerializedName("deviceId")
	var deviceId: String = "",

	@field:SerializedName("username")
	var username: String = ""
)