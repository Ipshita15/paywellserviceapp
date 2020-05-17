package com.cloudwell.paywell.services.activity.entertainment.Bongo.model

import com.google.gson.annotations.SerializedName

data class BongoTrxResponse(

	@field:SerializedName("data")
	val data: ArrayList<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("customer_mobile_no")
	val customerMobileNo: String? = null,

	@field:SerializedName("reference_id")
	val referenceId: String? = null,

	@field:SerializedName("total_amount")
	val totalAmount: String? = null,

	@field:SerializedName("status_name")
	val statusName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("expiry_datetime")
	val expirytime: String? = null,

	@field:SerializedName("add_datetime")
	val addtime: String? = null
)
