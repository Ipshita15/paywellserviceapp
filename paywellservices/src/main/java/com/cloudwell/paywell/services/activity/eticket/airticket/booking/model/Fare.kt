package com.cloudwell.paywell.services.activity.eticket.airticket.booking.model

import com.google.gson.annotations.SerializedName


class Fare {

    @SerializedName("base_fare")
    var baseFare: String? = null
    @SerializedName("booking_id")
    var bookingId: String? = null
    @SerializedName("convenience_fee")
    var convenienceFee: String? = null
    @SerializedName("discount")
    var discount: String? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("other_charges")
    var otherCharges: String? = null
    @SerializedName("passenger_count")
    var passengerCount: String? = null
    @SerializedName("pax_type")
    var paxType: String? = null
    @SerializedName("service_fee")
    var serviceFee: String? = null
    @SerializedName("tax")
    var tax: String? = null

}
