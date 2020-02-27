package com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log

import com.google.gson.annotations.SerializedName

data class BalanceClaimModel(

	@field:SerializedName("trxOrPhoneNo")
	var trxOrPhoneNo: String = "",

	@field:SerializedName("ref_id")
	var refId: String = "",

	@field:SerializedName("amount")
	var amount: String = "",

	@field:SerializedName("password")
	var password: String = "",

	@field:SerializedName("gateway_id")
	var gatewayId: String = "",

	@field:SerializedName("username")
	var username: String = ""


)