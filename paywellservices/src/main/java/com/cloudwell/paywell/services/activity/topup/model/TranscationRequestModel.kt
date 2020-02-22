package com.cloudwell.paywell.services.activity.topup.model

import com.google.gson.annotations.SerializedName

data class TranscationRequestModel(

	@field:SerializedName("service")
	var service: String = "",

	@field:SerializedName("limit")
	var limit: String = "",

	@field:SerializedName("username")
	var username: String = ""
)