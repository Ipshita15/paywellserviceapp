package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class CancelBookedTicketReques(

	@field:SerializedName("password")
	var password: String? = null,

	@field:SerializedName("trxId")
	var trxId: String? = null,

	@field:SerializedName("deviceId")
	var deviceId: String? = null,

	@field:SerializedName("username")
	var username: String? = null
)