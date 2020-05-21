package com.cloudwell.paywell.services.activity.entertainment.bongo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseDetailsItem(

	@field:SerializedName("trxId")
	val trxId: String? = null,

	@field:SerializedName("customer_mobile_no")
	val customerMobileNo: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("add_datetime")
	val addDatetime: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: String? = null,

	@field:SerializedName("statusName")
	val statusName: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("total_charge")
	val totalCharge: String? = null,

	@field:SerializedName("total_amount")
	val totalAmount: String? = null
)
