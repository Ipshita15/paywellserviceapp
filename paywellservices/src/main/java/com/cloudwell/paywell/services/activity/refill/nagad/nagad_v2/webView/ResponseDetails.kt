package com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView

import com.google.gson.annotations.SerializedName

data class ResponseDetails(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("redirect_link")
	val redirectLink: String? = null,

	@field:SerializedName("StatusName")
	val statusName: String? = null
)