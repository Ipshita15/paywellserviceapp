package com.cloudwell.paywell.services.activity.education.BBC.model

import com.google.gson.annotations.SerializedName

data class CourseListRresponsePojo(

	@field:SerializedName("courses")
	val courses: List<CoursesItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)