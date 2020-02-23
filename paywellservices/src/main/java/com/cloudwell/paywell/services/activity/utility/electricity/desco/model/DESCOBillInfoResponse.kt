package com.cloudwell.paywell.services.activity.utility.electricity.desco.model

import com.google.gson.annotations.SerializedName

data class DESCOBillInfoResponse(

	@field:SerializedName("ApiStatusName")
	val apiStatusName: String? = null,

	@field:SerializedName("ApiStatus")
	val apiStatus: Int? = null,

	@field:SerializedName("ResponseDetails")
	val responseDetails: ResponseDetails? = null
)