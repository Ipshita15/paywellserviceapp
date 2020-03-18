package com.cloudwell.paywell.services.activity.education.BBC.model

import com.google.gson.annotations.SerializedName

data class CoursesItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("course_name")
	val courseName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("course_no")
	val courseNo: String? = null
)