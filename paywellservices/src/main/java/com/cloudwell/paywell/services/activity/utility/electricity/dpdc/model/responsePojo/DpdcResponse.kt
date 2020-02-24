package com.cloudwell.paywell.services.activity.utility.electricity.dpdc.model.responsePojo

import com.google.gson.annotations.SerializedName

data class DpdcResponse(

	@field:SerializedName("ApiStatusName")
	val apiStatusName: String? = null,

	@field:SerializedName("ApiStatus")
	val apiStatus: Int? = null,

	@field:SerializedName("ResponseDetails")
	val responseDetails: ResponseDetails? = null
)