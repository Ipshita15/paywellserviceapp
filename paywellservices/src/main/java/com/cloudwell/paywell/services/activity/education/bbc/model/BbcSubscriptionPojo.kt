package com.cloudwell.paywell.services.activity.education.bbc.model

import com.google.gson.annotations.SerializedName

data class BbcSubscriptionPojo(

	@field:SerializedName("pin_no")
	var pinNo: String = "",

	@field:SerializedName("data")
	var data: Data? = null,

	@field:SerializedName("username")
	var username: String = ""
)