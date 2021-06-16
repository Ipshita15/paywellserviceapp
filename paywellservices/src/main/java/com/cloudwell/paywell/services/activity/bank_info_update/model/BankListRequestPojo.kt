package com.cloudwell.paywell.services.activity.bank_info_update.model

import com.google.gson.annotations.SerializedName

data class BankListRequestPojo(

	@field:SerializedName("ref_id")
	var refId: String? = "",


	@field:SerializedName("username")
	var username: String? = ""

)