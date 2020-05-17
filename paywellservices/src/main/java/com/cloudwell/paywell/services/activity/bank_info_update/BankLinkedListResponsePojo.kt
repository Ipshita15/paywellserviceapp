package com.cloudwell.paywell.services.activity.bank_info_update

import com.google.gson.annotations.SerializedName

data class BankLinkedListResponsePojo(

	@field:SerializedName("bankDetailsList")
	val bankDetailsList: List<BankDetailsListItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class BankDetailsListItem(

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("districtName")
	val districtName: String? = null,

	@field:SerializedName("checkBookImage")
	val checkBookImage: String? = null,

	@field:SerializedName("bankName")
	val bankName: String? = null,

	@field:SerializedName("user_account")
	val userAccount: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("branchname")
	val branchname: String? = null
)
