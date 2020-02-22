package com.cloudwell.paywell.services.activity.topup.model

import com.google.gson.annotations.SerializedName

data class ResponseDetailsItem(

	@field:SerializedName("operator_name")
	val operatorName: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("status_code")
	val statusCode: String? = null,

	@field:SerializedName("connection_type")
	val connectionType: String? = null,

	@field:SerializedName("status_name")
	val statusName: String? = null,

	@field:SerializedName("request_date")
	val requestDate: String? = null,

	@field:SerializedName("tran_id")
	val tranId: String? = null,

	@field:SerializedName("recipient_msisdn")
	val recipientMsisdn: String? = null
)