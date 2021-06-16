package com.cloudwell.paywell.services.activity.healthInsurance.model

import com.google.gson.annotations.SerializedName

data class TrxRequest(

	@field:SerializedName("limit")
	var limit: String? = null,

	@field:SerializedName("username")
	var username: String? = null
)
