package com.cloudwell.paywell.services.activity.topup.model

import com.google.gson.annotations.SerializedName

data class TranscationLogResponseModel(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("ResponseDetails")
	val responseDetails: List<ResponseDetailsItem?>? = null
)