package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class SeatViewData(

		@field:SerializedName("user_config_id")
		val userConfigId: String? = null,

		@field:SerializedName("uid")
		val uid: String? = null,

		@field:SerializedName("seatstructure_details")
		val seatstructureDetails: List<SeatstructureDetailsItem?>? = null,

		@field:SerializedName("bus_service_type")
		val busServiceType: String? = null,

		@field:SerializedName("row_no")
		val rowNo: String? = null,

		@field:SerializedName("bording_points")
		val bordingPoints: JSONObject? = null,

		@field:SerializedName("column_no")
		val columnNo: String? = null
)