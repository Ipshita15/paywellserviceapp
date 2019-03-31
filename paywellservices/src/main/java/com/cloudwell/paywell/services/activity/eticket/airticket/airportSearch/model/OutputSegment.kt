package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.android.parcel.Parcelize

@Parcelize
class OutputSegment : Parcelable {

    @SerializedName("Airline")
    var airline: Airline? = null
    @SerializedName("Baggage")
    var baggage: String? = null
    @SerializedName("Destination")
    var destination: Destination? = null
    @SerializedName("Equipment")
    var equipment: String? = null
    @SerializedName("JourneyDuration")
    var journeyDuration: String? = null
    @SerializedName("Origin")
    var origin: Origin? = null
    @SerializedName("TripIndicator")
    var tripIndicator: String? = null

}
