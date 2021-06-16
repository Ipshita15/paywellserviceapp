package com.cloudwell.paywell.services.activity.topup.model.SingleTopUp

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("trans_id")
	val transId: String? = null,

	@field:SerializedName("topupData")
	val topupData: TopupData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)