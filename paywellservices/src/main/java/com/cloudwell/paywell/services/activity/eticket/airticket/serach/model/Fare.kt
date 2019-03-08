package com.cloudwell.paywell.services.activity.eticket.airticket.serach.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Fare : Parcelable {

    @SerializedName("AgentMarkUp")
    var agentMarkUp: Long = 0
    @SerializedName("BaseFare")
    var baseFare: Long = 0
    @SerializedName("Currency")
    var currency: String = ""
    @SerializedName("Discount")
    var discount: Long = 0
    @SerializedName("OtherCharges")
    var otherCharges: Long = 0
    @SerializedName("PassengerCount")
    var passengerCount: Long = 0
    @SerializedName("PaxType")
    var paxType: String = ""
    @SerializedName("ServiceFee")
    var serviceFee: Long = 0
    @SerializedName("Tax")
    var tax: Long = 0

}
