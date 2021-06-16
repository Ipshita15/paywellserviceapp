package com.cloudwell.paywell.services.activity.bank_info_update.model

import com.google.gson.annotations.SerializedName

data class RemoveReqPojo(

	@field:SerializedName("ref_id")
	var refId: String? = null,

	@field:SerializedName("id")
	var id: String? = null,

	@field:SerializedName("username")
	var username: String? = null
)