package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class Passenger(

	@field:SerializedName("passengerName")
	var passengerName: String = "",

	@field:SerializedName("passengerGender")
	var passengerGender: String = "",

	@field:SerializedName("passengerAge")
	var passengerAge: String = "",

	@field:SerializedName("passengerMobile")
	var passengerMobile: String = "",

	@field:SerializedName("passengerAddress")
	var passengerAddress: String = "",

	@field:SerializedName("passengerEmail")
	var passengerEmail: String = ""
)