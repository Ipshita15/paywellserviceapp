package com.cloudwell.paywell.services.activity.education.BBC.model

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("name")
	var name: String = "",

	@field:SerializedName("mobile")
	var mobile: String = "",

	@field:SerializedName("course")
	var course: String = ""
)