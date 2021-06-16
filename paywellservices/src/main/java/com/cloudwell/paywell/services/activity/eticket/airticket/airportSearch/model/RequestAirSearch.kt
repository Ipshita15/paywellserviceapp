package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model

import com.google.gson.annotations.SerializedName


data class RequestAirSearch(
        @SerializedName("AdultQuantity")
        var adultQuantity: Long = 0,

        @SerializedName("ChildQuantity")
        var childQuantity: Long = 0,

        @SerializedName("InfantQuantity")
        var infantQuantity: Long = 0,

        @SerializedName("JourneyType")
        var journeyType: String = "",

        @SerializedName("Segments")
        var segments: List<Segment> = mutableListOf()

)



