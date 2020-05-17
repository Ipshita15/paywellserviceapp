package com.cloudwell.paywell.services.activity.bank_info_update.model

import com.google.gson.annotations.SerializedName

data class BankDialogPojo(


	@field:SerializedName("branch")
	var branch: String? = null,

	@field:SerializedName("bank")
	var bank: String? = null,

	@field:SerializedName("district")
	var district: String? = null,

	@field:SerializedName("accountNo")
	var account: String? = null

)