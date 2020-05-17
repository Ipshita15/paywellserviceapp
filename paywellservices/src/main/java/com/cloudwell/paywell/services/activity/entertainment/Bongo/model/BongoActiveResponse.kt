package com.cloudwell.paywell.services.activity.entertainment.Bongo.model

import com.google.gson.annotations.SerializedName

data class BongoActiveResponse(

	@field:SerializedName("trans_id")
	val transId: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
