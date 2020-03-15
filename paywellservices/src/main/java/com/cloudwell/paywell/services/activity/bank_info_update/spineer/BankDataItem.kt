package com.cloudwell.paywell.services.activity.bank_info_update.spineer
import com.google.gson.annotations.SerializedName

data class BankDataItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)