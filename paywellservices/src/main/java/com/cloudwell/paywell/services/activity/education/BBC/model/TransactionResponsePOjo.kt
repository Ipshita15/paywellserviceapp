package com.cloudwell.paywell.services.activity.education.BBC.model

import com.google.gson.annotations.SerializedName

data class TransactionResponsePOjo(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("ResponseDetails")
	val responseDetails: List<ResponseDetailsItem?>? = null
)