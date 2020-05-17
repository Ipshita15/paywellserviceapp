package com.cloudwell.paywell.services.activity.entertainment.bongo.model

import com.google.gson.annotations.SerializedName

data class BongoResponsePojo(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("packages")
	val packages: ArrayList<PackagesItem?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)