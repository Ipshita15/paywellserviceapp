package com.cloudwell.paywell.services.activity.topup.model

import com.google.gson.annotations.SerializedName

data class RechargeEnqueryModel(

	@field:SerializedName("msisdn")
	var msisdn: String = "",

	@field:SerializedName("username")
	var username: String = ""
)