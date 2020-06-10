package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm_cancel

import com.google.gson.annotations.SerializedName

data class TicketInfoItem(

	@field:SerializedName("trx_id")
	val trxId: String? = null,

	@field:SerializedName("totalAmount")
	val totalAmount: String? = null,

	@field:SerializedName("transportName")
	val transportName: String? = null,

	@field:SerializedName("journeyRoute")
	val journeyRoute: String? = null,

	@field:SerializedName("coachNo")
	val coachNo: String? = null,

	@field:SerializedName("isCancel")
	val isCancel: Int? = null,

	@field:SerializedName("ticketNo")
	val ticketNo: String? = null,

	@field:SerializedName("cancelTime")
	val cancelTime: String? = null,

	@field:SerializedName("departureDateTime")
	val departureDateTime: String? = null,

	@field:SerializedName("cancellationFee")
	val cancellationFee: Int? = null,

	@field:SerializedName("returnAmount")
	val returnAmount: Int? = null,

	@field:SerializedName("basePrice")
	val basePrice: String? = null
)