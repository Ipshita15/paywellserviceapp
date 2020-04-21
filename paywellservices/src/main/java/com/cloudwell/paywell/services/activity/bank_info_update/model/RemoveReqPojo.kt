package com.cloudwell.paywell.services.activity.bank_info_update.model

import com.google.gson.annotations.SerializedName

data class RemoveReqPojo(

	@field:SerializedName("ref_id")
	val refId: String? = null,

	@field:SerializedName("format")
	val format: String? = null,

	@field:SerializedName("channel")
	val channel: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deviceId")
	val deviceId: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)