package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.android.parcel.Parcelize


@Parcelize
class Fare : Parcelable {
    @SerializedName("AgentMarkUp")
    var agentMarkUp: Double = 0.0
    @SerializedName("BaseFare")
    var baseFare: Double = 0.0
    @SerializedName("Currency")
    var currency: String = ""
    @SerializedName("Discount")
    var discount: Double = 0.0
    @SerializedName("OtherCharges")
    var otherCharges: Double = 0.0
    @SerializedName("PassengerCount")
    var passengerCount: Double = 0.0
    @SerializedName("PaxType")
    var paxType: String = ""
    @SerializedName("ServiceFee")
    var serviceFee: Double = 0.0
    @SerializedName("Tax")
    var tax: Double = 0.0

    @SerializedName("ConvenienceFee")
    var convenienceFee: Double = 0.0

    @SerializedName("Amount")
    var amount: String = ""


}
