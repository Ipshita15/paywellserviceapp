package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm

import com.google.gson.annotations.SerializedName

data class ConfirmTicketResponse(

	@field:SerializedName("ticketData")
	val ticketData: List<TicketDataItem?>? = null,

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)