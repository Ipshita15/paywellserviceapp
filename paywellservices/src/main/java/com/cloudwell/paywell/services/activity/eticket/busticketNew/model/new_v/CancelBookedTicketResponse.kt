package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class CancelBookedTicketResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)