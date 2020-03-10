package com.cloudwell.paywell.services.activity.location.model

import com.google.gson.annotations.SerializedName

data class CurrentLocationModel(

		@field:SerializedName("ref_id")
		var refId: String = "",

		@field:SerializedName("country")
		var country: String = "",

		@field:SerializedName("address")
		var address: String = "",

		@field:SerializedName("latitude")
		var latitude: String = "",

		@field:SerializedName("format")
		var format: String = "",

		@field:SerializedName("channel")
		var channel: String = "",

		@field:SerializedName("accuracy")
		var accuracy: String = "",

		@field:SerializedName("deviceId")
		var deviceId: String = "",

		@field:SerializedName("longitude")
		var longitude: String = "",

		@field:SerializedName("username")
		var username: String = "",

		@field:SerializedName("timestamp")
		var timestamp: String = ""
)