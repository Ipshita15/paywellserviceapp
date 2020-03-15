package com.cloudwell.paywell.services.activity.bank_info_update.spineer

import com.google.gson.annotations.SerializedName

data class BankResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("bankData")
	val bankData: List<BankDataItem>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)