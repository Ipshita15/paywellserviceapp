package com.cloudwell.paywell.services.activity.education.BBC.model

import com.google.gson.annotations.SerializedName

data class ResponseDetailsItem(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("course_name")
	val courseName: String? = null,

	@field:SerializedName("mobile_no")
	val mobileNo: String? = null,

	@field:SerializedName("statusName")
	val statusName: String? = null,

	@field:SerializedName("subscriptionId")
	val subscriptionId: String? = null,

	@field:SerializedName("trxId")
	val trxId: String? = null,

	@field:SerializedName("bbc_userId")
	val bbcUserId: String? = null,

	@field:SerializedName("add_datetime")
	val addDatetime: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: String? = null
)