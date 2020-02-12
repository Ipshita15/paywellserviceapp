package com.cloudwell.paywell.services.activity.topup.brilliant.model.transtionLog

import com.google.gson.annotations.SerializedName


data class ResponseBrillintTNXLog(

	@field:SerializedName("number")
	val number: String = "",

	@field:SerializedName("password")
	val password: String = "",

	@field:SerializedName("format")
	val format: String = "",

	@field:SerializedName("username")
	val username: String = ""
)