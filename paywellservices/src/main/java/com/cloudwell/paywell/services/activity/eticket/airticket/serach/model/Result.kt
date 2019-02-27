package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import com.google.gson.annotations.SerializedName


class Result {

    @SerializedName("Availabilty")
    var availabilty: Long? = null
    @SerializedName("Currency")
    var currency: String? = null
    @SerializedName("Discount")
    var discount: Long? = null
    @SerializedName("FareType")
    var fareType: String? = null
    @SerializedName("Fares")
    var fares: List<Fare>? = null
    @SerializedName("IsRefundable")
    var isRefundable: Boolean? = null
    @SerializedName("LastTicketDate")
    var lastTicketDate: Any? = null
    @SerializedName("ResultID")
    var resultID: String? = null
    @SerializedName("segments")
    var segments: List<OutputSegment>? = null
    @SerializedName("TotalFare")
    var totalFare: Long? = null
    @SerializedName("TotalFareWithAgentMarkup")
    var totalFareWithAgentMarkup: Long? = null
    @SerializedName("Validatingcarrier")
    var validatingcarrier: String? = null

}
