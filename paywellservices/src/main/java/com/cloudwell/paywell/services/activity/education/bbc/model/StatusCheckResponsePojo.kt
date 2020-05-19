package com.cloudwell.paywell.services.activity.education.bbc.model

import com.google.gson.annotations.SerializedName

data class StatusCheckResponsePojo(

	@field:SerializedName("courses")
	val courses: List<StatusCheckCourseItem?>? = null,

	@field:SerializedName("msg_text")
	val msgText: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)