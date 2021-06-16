package com.cloudwell.paywell.services.activity.bank_info_update.spineer
import com.google.gson.annotations.SerializedName

data class BankDataItem(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("id")
	var id: String? = null
)