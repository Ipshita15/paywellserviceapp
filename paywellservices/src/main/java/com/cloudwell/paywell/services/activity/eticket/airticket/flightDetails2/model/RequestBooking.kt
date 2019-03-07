package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class RequestBooking {

    @SerializedName("Passengers")
    var passengers: List<Passenger>? = null
    @SerializedName("ResultID")
    var resultID: String? = null
    @SerializedName("SearchId")
    var searchId: String? = null

}
