package com.cloudwell.paywell.services.activity.education.bbc.model

import com.google.gson.annotations.SerializedName

data class StatusCheckCourseItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("expiry_datetime")
	val expiryDatetime: String? = null,

	@field:SerializedName("course_name")
	val courseName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("course_no")
	val courseNo: String? = null,

	@field:SerializedName("expire_in")
	val expireIn: String? = null,

	@field:SerializedName("add_datetime")
	val addDatetime: String? = null
)