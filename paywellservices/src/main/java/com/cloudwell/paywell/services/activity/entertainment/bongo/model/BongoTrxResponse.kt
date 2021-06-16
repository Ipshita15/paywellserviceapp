package com.cloudwell.paywell.services.activity.entertainment.bongo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BongoTRXresponse(

	@field:SerializedName("Status")
	val status: Int? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("ResponseDetails")
	val responseDetails: ArrayList<ResponseDetailsItem?>? = null
)