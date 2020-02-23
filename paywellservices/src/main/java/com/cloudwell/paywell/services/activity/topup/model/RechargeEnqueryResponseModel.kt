package com.cloudwell.paywell.services.activity.topup.model

import com.google.gson.annotations.SerializedName

data class RechargeEnqueryResponseModel(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("hotline")
	val hotline: String? = null,

	@field:SerializedName("enquiryData")
	val enquiryData: EnquiryData? = null,

	@field:SerializedName("message")
	val message: String? = null
)