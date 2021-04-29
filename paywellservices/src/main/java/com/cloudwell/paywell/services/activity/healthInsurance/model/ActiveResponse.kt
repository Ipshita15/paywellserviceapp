package com.cloudwell.paywell.services.activity.healthInsurance.model

import com.google.gson.annotations.SerializedName

data class ActiveResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("transactionId")
	val transactionId: String? = null
)
