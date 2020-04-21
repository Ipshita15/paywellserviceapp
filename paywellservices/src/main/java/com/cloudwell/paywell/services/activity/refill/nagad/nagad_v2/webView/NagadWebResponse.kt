package com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView

import com.google.gson.annotations.SerializedName

data class NagadWebResponse(

	@field:SerializedName("ApiStatusName")
	val apiStatusName: String? = null,

	@field:SerializedName("ApiStatus")
	val apiStatus: Int? = null,

	@field:SerializedName("ResponseDetails")
	val responseDetails: ResponseDetails? = null
)