package com.cloudwell.paywell.services.activity.topup.model.SingleTopUp

import com.google.gson.annotations.SerializedName

data class TopupData(

	@field:SerializedName("clientTrxId")
	val clientTrxId: String = "",

	@field:SerializedName("amount")
	val amount: String = "",

	@field:SerializedName("con_type")
	val conType: String = "",

	@field:SerializedName("msisdn")
	val msisdn: String = "",

	@field:SerializedName("operator")
	val operator: String = ""
)