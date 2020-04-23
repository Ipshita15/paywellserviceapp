package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v
import com.google.gson.annotations.SerializedName

class BusLunCityResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("citiesList")
	val citiesList: List<CitiesListItem> = mutableListOf(),

	@field:SerializedName("message")
	val message: String? = null
)