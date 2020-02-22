package com.cloudwell.paywell.services.activity.topup.model

import com.google.gson.annotations.SerializedName

data class RechargeOfferRequestModel(

	@field:SerializedName("username")
	var username: String = "",

	@field:SerializedName("subServiceId")
	var subServiceId: String = ""
)