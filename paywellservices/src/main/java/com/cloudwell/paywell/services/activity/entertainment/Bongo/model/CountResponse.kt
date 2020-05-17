package com.cloudwell.paywell.services.activity.entertainment.Bongo.model

import com.google.gson.annotations.SerializedName

data class CountResponse(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
