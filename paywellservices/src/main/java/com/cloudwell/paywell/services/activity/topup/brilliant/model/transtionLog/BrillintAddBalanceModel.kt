package com.cloudwell.paywell.services.activity.topup.brilliant.model.transtionLog

import com.google.gson.annotations.SerializedName

data class BrillintAddBalanceModel(
		@field:SerializedName("ref_id")
		var refId: String? = "",

		@field:SerializedName("password")
		var password: String = "",

		@field:SerializedName("amount")
		var amount: String = "",

		@field:SerializedName("format")
		var format: String = "",

		@field:SerializedName("brilliantNumber")
		var brilliantNumber: String = "",

		@field:SerializedName("username")
		var username: String = ""
)
