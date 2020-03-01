package com.cloudwell.paywell.services.activity.BBC.model

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("course")
	val course: String? = null
)