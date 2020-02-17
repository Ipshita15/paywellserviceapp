package com.cloudwell.paywell.services.activity.topup.brilliant.model.transtionLog

import com.google.gson.annotations.SerializedName

data class EnqueryModel(
		@field:SerializedName("password")
		var password: String = "",

		@field:SerializedName("brilliant_number")
		var brilliantNumber: String = "",

		@field:SerializedName("format")
		var format: String = "",

		@field:SerializedName("username")
		var username: String = ""
)
