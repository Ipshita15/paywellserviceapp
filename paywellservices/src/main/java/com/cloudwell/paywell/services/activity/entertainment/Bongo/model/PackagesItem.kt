package com.cloudwell.paywell.services.activity.entertainment.Bongo.model

import com.google.gson.annotations.SerializedName

data class PackagesItem(

	@field:SerializedName("period")
	val period: String? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("price")
	val price: Double? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)