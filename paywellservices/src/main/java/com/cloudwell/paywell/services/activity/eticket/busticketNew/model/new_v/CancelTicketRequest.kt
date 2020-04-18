package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class CancelTicketRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("ticketNo")
	val ticketNo: String? = null,

	@field:SerializedName("trxId")
	val trxId: String? = null,

	@field:SerializedName("deviceId")
	val deviceId: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)