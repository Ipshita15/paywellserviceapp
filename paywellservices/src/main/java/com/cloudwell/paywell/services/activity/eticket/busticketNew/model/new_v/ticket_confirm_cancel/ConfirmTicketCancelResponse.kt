package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm_cancel

import com.google.gson.annotations.SerializedName

data class ConfirmTicketCancelResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("ticketInfo")
	val ticketInfo: List<TicketInfoItem?>? = null
)