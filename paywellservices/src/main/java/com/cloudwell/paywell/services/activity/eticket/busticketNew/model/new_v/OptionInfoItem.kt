package com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v

import com.google.gson.annotations.SerializedName

data class OptionInfoItem(

        @field:SerializedName("seat")
        var seat: String = "",

        var seatLevel: String = "",

        @field:SerializedName("boardingPointId")
        var boardingPointId: String = "",

        @field:SerializedName("optionId")
        var optionId: String = "",

        @field:SerializedName("departureDate")
        var departureDate: String = ""
)