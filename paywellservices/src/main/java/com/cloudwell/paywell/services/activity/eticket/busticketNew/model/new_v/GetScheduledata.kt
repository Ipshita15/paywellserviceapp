package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v
import com.google.gson.annotations.SerializedName

class GetScheduledata(

	@field:SerializedName("returnCoachType")
	var returnCoachType: String = "",

	@field:SerializedName("roundTrip")
	var roundTrip: String = "",

	@field:SerializedName("returnDate")
	var returnDate: String = "",

	@field:SerializedName("coachType")
	var coachType: String = "",

	@field:SerializedName("departingTime")
	var departingTime: String = "",

	@field:SerializedName("destination")
	var destination: String = "",

	@field:SerializedName("transportType")
	var transportType: String = "",

	@field:SerializedName("departure")
	var departure: String = "",

	@field:SerializedName("departingDate")
	var departingDate: String = "",

	@field:SerializedName("deviceId")
	var deviceId: String = "",

	@field:SerializedName("returnTime")
	var returnTime: String = "",

	@field:SerializedName("username")
	var username: String = ""
)