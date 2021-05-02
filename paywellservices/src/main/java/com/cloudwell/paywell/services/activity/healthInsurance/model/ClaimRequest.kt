package com.cloudwell.paywell.services.activity.healthInsurance.model

import com.google.gson.annotations.SerializedName

data class ClaimRequest(

	@field:SerializedName("phone")
	var phone: String? = null,

	@field:SerializedName("username")
    var username: String? = null
)
