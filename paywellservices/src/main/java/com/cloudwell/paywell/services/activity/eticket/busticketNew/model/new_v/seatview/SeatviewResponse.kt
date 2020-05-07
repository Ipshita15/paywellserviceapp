package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview

import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo
import com.google.gson.annotations.SerializedName
import java.util.HashMap

data class SeatviewResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("seatViewData")
	val seatViewData: SeatViewData? = null
) {
	lateinit var boothInfoHashMap: HashMap<String, BoothInfo>
}