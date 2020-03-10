package com.cloudwell.paywell.services.activity.utility.karnaphuli.model.responsePojo


import com.google.gson.annotations.SerializedName

data class ResponseDetails(

	@field:SerializedName("trans_id")
	val transId: String? = null,

	@field:SerializedName("msg_text")
	val msgText: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("reference_id")
	val reference_id: String? = null,

	@field:SerializedName("total_amount")
	val total_amount: String? = null

)