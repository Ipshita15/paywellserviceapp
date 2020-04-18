package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class SeatBlockRequestPojo(

	@field:SerializedName("roundTrip")
	var roundTrip: String = "",

	@field:SerializedName("password")
	var password: String = "",

	@field:SerializedName("passenger")
	var passenger: Passenger? = null,

	@field:SerializedName("toCity")
	var toCity: String = "",

	@field:SerializedName("optionInfo")
	var optionInfo: ArrayList<OptionInfoItem> = ArrayList(),

	@field:SerializedName("fromCity")
	var fromCity: String = "",

	@field:SerializedName("deviceId")
	var deviceId: String = "",

	@field:SerializedName("username")
	var username: String = ""
)