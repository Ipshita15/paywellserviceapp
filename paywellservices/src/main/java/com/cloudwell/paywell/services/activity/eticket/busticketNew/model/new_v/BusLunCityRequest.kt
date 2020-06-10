package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

class BusLunCityRequest(

	@field:SerializedName("transportType")
	var transportType: String? = "",

	@field:SerializedName("deviceId")
	var deviceId: String? = "",

	@field:SerializedName("username")
	var username: String? = ""
)