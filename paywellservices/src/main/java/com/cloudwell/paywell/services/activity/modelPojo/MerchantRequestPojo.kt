package com.cloudwell.paywell.services.activity.modelPojo

import com.google.gson.annotations.SerializedName

data class MerchantRequestPojo(

	@field:SerializedName("ref_id")
	var refId: String = "",

	@field:SerializedName("businessTypeName")
	var businessTypeName: String = "",

	@field:SerializedName("businessType")
	var businessType: String = "",

	@field:SerializedName("merchantType")
	var merchantType: String = "",

	@field:SerializedName("username")
	var username: String = ""

)