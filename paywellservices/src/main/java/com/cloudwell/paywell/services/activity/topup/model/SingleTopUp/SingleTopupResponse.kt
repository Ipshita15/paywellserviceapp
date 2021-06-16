package com.cloudwell.paywell.services.activity.topup.model.SingleTopUp


import com.google.gson.annotations.SerializedName


data class SingleTopupResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("hotlineNumber")
	val hotlineNumber: String? = null
)