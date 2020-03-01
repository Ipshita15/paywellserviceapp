package com.cloudwell.paywell.services.activity.BBC.model

import com.google.gson.annotations.SerializedName

data class BbcSubscriptionPojo(

	@field:SerializedName("pin_no")
	val pinNo: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("format")
	val format: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)