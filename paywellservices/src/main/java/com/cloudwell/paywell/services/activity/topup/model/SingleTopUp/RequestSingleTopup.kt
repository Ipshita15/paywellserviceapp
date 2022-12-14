package com.cloudwell.paywell.services.activity.topup.model.SingleTopUp

import com.google.gson.annotations.SerializedName

data class RequestSingleTopup(

		@field: SerializedName("clientTrxId")
	var clientTrxId: String = "",

		@field: SerializedName("password")
	var password: String = "",

		@field: SerializedName("amount")
	var amount: Int = 0,

		@field: SerializedName("con_type")
	var conType: String = "",

		@field: SerializedName("msisdn")
	var msisdn: String = "",

		@field: SerializedName("operator")
	var operator: String = "",

		@field: SerializedName("username")
	var username: String = ""
)
