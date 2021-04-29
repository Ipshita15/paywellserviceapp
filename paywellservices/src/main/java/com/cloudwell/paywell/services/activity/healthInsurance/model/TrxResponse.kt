package com.cloudwell.paywell.services.activity.healthInsurance.model

import com.google.gson.annotations.SerializedName

data class TrxResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("TransactionData")
	val transactionData: List<TransactionDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class TransactionDataItem(

	@field:SerializedName("trx_id")
	val trxId: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("active_datetime")
	val activeDatetime: String? = null,

	@field:SerializedName("status_name")
	val statusName: String? = null,

	@field:SerializedName("customer_name")
	val customerName: String? = null,

	@field:SerializedName("customer_phoneNumber")
	val customerPhoneNumber: String? = null,

	@field:SerializedName("add_datetime")
	val addDatetime: String? = null,

	@field:SerializedName("reasone")
	val reasone: Any? = null,

	@field:SerializedName("name_en")
	val nameEn: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
